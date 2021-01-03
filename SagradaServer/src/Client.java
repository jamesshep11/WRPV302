import PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Client {
    private Socket connection;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Broker broker;
    private boolean running;

    public Client(Socket connection, Broker broker) {
        this.connection = connection;
        getStreams();
        this.broker = broker;
        this.running = true;

        this.broker.subscribe("CloseConnection", ((publisher, topic, params) -> running = false));

        readObjects();
    }

    // Get streams to send and receive data
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

    // This is the only method that needs to be customized for the given project
    // Defines how to handle input received from the client
    private void handleInput(Object object){
        HashMap<String, Object> params = (HashMap<String, Object>) object;
        broker.publish(this, (String)params.get("topic"), params);
    }

    private void readObjects(){
        new Thread(() -> {
            try {
                do {
                    // Read message from Client
                    Object object = ois.readObject();
                    handleInput(object);
                } while (running);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (ClassNotFoundException classNotFoundException) {
                System.out.println("Unknown object type received.");
            }
            finally{
                closeConnection();
            }
        }).start();
    }

    public void sendObjects(Object object){
        new Thread(() -> {
            try {
                // Send String OBJECT to client.
                oos.writeObject(object);
                // Flush to ensure that data is pushed through stream to client.
                oos.flush();
            } catch (IOException ioException) {
                System.out.println("Error writing object. " + ioException.getMessage());
                ioException.printStackTrace();
            }
        }).start();
    }

    private void closeConnection(){
        System.out.println("User terminated connection.");
        try {
            oos.close();
            ois.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
