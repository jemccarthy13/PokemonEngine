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
	private Socket socket;
	private PrintWriter printWriter;
	private int sessionID = -1;

	private static GameClient instance = new GameClient();

	public static GameClient getInstance() {
		return instance;
	}

	private GameClient() {}

	public void setPlayerID(int playerID) {
		this.sessionID = playerID;
	}

	/**
	 * Main entry point for the client.
	 * 
	 * @param args
	 */
	@Override
	public void run() {
		int portNum = 8085;
		try {
			this.socket = new Socket("localhost", portNum);
			this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
			this.printWriter.println("set ID " + this.sessionID);
			DebugUtility.printMessage("Connection established: port (" + portNum + ")");
		} catch (ConnectException e) {
			DebugUtility.printError(
					"Connection refused: port (" + portNum + ") in use or no response.\n" + e.getLocalizedMessage());
		} catch (IOException e1) {
			DebugUtility.printError("Unknown error: " + e1.getMessage());
		}
	}

	/**
	 * Send a message to the game server.
	 * 
	 * @param string
	 *            - the message to send
	 */
	public void sendMessage(String string) {
		try {
			this.printWriter.println(string);
		} catch (NullPointerException e) {
			DebugUtility.printError("Server/client connection wasn't established.");
			DebugUtility.printError("Exiting anyway.\n " + e.getLocalizedMessage());
		}
	}

	/**
	 * Setup and start the multiplayer session
	 * 
	 * @param id
	 *            - the id of the player
	 */
	public void establishMultiplayerSession(int id) {
		DebugUtility.printMessage("Connecting to game server...");
		this.setPlayerID(id);
		this.start();
	}

	/**
	 * Disconnect from the multiplayer session
	 */
	public void endMuliplayerSession() {
		this.sendMessage("end");
		DebugUtility.printMessage("Disconnected from game server.");
	}
}
