package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import utilities.DebugUtility;

/**
 * A client for the game server.
 */
public class GameClient extends Thread {
	private static Socket socket;
	private static PrintWriter printWriter;
	private static int sessionID = -1;

	/**
	 * Constructor given a session ID
	 * 
	 * @param ID
	 *            - the Player ID
	 */
	public GameClient(int ID) {
		sessionID = ID;
	}

	/**
	 * Main entry point for the client.
	 * 
	 * @param args
	 */
	public void run() {
		int portNum = 8085;
		try {
			socket = new Socket("localhost", portNum);
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("set ID " + sessionID);
		} catch (ConnectException e) {
			DebugUtility.error("Connection refused: port (" + portNum + ") likely in use.");
		} catch (IOException e1) {
			DebugUtility.error("Unknown error: " + e1.getMessage());
		}
	}

	/**
	 * Send a message to the game server.
	 * 
	 * @param string
	 *            - the message to send
	 */
	public void sendMessage(String string) {
		printWriter.println(string);
	}
}
