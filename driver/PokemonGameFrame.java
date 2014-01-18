package driver;

import javax.swing.JFrame;

import utilities.EnumsAndConstants;
import utilities.Utils;

public class PokemonGameFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public PokemonGameFrame() {
		setTitle("Pokemon: Metallic Silver");
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

	public static void main(String[] args) {
		PokemonGameFrame pf = new PokemonGameFrame();
		pf.setVisible(true);
	}
}
