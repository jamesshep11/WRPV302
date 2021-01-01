package com.example.segrada;

import com.example.segrada.PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class ClientController extends Thread {
    private String serverAddress;
    private Socket conn = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Broker broker;
    private boolean running;

    public ClientController(String serverAddress) {
        super(serverAddress);
        this.serverAddress = serverAddress;
        broker = Broker.getInstance();
        broker.subscribe("CloseConnection", ((publisher, topic, params) -> running = false));
        running = false;
    }

    // Send a message to the server.
    private void sendMessage(Map<String, Object> message) {
        new Thread(() -> {
            try {
                oos.writeObject(message);
                oos.flush();
                System.out.println("CLIENT>>> " + message);
            } catch (IOException ioException) {
            System.out.println("ERROR: Error writing object");
        }
        }).start();
    }

    @Override
    public void run() {
        try {
            // Connect to server.
            conn = new Socket(serverAddress, 500);
            System.out.println("Connecting to: " + conn.getInetAddress().getHostName());

            getStreams();

            running = true;
            // Read messages from server until told to terminate.
            while (running) {
                Map<String, Object> message = (Map<String, Object>) ois.readObject();
                broker.publish(this, (String) message.get("topic"), message);
            }
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        finally {
            // Close connection.
            close();

            System.out.println("Disconnected...");
        }
    }

    // Get and initialise streams.
    private void getStreams() throws IOException {
        oos = new ObjectOutputStream(conn.getOutputStream());
        oos.flush();

        ois = new ObjectInputStream(conn.getInputStream());

        System.out.println("Got I/O streams");
    }

    // Close streams and connection
    private void close() {
        try {
            oos.close();
            ois.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
