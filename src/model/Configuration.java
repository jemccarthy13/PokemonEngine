package model;

import java.io.Serializable;
import java.util.HashMap;

import model.GameData.SCREEN;

public class Configuration implements Serializable {
	private static final long serialVersionUID = 3800907360431668204L;

	// ==========Configuration information and game settings=================//
	public static boolean NOCLIP = false; // walk anywhere
	public static boolean DOBATTLES = true; // no wild/trainer battles
	public static boolean SHOWINTRO = false; // false = skip Oak intro

	public static final int NPC_SIGHT_DISTANCE = 5; // NPC line of sight calc
	public static final int MAX_NAME_SIZE = 7; // max name size for a character

	public static final String VERSION = "Metallic Silver";
	public static final String MAP_TO_LOAD = "/maps/NewBarkTown.map";
	public static final String MUSIC_PATH = "resources/audio_lib/BGM/";
	public static final String SOUND_EFFECT_PATH = "resources/audio_lib/SE/";

	public enum PLAYER_SPEED {
		WALK(80), RUN(90), BIKE(95);

		private int value;

		PLAYER_SPEED(int v) {
			this.value = v;
		}

		public int getValue() {
			return value;
		}
	}

	public HashMap<SCREEN, Integer> numSelections = new HashMap<SCREEN, Integer>();

	public Configuration() {
		numSelections.put(SCREEN.CONTINUE, 3);
		numSelections.put(SCREEN.SAVE, 2);
		numSelections.put(SCREEN.OPTION, 6);
		numSelections.put(SCREEN.MENU, 8);
		numSelections.put(SCREEN.POKEGEAR, 4);
	}

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
