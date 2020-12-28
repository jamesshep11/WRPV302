package com.example.segrada;

import com.example.segrada.PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientController extends Thread {
    private String serverAddress;
    private Socket conn = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    private Broker broker;

    public ClientController(String serverAddress) {
        super(serverAddress);
        this.serverAddress = serverAddress;
        broker = Broker.getInstance();
    }

    // Called by another thread to send a message.
    public void sendMessage(String message) {
        try {
            oos.writeObject("CLIENT>>> " + message);
            oos.flush();
            System.out.println("CLIENT>>>" + message);
        } catch (IOException ioException) {
            System.out.println("ERROR: Error writing object");
        }
    }

    @Override
    public void run() {
        try {
            // Connect to server.
            conn = new Socket(serverAddress, 500);
            System.out.println("Connecting to: " + conn.getInetAddress().getHostName());

            // Get and initialise streams.
            oos = new ObjectOutputStream(conn.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(conn.getInputStream());
            System.out.println("Got I/O streams");

            // Read messages from server until told to terminate.
            String message = (String) ois.readObject();
            broker.publish(this, message, null);

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());

        } finally {
            // Close connection.
            close();

            System.out.println("Disconnected...");
        }
    }

    private void close() {
        try {
            conn.close();

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
