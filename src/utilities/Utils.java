package utilities;

import java.awt.Graphics;
import java.io.File;
import java.io.FileOutputStream;
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
	// saveGame - writes a player object to a .SAV file
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