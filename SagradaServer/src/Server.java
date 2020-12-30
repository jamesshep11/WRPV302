import PubSubBroker.Broker;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	// Server related fields.
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ServerSocket server;
	private Socket connection;

	private Broker broker;

	private ArrayList<Client> clients;
	private ArrayList<Game> games;

	public Server() {
		broker = Broker.getInstance();
		broker.subscribe("CloseConnection", ((publisher, topic, params) -> ((Client)params.get("client")).closeConnection()));

		clients = new ArrayList<>();
		games = new ArrayList<>();
	}

	public void run() {
		try {
			// Create server socket.
			server = new ServerSocket(500, 100);
			System.out.println("Started server: " + InetAddress.getLocalHost().getHostAddress());

			// Get 4 clients for a game
			while (true) {
				try {
					for (int i = 1; i <= 4; i++) {
						waitForConnection(i);
						getStreams();

						//Create client using global values from waitingForConn() and getStreams()
						Client client = new Client(connection, oos, ois);
						clients.add(client);

						System.out.println("Added client #" + clients.size() + 1);
					}
				}
				catch (EOFException eofException) {
					System.out.println("Client terminated connection");
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Wait for connection to arrive, then display connection info
	private void waitForConnection(int i) throws IOException {
		System.out.println("Waiting for player " + i);

		connection = server.accept();

		// size()+2 because connection exists but client not added to list yet
		System.out.println("Connection #" + clients.size()+2 + " received from: "
				+ connection.getInetAddress().getHostName());
	}

	// Get streams to send and receive data
	private void getStreams() throws IOException {
		oos = new ObjectOutputStream(connection.getOutputStream());
		oos.flush();

		ois = new ObjectInputStream(connection.getInputStream());

		// size()+2 because connection exists but client not added to list yet
		System.out.println("Got I/O streams for client #" + clients.size()+2);
	}
}
