package graphics;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import utilities.DebugUtility;
import controller.GameController;

/**
 * The main UI frame for the Game Engine. Creates and packs a game panel which
 * houses most of the logic control. This class is more responsible for the
 * "look and feel"
 */
public class GameFrame extends JFrame {

	private static final long serialVersionUID = 8002391898226135401L;
	static GamePanel pokemonGame;

	static WindowListener frameListener = new WindowListener() {
		/**
		 * WindowListener required
		 */
		@Override
		public void windowActivated(WindowEvent arg0) {}

		/**
         * 
         */
		@Override
		public void windowClosed(WindowEvent arg0) {
			pokemonGame.gameController.endMultiplayerSession();
			DebugUtility.printMessage("Game session ended.");
			System.exit(0);
		}

		/**
		 * WindowListener required
		 */
		@Override
		public void windowClosing(WindowEvent arg0) {}

		/**
		 * WindowListener required
		 */
		@Override
		public void windowDeactivated(WindowEvent arg0) {}

		/**
		 * WindowListener required
		 */
		@Override
		public void windowDeiconified(WindowEvent arg0) {}

		/**
		 * WindowListener required
		 */
		@Override
		public void windowIconified(WindowEvent arg0) {}

		/**
		 * WindowListener required
		 */
		@Override
		public void windowOpened(WindowEvent arg0) {}
	};

	/**
	 * Default constructor for a GameFrame
	 */
	public GameFrame() {
		// Title changes with each game release (along with version)
		setTitle("Pokemon: Metallic Silver");

		setIconImage(SpriteLibrary.getImage("Icon"));

		// Add the main game panel to the game
		pokemonGame = new GamePanel();
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		pokemonGame.gameController.startGameTimer(pokemonGame);

		pokemonGame.gameController.printData();
		add(pokemonGame);
	}

	/**
	 * Allows access to the game controller (for testing validation purposes)
	 * 
	 * @return the current game controller
	 */
	public GameController getController() {
		return pokemonGame.gameController;
	}

	/**
	 * The absolute main starting point for the Pokemon game. Creates the JFrame
	 * that houses all game logic.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {
		GameFrame pf = new GameFrame();
		pf.setVisible(true);
		pf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pf.setResizable(false);
		pf.pack();
		pf.setLocationRelativeTo(null);
		pf.addWindowListener(frameListener);

		DebugUtility.printHeader("Startup completed");
	}
}
