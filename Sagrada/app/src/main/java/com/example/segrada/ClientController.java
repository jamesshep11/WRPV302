package com.example.segrada;

import com.example.segrada.PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
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
        broker.subscribe("CloseConnection", ((publisher, topic, params) -> {
            params.put("topic", "EndGame");
            sendObject(params);
            running = false;
        }));
        broker.subscribe("EndGame", ((publisher, topic, params) -> running = false));

        running = false;
    }

    // Send an object to the server.
    private void sendObject(Map<String, Object> message) {
        new Thread(() -> {
            try {
                oos.writeObject(message);
                oos.flush();
                System.out.println("CLIENT>>> " + message.get("topic"));
            } catch (IOException ioException) {
            System.out.println("ERROR: Error writing object");
        }
        }).start();
    }

    // Read an object from the server
    private void readObject() throws IOException, ClassNotFoundException {
        HashMap<String, Object> message = (HashMap<String, Object>) ois.readObject();
        broker.publish(this, (String) message.get("topic"), message);
        System.out.println("SERVER>>> " + message.get("topic"));
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
            while (running)
                readObject();
        }
        catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            // Close connection.
            close();

            System.out.println("Disconnected...");
        }
    }

    // Get and initialise streams.
    private void getStreams() throws IOException {
        ois = new ObjectInputStream(conn.getInputStream());

        oos = new ObjectOutputStream(conn.getOutputStream());
        oos.flush();

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
