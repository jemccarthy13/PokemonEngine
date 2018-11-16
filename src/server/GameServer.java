package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import utilities.DebugUtility;
import utilities.DebugUtility.DEBUG_LEVEL;

/**
 * A server used to communicate in multiple player games.
 */
public class GameServer {

	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static BufferedReader bufferedReader;
	private static int connections = 0;

	/**
	 * Main entry point for the server
	 * 
	 * @param args
	 *            - command line arguments
	 */
	public static void main(String[] args) {
		DebugUtility.setLevel(DEBUG_LEVEL.DEBUG);
		// Wait for client to connect on 8085
		int portNum = 8085;
		try {
			serverSocket = new ServerSocket(portNum);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				// Create a reader
				bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				new Thread(new GameServerThread(connections, bufferedReader)).start();
				connections++;
			} catch (BindException e1) {
				DebugUtility.error(portNum + " already in use.\n" + e1.getLocalizedMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
