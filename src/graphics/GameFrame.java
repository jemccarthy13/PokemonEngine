package graphics;

import javax.swing.JFrame;

import utilities.DebugUtility;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 8002391898226135401L;
	static GamePanel pokemonGame;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default initializer for game.
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameFrame() {
		// Title changes with each game release (along with version)
		setTitle("Pokemon: Metallic Silver");

		setIconImage(SpriteLibrary.getImage("Icon"));

		// Add the main game panel to the game
		pokemonGame = new GamePanel();
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		pokemonGame.game.startGameTimer(pokemonGame);

		pokemonGame.game.printData();
		add(pokemonGame);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// The absolute main starting point for the Pokemon game.
	// Creates the JFrame that houses all game logic.
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		GameFrame pf = new GameFrame();
		pf.setVisible(true);
		pf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pf.setResizable(false);
		pf.pack();
		pf.setLocationRelativeTo(null);

		DebugUtility.printHeader("Startup completed");
	}
}
