import PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class Client {
    private Socket connection;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Broker broker;

    public Client(Socket connection, ObjectOutputStream oos, ObjectInputStream ois) {
        this.connection = connection;
        this.oos = oos;
        this.ois = ois;
        broker = Broker.getInstance();

        readMessages();
    }

    private void readMessages(){
        new Thread(() -> {
            try {
                Map<String, Object> message;
                do {
                    // Read message from Client
                    message = (Map<String, Object>) ois.readObject();
                    broker.publish(this, (String)message.get("topic"), message);
                } while (!message.get("topic").equals("TERMINATE"));
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (ClassNotFoundException classNotFoundException) {
                System.out.println("Unknown object type received.");
            }
        }).start();
    }

    public void sendMessage(Object message){
        new Thread(() -> {
            try {
                // Send String OBJECT to client.
                oos.writeObject(message);
                // Flush to ensure that data is pushed through stream to client.
                oos.flush();
            } catch (IOException ioException) {
                System.out.println("Error writing object. " + ioException.getMessage());
            }
        }).start();
    }

    public void closeConnection(){
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
