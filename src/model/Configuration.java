package model;

import java.io.Serializable;
import java.util.HashMap;

import model.GameData.SCREEN;

public class Configuration implements Serializable {
	private static final long serialVersionUID = 3800907360431668204L;

	// ==========Configuration information and game settings=================//

	public static final int MAX_NAME_SIZE = 7;
	public static final String VERSION = "Metallic Silver";
	public static final int PLAYER_SPEED_WALK = 80;
	public static final int PLAYER_SPEED_RUN = 90;
	public static final int PLAYER_SPEED_BIKE = 95;

	public final String MAP_TO_LOAD = "/maps/NewBarkTown.map";
	public final int NPC_SIGHT_DISTANCE = 5;

	public HashMap<SCREEN, Integer> numSelections = new HashMap<SCREEN, Integer>();

	public Configuration() {
		numSelections.put(SCREEN.CONTINUE, 3);
		numSelections.put(SCREEN.SAVE, 2);
		numSelections.put(SCREEN.OPTION, 6);
		numSelections.put(SCREEN.MENU, 8);
		numSelections.put(SCREEN.POKEGEAR, 4);
	}
}
