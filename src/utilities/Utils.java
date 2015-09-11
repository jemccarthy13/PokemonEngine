package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import trainers.Player;
import driver.GameData;
import driver.GamePanel;

// ////////////////////////////////////////////////////////////////////////
//
// Utils - some utility functions used during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class Utils {

	// ////////////////////////////////////////////////////////////////////////
	//
	// saveGame - writes a game data object to a .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void saveGame(GamePanel data) {
		data.game.saveTime();
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			File dir = new File("resources/data");
			DebugUtility.printMessage(dir.isDirectory() + "");
			DebugUtility.printMessage(data.game.getPlayer().tData.toString());
			fout = new FileOutputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(data);
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			DebugUtility.printMessage("Unable to save game...");
			return;
		}
		DebugUtility.printMessage("** Saved game.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadGame - loads game object from a default .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static GameData loadGame() {
		GameData data = null;
		FileInputStream fout = null;
		ObjectInputStream oos = null;
		try {
			fout = new FileInputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectInputStream(fout);
			data = (GameData) oos.readObject();
			oos.close();
			fout.close();
			DebugUtility.printMessage("** Loaded game from save.");
		} catch (Exception e1) {
			e1.printStackTrace();
			DebugUtility.printMessage("Unable to load game...");
		}
		return data;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// accessComputer
	//
	// TODO - move to HandleEvent and graphics Painter
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void accessComputer(GamePanel data, Player you) {
		DebugUtility.printMessage("Saving game...");
		saveGame(data);
		DebugUtility.printMessage("COMPUTER ACCESS GRANTED");
		DebugUtility.printMessage("computer menu here.");
	}

}