package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A thread to read server socket input.
 */
public class GameServerThread extends Thread {

	int id;
	BufferedReader input;

	Pattern p = Pattern.compile("[0-9]+");

	/**
	 * Constructor for a server thread.
	 * 
	 * @param id
	 *            - the ID number of the thread
	 * @param reader
	 *            - input to process
	 */
	public GameServerThread(int id, BufferedReader reader) {
		this.input = reader;
		this.id = id;
	}

	/**
	 * Start the server.
	 */
	@Override
	public synchronized void start() {
		// Get the client message
		String inputLine = "";
		try {
			while (inputLine != "quit") {
				inputLine = this.input.readLine();
				if (inputLine != null) {
					if (inputLine.contains("set ID")) {
						Matcher m = this.p.matcher(inputLine);
						m.find();
						String ID = m.group();
						System.out.println("Player " + ID + " entered session.");
						this.id = Integer.parseInt(ID);
					} else if (inputLine.equals("end")) {
						System.out.println("Player " + this.id + " left session.");
					} else {
						System.err.println("Unknown command: " + inputLine);
					}
				}
			}
		} catch (IOException e) {
			// do nothing
		}

	}

	/**
	 * Run the server
	 */
	@Override
	public void run() {
		start();
	}
}
