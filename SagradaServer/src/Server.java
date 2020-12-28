import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	// Server related fields.
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerSocket server;
	private Socket connection;
	private int counter = 1;

	private Runnable onConnected;
	private Runnable onDisconnected;

	public Server(Runnable onConnected, Runnable onDisconnected) {
		this.onConnected = onConnected;
		this.onDisconnected = onDisconnected;
		run();
	}

	public static void main(String args[]) {
		new Server(null, // What to do when connected (run on UI thread).
				null // What to do when connected (run on UI thread).
		);
	}

	protected void display(String message) {
		System.out.println(message);
	}

	public void run() {
		try {
			// Step 1: Create a ServerSocket.
			server = new ServerSocket(5001, 100);
			display("Started server: " + InetAddress.getLocalHost().getHostAddress());

			while (true) {
				// Step 2: Wait for a connection.
				waitForConnection();

				// Step 3: Get input and output streams.
				getStreams();

				// Step 4: Process connection.
				readMessages();

				// Step 5: Close connection.
				closeConnection();

				++counter;
			}
		}
		catch (EOFException eofException) {
			display("Client terminated connection");

		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Send message to client
	public void sendMessage(String message) {
		try {
			message = "SERVER>>> " + message;

			// Send String OBJECT to client.
			oos.writeObject(message);
			// Flush to ensure that data is pushed through stream to client.
			oos.flush();

			display(message);

		} catch (IOException ioException) {
			display("Error writing object. " + ioException.getMessage());
		}
	}

	// Wait for connection to arrive, then display connection info
	private void waitForConnection() throws IOException {
		display("Waiting for connection...");

		connection = server.accept();

		display("Connection #" + counter + " received from: "
				+ connection.getInetAddress().getHostName());
	}

	// Get streams to send and receive data
	private void getStreams() throws IOException {
		oos = new ObjectOutputStream(connection.getOutputStream());
		oos.flush();

		ois = new ObjectInputStream(connection.getInputStream());

		display("Got I/O streams.");
	}

	// Process connection with client
	private void readMessages() throws IOException {
		// Send initial message to client.
		String message = "Connection successful. Client #" + counter;
		sendMessage(message);

		// Connected
		if(onConnected != null) onConnected.run();

		do {
			try {
				message = (String) ois.readObject();
				display(message);

			} catch (ClassNotFoundException classNotFoundException) {
				display("Unknown object type received.");
			}

		} while (!message.equals("CLIENT>>> TERMINATE"));
	}

	// close streams and socket
	private void closeConnection() throws IOException {
		display("User terminated connection.");
		oos.close();
		ois.close();
		connection.close();

		if(onDisconnected != null) onDisconnected.run();
	}
}
