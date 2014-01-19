package driver;

import javax.swing.JFrame;

import utilities.EnumsAndConstants;
import utilities.Utils;

public class PokemonGameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default initializer for game.
	//
	// ////////////////////////////////////////////////////////////////////////
	public PokemonGameFrame() {
		// Title changes with each game release (along with version)
		setTitle("Pokemon: " + EnumsAndConstants.VERSION);

		// No matter the version, the Icon is always located in the same place
		// and these attributes of the game do not change.
		setIconImage(EnumsAndConstants.sprite_lib.ICON);
		Main pokemon = new Main();
		pokemon.setFocusable(true);
		pokemon.requestFocus();
		add(pokemon);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		Utils.playBackgroundMusic(EnumsAndConstants.MUSIC.TITLE);
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
