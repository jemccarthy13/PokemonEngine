package utilities;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import driver.Game;
import graphics.MessageBox;
import trainers.Player;

// ////////////////////////////////////////////////////////////////////////
//
// Utils - some utility functions used during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class Utils {

	static MessageBox msg_box = new MessageBox();

	// ////////////////////////////////////////////////////////////////////////
	//
	// saveGame - writes a game object to a .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void saveGame(Game game) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			File dir = new File("resources/data");
			System.out.println(dir.isDirectory());
			fout = new FileOutputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(game);
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Unable to save game...");
			return;
		}
		System.out.println("Game saved to .SAV!");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadGame - loads game object from a default .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static Game loadGame() {
		Game game = null;
		FileInputStream fout = null;
		ObjectInputStream oos = null;
		try {
			fout = new FileInputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectInputStream(fout);

			game = (Game) oos.readObject();

			System.out.println(game.gData.toString());
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Unable to load game...");
			return game;
		}
		System.out.println("Game loaded from .SAV!");
		return game;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessComputer
	//
	// TODO - move to HandleEvent and graphics Painter
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void accessComputer(Game game, Player you) {
		System.out.println("Saving game...");
		saveGame(game);
		System.out.println("COMPUTER ACCESS GRANTED");
		System.out.println("computer menu here.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// methods for conversation dialog boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void messageBox(Graphics g, String string, String string2) {
		msg_box.Message(g, string, string2);
	}
}