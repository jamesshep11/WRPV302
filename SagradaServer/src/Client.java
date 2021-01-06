import PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Client {
    // Communication variable
    private final Socket connection;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    // Control input and output streams on separate threads
    private ReadThread readThread;
    private WriteThread writeThread;

    private final Broker broker;

    // Filter the output through a blocking Queue to avoid garbled output
    private BlockingQueue<Object> objectsOut = new LinkedBlockingDeque<>();

    public Client(Socket connection, Broker broker) {
        this.connection = connection;
        this.broker = broker;
        getStreams();

        readThread = new ReadThread();
        readThread.start();
        writeThread = new WriteThread();
        writeThread.start();
    }

    private void getStreams() {
        try {
            oos = new ObjectOutputStream(connection.getOutputStream());
            oos.flush();

            ois = new ObjectInputStream(connection.getInputStream());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendObject(Object object){
        try {
            objectsOut.put(object);
            System.out.println(((HashMap<String, Object>)objectsOut.peek()).get("player"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ReadThread extends Thread{
        @Override
        public void run() {
            try {
                HashMap<String, Object> message;
                do {
                    // Read message from Client
                    message = (HashMap<String, Object>)ois.readObject();
                    broker.publish(this, (String)message.get("topic"), message);
                } while (!(message.get("topic")).equals("CloseConnection"));
            } catch (Exception e){
                e.printStackTrace();
            }
            finally{
                closeConnection();
            }
        }
    }

    private class WriteThread extends Thread{
        @Override
        public void run() {
            try {
                while(!isInterrupted()) {
                    Object object = objectsOut.take();

                    oos.writeObject(object);
                    oos.flush();

                    System.out.println("CLIENT>>> " + ((HashMap<String, Object>) object).get("player"));
                }
            } catch (Exception e) {
                System.out.println("Error writing object. " + e.getMessage());
                e.printStackTrace();
            }
            finally {
                writeThread = null;
            }
        }
    }

    private void closeConnection(){
        System.out.println("User terminated connection.");
        try {
            oos.close();
            ois.close();
            connection.close();

            writeThread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
