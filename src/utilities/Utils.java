package utilities;

import graphics.MessageBox;

import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import trainers.Player;
import driver.GameData;

// ////////////////////////////////////////////////////////////////////////
//
// Utils - some utility functions used during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class Utils {

	static MessageBox msg_box = new MessageBox();

	// ////////////////////////////////////////////////////////////////////////
	//
	// saveGame - writes a game data object to a .SAV file
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void saveGame(GameData data) {
		data.gameTimeStruct.saveTime();
		FileOutputStream fout = null;
		ObjectOutputStream oos = null;
		try {
			File dir = new File("resources/data");
			System.out.println(dir.isDirectory());
			System.out.println(data.player.tData.toString());
			fout = new FileOutputStream("resources/data/PokemonOrange.sav");
			oos = new ObjectOutputStream(fout);
			oos.writeObject(data);
			oos.close();
			fout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Unable to save game...");
			return;
		}
		System.out.println("** Saved game.");
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
			System.out.println("** Loaded game from save.");
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("Unable to load game...");
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
	public static void accessComputer(GameData data, Player you) {
		System.out.println("Saving game...");
		saveGame(data);
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