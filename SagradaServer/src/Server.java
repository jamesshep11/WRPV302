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
	// Server related fields.
	private ServerSocket server;
	private Socket connection;

	private Broker broker;

	private ArrayList<Game> games;

	public Server() {
		broker = new Broker();
		broker.subscribe("EndGame", ((publisher, topic, params) -> endGame((Game)publisher)));

		games = new ArrayList<>();
	}

	public void run() {
		try {
			// Create server socket.
			server = new ServerSocket(500, 100);
			System.out.println("Started server: " + InetAddress.getLocalHost().getHostAddress());

			// Listen for clients and start new games
			while (true) {
				try {
					// Initialize new client list and broker for a new game
					ArrayList<Client> clients = new ArrayList<>();
					Broker gameBroker = new Broker();
					// In case a client leaves before the game starts
					Subscriber removeClient = (publisher, topic, params) -> clients.remove(publisher);
					gameBroker.subscribe("CloseConnection", removeClient);

					// Wait for 4 clients to join
					//while (clients.size() < 4) {
						waitForConnection(clients.size()+1);

						//Create client using global values from waitingForConn() and getStreams()
						Client client = new Client(connection, gameBroker);
						clients.add(client);

						System.out.println("Added client #" + clients.size());
					//}
					gameBroker.unsubscribe(removeClient);
					// Start new game with the given clients, their shared broker and the serverBroker
					Game newGame = new Game(clients, gameBroker, broker);
					games.add(newGame);
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
	}

	private void endGame(Game game){
		games.remove(game);
	}
}
