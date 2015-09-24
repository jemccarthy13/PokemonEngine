package model;

import java.io.Serializable;

/**
 * Stores the configuration of the game
 */
// - Cheats
// - Default sizes / distances
// - Version information
// - Map to use
// - Location of sound/music files
// - Speed of the game
public class Configuration implements Serializable {
	private static final long serialVersionUID = 3800907360431668204L;

	// ==========Configuration information and game settings=================//
	/**
	 * Walk anywhere
	 */
	public static boolean NOCLIP = false;

	/**
	 * Do wild / opponent battles
	 */
	public static boolean DOBATTLES = true;
	/**
	 * False = skip Oak intro, True = do Oak intro
	 */
	public static boolean SHOWINTRO = false;

	/**
	 * NPC will see player <= this number of tiles away
	 */
	public static final int NPC_SIGHT_DISTANCE = 5;

	/**
	 * Max name size for a character or battler
	 */
	public static final int MAX_NAME_SIZE = 7;

	/**
	 * Version information
	 */
	public static final String VERSION = "Metallic Silver";
	/**
	 * Where to find this game's map
	 */
	public static final String MAP_TO_LOAD = "/maps/NewBarkTown.map";
	/**
	 * Where to find this game's music
	 */
	public static final String MUSIC_PATH = "resources/audio_lib/BGM/";
	/**
	 * Where to find this game's sound effects
	 */
	public static final String SOUND_EFFECT_PATH = "resources/audio_lib/SE/";

	/**
	 * Player's speed in game
	 */
	public enum PLAYER_SPEED {
		/**
		 * Player speed walking
		 */
		WALK(80),
		/**
		 * Player speed running
		 */
		RUN(90),
		/**
		 * Player speed on a bike
		 */
		BIKE(95);

		private int value;

		PLAYER_SPEED(int v) {
			this.value = v;
		}

		/**
		 * Get the value of the enumeration
		 * 
		 * @return int representing game speed
		 */
		public int getValue() {
			return value;
		}
	}

	/**
	 * Default constructor initializes the number of selections at each screen
	 */
	public Configuration() {}

	/**
	 * Convert game information to a string
	 * 
	 * @return String representing game configuration
	 */
	public static String getConfig() {
		String retStr = "* No clip: " + NOCLIP + "\n";
		retStr += "* Do battles: " + DOBATTLES + "\n";
		retStr += "* Show intro: " + SHOWINTRO + "\n"; // false = skip Oak intro

		retStr += "* Max name size: " + MAX_NAME_SIZE + "\n";
		retStr += "* Version: " + VERSION + "\n";
		retStr += "* Player speed walk: " + PLAYER_SPEED.WALK.getValue() + "\n";
		retStr += "* Player speed run: " + PLAYER_SPEED.RUN.getValue() + "\n";
		retStr += "* Player speed bike: " + PLAYER_SPEED.BIKE.getValue() + "\n";

		retStr += "* Map to load: " + MAP_TO_LOAD + "\n";
		retStr += "* NPC sight distance: " + NPC_SIGHT_DISTANCE;

		return retStr;
	}
}
