package com.example.segrada;

import android.util.Log;

import com.example.segrada.PubSubBroker.Broker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientController {
    static private ClientController instance;

    // Communication variables
    private Socket conn = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    private ReadThread readThread;
    private WriteThread writeThread;

    private Broker broker;

    private BlockingQueue<Object> objectsOut = new LinkedBlockingQueue<>();

    private ClientController(String serverAddress) {
        connectToServer(serverAddress);
        broker = Broker.getInstance();
    }

    static public ClientController getInstance(String serverAddress){
        if (instance == null)
            instance = new ClientController(serverAddress);

        return instance;
    }

    private void connectToServer(String serverAddress){
        new Thread(()->{
            try {
                // Connect to server
                conn = new Socket(serverAddress, 500);
                Log.i("Client>>> ", "Connecting to: " + conn.getInetAddress().getHostName());

                // Get IO streams
                ois = new ObjectInputStream(conn.getInputStream());
                oos = new ObjectOutputStream(conn.getOutputStream());
                oos.flush();

                // Start read/write threads
                readThread = new ReadThread();
                readThread.start();
                writeThread = new WriteThread();
                writeThread.start();

                Log.i("Client>>> ", "Got I/O streams");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendObject(Object object) {
        try {
            objectsOut.put(object);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Read objects from the server
    private class ReadThread extends Thread{
        @Override
        public void run() {
            try {
                HashMap<String, Object> message;
                do {
                    // Read and broadcast message.
                    message = (HashMap<String, Object>) ois.readObject();
                    broker.publish(this, (String) message.get("topic"), message);

                    Log.i("SERVER>>> ", (String)message.get("topic"));
                } while (!message.get("topic").equals("CloseConnection"));
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    // Send objects to the server
    private class WriteThread extends Thread{
        @Override
        public void run() {
            try {
                while(!isInterrupted()) {
                    Object object = objectsOut.take();

                    oos.writeObject(object);
                    oos.flush();

                    Log.i("Client>>> ", (String)((HashMap<String, Object>)object).get("topic"));
                }
            } catch (Exception e) {
                System.out.println("ERROR: Error writing object");
            }
        }
    }

    // Close streams and connection
    private void closeConnection() {
        try {
            oos.close();
            ois.close();
            conn.close();

            readThread = null;
            if (writeThread != null) writeThread.interrupt();
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
