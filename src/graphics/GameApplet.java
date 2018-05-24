package graphics;

import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JApplet;

import controller.GameKeyListener;
import utilities.DebugUtility;

/**
 * An applet wrapper for the game
 */
public class GameApplet extends JApplet {

	private static final long serialVersionUID = 2065351265327520213L;

	static GamePanel pokemonGame = null;

	public void init() {
		DebugUtility.printMessage("In init...");
		DebugUtility.printMessage("Creating game panel...");
		pokemonGame = new GamePanel();
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		pokemonGame.gameController.startGameTimer(pokemonGame);

		pokemonGame.gameController.printData();
		try {
			pokemonGame.gameController.loadMap();
		} catch (IOException | InterruptedException e) {
			DebugUtility.printError("Unable to load map!");
		}
		// setup key press listening
		GameKeyListener.getInstance().setGameController(pokemonGame.gameController);
		this.addKeyListener(GameKeyListener.getInstance());

		DebugUtility.printHeader("Event Registration");
		DebugUtility.printMessage("Added event handler.");
		DebugUtility.printMessage("Registered for events.");

		DebugUtility.printMessage("Playing title music...");
		pokemonGame.gameController.playBackgroundMusic("Title");
		add(pokemonGame);
	}

	public void paintComponent(Graphics g) {
		g.drawString("painting...", 20, 30);
		pokemonGame.paintComponent(g);
	}

	public static void main(String[] args) {}
}
