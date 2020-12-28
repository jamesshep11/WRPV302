import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public Server() {
		runServer();
	}

	public void runServer() {
		ServerSocket server;
		Socket connection;
		DataOutputStream dos;
		DataInputStream dis;
		int counter = 1;

		try {
			// Step 1: Create a ServerSocket.
			server = new ServerSocket(500, 100);
			System.out.println("SERVER: Server started "
					+ InetAddress.getLocalHost().getHostAddress()
			);


			while (true) {

				// Step 2: Wait for a connection.
				connection = server.accept();
				System.out.println("SERVER: Connection " + counter
						+ " received from: "
						+ connection.getInetAddress().getHostName());

				// Step 3: Get dis and dos streams.
				dis = new DataInputStream(connection.getInputStream());
				dos = new DataOutputStream(connection.getOutputStream());
				System.out.println("SERVER: Got I/O streams");

				// Step 4: Process connection.
				System.out
						.printf("SERVER: Sending message \"Connection successful #%d\"\n", counter);
				dos.writeUTF("Connection successful #" + counter);
				System.out
						.println("SERVER: Client message: " + dis.readUTF());

				// Step 5: Close connection.
				System.out.println("SERVER: Transmission complete. "
						+ "Closing socket.");
				connection.close();
				++counter;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		new Server();
	}
}
