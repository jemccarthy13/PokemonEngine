package driver;

import javax.swing.JFrame;

import libraries.SpriteLibrary;
import audio.AudioLibrary;

public class PokemonGameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default initializer for game.
	//
	// ////////////////////////////////////////////////////////////////////////
	public PokemonGameFrame() {
		// Title changes with each game release (along with version)
		setTitle("Pokemon: Metallic Silver");

		// No matter the version, the Icon is always located in the same place
		// and these attributes of the game do not change.
		setIconImage(SpriteLibrary.getInstance().ICON);
		Game pokemonGame = new Game();
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		add(pokemonGame);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);

		AudioLibrary.getInstance().playBackgroundMusic("Title");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// The absolute main starting point for the Pokemon game.
	// Creates the JFrame that houses all game logic.
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		PokemonGameFrame pf = new PokemonGameFrame();
		pf.setVisible(true);
	}
}
