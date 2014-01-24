package utilities;

import java.awt.Graphics;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.Random;

import location.Location;
import pokedex.Pokemon;
import trainers.Player;

// ////////////////////////////////////////////////////////////////////////
//
// Utils - some utility functions used during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class Utils {
	static Random randomGenerator = new Random();

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomDirection - generates a random direction for NPCs
	//
	// ////////////////////////////////////////////////////////////////////////
	public static EnumsAndConstants.DIR randomDirection() {
		int dir = generateRandom(1, 4);
		switch (dir) {
		case 1:
			return EnumsAndConstants.DIR.NORTH;
		case 2:
			return EnumsAndConstants.DIR.EAST;
		case 3:
			return EnumsAndConstants.DIR.SOUTH;
		case 4:
			return EnumsAndConstants.DIR.WEST;
		}
		return EnumsAndConstants.DIR.SOUTH;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// createTrainerID - creates a random Trainer ID for the player
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int createTrainerID() {
		int a = Utils.generateRandom(0, 9);
		int b = Utils.generateRandom(0, 9);
		int c = Utils.generateRandom(0, 9);
		int d = Utils.generateRandom(0, 9);
		int e = Utils.generateRandom(0, 9);
		return (10000 * a + 1000 * b + 100 * c + 10 * d + e);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatTime - formats the system time into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public static String formatTime(TimeStruct time) {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(time.getHours()) + ": " + df.format(time.getMinutes()) + ": " + df.format(time.getSeconds());
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// sound effect utility functions to play different sound effects
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void playCollisionSound() {
		EnumsAndConstants.col.playClip("COLLISION");
	}

	public static void playMenuSound() {
		EnumsAndConstants.col.playClip("MENU");
	}

	public static void playSelectSound() {
		EnumsAndConstants.col.playClip(EnumsAndConstants.SE_SELECT);
	}

	public static void playDamageSound() {
		EnumsAndConstants.col.playClip("DAMAGE");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// playBackgroundMusic - switches the background music to the current
	// location's music
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void playBackgroundMusic(String l) {
		switch (l) {
		case "New Bark Town": {
			playBackgroundMusic(EnumsAndConstants.MUSIC.NEWBARKTOWN);
			break;
		}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// pickTrainerMusic - when an enemy sees the player, pick a random
	// "I see you" music
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void pickTrainerMusic() {
		int choice = generateRandom(6, 1);
		switch (choice) {
		case 1: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER1);
			break;
		}
		case 2: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER2);
			break;
		}
		case 3: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER3);
			break;
		}
		case 4: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER4);
			break;
		}
		case 5: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER5);
			break;
		}
		case 6: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER6);
			break;
		}
		default: {
			playBackgroundMusic(EnumsAndConstants.MUSIC.TRAINER4);
			break;
		}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// playBackgroundMusic - plays different background music according to the
	// scenario
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void playBackgroundMusic(EnumsAndConstants.MUSIC song) {
		if (EnumsAndConstants.BACKGROUND_MUSIC != null) {
			EnumsAndConstants.BACKGROUND_MUSIC.stop();
			EnumsAndConstants.BACKGROUND_MUSIC = null;
		}
		if (song == EnumsAndConstants.MUSIC.NEWBARKTOWN) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.NEWBARKTOWN;
		} else if (song == EnumsAndConstants.MUSIC.TITLE) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TITLE;
		} else if (song == EnumsAndConstants.MUSIC.CONTINUE) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.CONTINUEBGM;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER_BATTLE) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER_BATTLE;
		} else if (song == EnumsAndConstants.MUSIC.INTRO) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.INTRO;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER1) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER1;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER2) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER2;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER3) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER3;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER4) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER4;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER5) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER5;
		} else if (song == EnumsAndConstants.MUSIC.TRAINER6) {
			EnumsAndConstants.BACKGROUND_MUSIC = EnumsAndConstants.audio_lib.TRAINER6;
		}
		if (EnumsAndConstants.BACKGROUND_MUSIC != null) {
			EnumsAndConstants.BACKGROUND_MUSIC.start();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// pauseBackgroundMusic - pauses the background music, typically to
	// interrupt the music thread with another song
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void pauseBackgrondMusic() {
		if (EnumsAndConstants.BACKGROUND_MUSIC != null) {
			EnumsAndConstants.BACKGROUND_MUSIC.stop();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// generateRandom - the heart of random number generation in game
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int generateRandom(int start, int end) {
		double range = end - start + 1.0;
		double fraction = (range * randomGenerator.nextDouble());
		int num = (int) (fraction + start);
		return num;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// for an enemy pokemon, pick a random move
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int getMove(Pokemon p) {
		return generateRandom(0, p.getNumMoves() - 1);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomLevel - generates a random level between start and end, inclusive
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomLevel(int start, int end) {
		return generateRandom(start + 1, end - 1);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomBaseStat - generates a random base stat based on the given level
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomBaseStat(int level) {
		int start = (int) (2.4D * level) - 5;
		int end = (int) (2.4D * level) + 5;
		return generateRandom(start, end);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomStatIncr - generates a number to incrase a stat by:
	// 75% chance of +2
	// 25% chance of +3
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomStatIncr() {
		double fraction = (100 * randomGenerator.nextDouble());
		if (fraction < 75) {
			return 2;
		}
		return 3;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomPokemon - given a location, generate a random pokemon from the
	// wild pokemon chart in that region
	//
	// ////////////////////////////////////////////////////////////////////////
	public static Pokemon randomPokemon(Location location) {
		Pokemon p = null;
		long name_number = generateRandom(0, 100);
		String name = location.getPokemon(name_number);
		int level_start = location.getMinLevel(name);
		int level_end = location.getMaxLevel(name);
		int level = randomLevel(level_start, level_end);
		p = EnumsAndConstants.pokemon_generator.createPokemon(name, level);
		return p;
	}

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