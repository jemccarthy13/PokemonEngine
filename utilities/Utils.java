package utilities;

import java.awt.Graphics;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import trainers.Player;

// ////////////////////////////////////////////////////////////////////////
//
// Utils - some utility functions used during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class Utils {

	// ////////////////////////////////////////////////////////////////////////
	//
	// saveGame - writes a player object to a .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void saveGame(Player you) {
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			fout = new FileOutputStream("Data/PokemonOrange.SAV");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(you);
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Unable to save game...");
			return;
		}
		System.out.println("Game saved!");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessComputer
	//
	// TODO - move to HandleEvent and graphics Painter
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void accessComputer(Player you) {
		System.out.println("Saving game...");
		saveGame(you);
		System.out.println("COMPUTER ACCESS GRANTED");
		System.out.println("computer menu here.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// methods for conversation dialog boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void messageBox(Graphics g, String string) {
		EnumsAndConstants.msg_box.Message(g, string);
	}

	public static void messageBox(Graphics g, String string, String string2) {
		EnumsAndConstants.msg_box.Message(g, string, string2);
	}
}