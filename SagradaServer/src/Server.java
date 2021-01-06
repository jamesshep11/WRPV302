import PubSubBroker.Broker;
import PubSubBroker.Subscriber;
import javafx.application.Platform;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class Server {
	private ServerSocket server;
	private final Broker broker;
	private ArrayList<Game> games;

	public Server() throws Exception {
		broker = new Broker();

		games = new ArrayList<>();
		run();
	}

	public void run() throws Exception{
		// Create server socket.
		server = new ServerSocket(500, 100);
		System.out.println("Started server: " + InetAddress.getLocalHost().getHostAddress());

		// Listen for clients and start new games
		while (true) {
			// Initialize new client list and broker for a new game
			ArrayList<Client> clients = new ArrayList<>();
			Broker gameBroker = new Broker();
			// In case a client leaves before the game starts
			Subscriber removeClient = (publisher, topic, params) -> clients.remove(publisher);
			gameBroker.subscribe("CloseConnection", removeClient);

			// Wait for 4 clients to join
			while (clients.size() < 2) {
				Socket connection = server.accept();
				System.out.printf("Connection request received: %s\n", connection.getInetAddress().getHostAddress());

				//Create client using global values from waitingForConn() and getStreams()
				Client client = new Client(connection, gameBroker);
				clients.add(client);

				System.out.println("Added client #" + clients.size());
			}
			gameBroker.unsubscribe(removeClient);
			// Start new game with the given clients, their shared broker and the serverBroker
			Game newGame = new Game(clients, gameBroker, broker);
			games.add(newGame);
		}
	}
}
