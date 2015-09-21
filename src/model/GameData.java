package model;

import java.io.Serializable;
import java.util.HashMap;

import model.Configuration.PLAYER_SPEED;
import tiles.Tile;
import utilities.RandomNumUtils;

/**
 * Holds data related to gameplay logic control
 */
public class GameData implements Serializable {

	// ======================= Serialization ================================//

	private static final long serialVersionUID = 4753670767642154788L;

	//
	// Converting to use game controller to convert to MVC design pattern
	//
	// ==================== Game Logic Control ==============================//

	/**
	 * Player animation value - is the player walking?
	 */
	public boolean isPlayerWalking = false;
	/**
	 * Can the player move
	 */
	public boolean movable = false;
	/**
	 * Current message of the game
	 */
	public String currentMessage;
	/**
	 * Current message stage
	 */
	public int messageStage;

	/**
	 * An enumeration representing what screen the game is currently displaying
	 */
	public enum SCREEN {
		/**
		 * The main world scene
		 */
		WORLD,
		/**
		 * The main menu scene
		 */
		MENU,
		/**
		 * The introduction scene/animation
		 */
		INTRO,
		/**
		 * Creating a name scene
		 */
		NAME,
		/**
		 * The opening credits scene
		 */
		TITLE,
		/**
		 * The scene with option to load a game or create a new game
		 */
		CONTINUE,
		/**
		 * Scene to display a message (TODO - remove)
		 */
		MESSAGE,
		/**
		 * Main Scene in a battle
		 */
		BATTLE,
		/**
		 * Displays available moves
		 */
		BATTLE_FIGHT,
		/**
		 * Victory message at the end of a battle (TODO - replace with message
		 * queue)
		 */
		BATTLE_MESSAGE,
		/**
		 * Selecting an item in battle
		 */
		BATTLE_ITEM,
		/**
		 * Switching party members in battle (TODO rename)
		 */
		BATTLE_POKEMON,
		/**
		 * Information on encountered characters (TODO rename)
		 */
		POKEDEX,
		/**
		 * Current party (TODO rename)
		 */
		POKEMON,
		/**
		 * View available items
		 */
		BAG,
		/**
		 * Information display (helper) (TODO rename)
		 */
		POKEGEAR,
		/**
		 * Player & session information (TODO rename)
		 */
		TRAINERCARD,
		/**
		 * Save scene
		 */
		SAVE,
		/**
		 * Game options scene
		 */
		OPTION,
		/**
		 * Scene in conversation with an NPC
		 */
		CONVERSATION;
	}

	/**
	 * The current screen, defaults to main credits
	 */
	public SCREEN screen = SCREEN.TITLE; // start at the title screen
	/**
	 * The stage of the introduction
	 */
	public int introStage = 1;

	// ============================ Game Data =============================//

	/**
	 * This session's 'unique' ID
	 */
	public int gameSessionID = RandomNumUtils.createTrainerID();

	// ======================== Map Data ===================================//

	/**
	 * The width of the map
	 */
	public int map_width;
	/**
	 * The height of the map
	 */
	public int map_height;

	// ====================== Graphics control variables ===================//
	/**
	 * Painting variable, map offset in the x direction
	 */
	public int offsetX = 0;
	/**
	 * Painting variable, map offset in the y direction
	 */
	public int offsetY = 0;

	/**
	 * Teleportation graphics x variable
	 */
	public int start_coorX;
	/**
	 * Teleportation graphics y variable
	 */
	public int start_coorY; // teleportation graphics variables

	/**
	 * Stored as a map so each SCREEN can store it's own current selection
	 */
	public HashMap<SCREEN, Integer> currentSelection = new HashMap<SCREEN, Integer>();

	/**
	 * Representation of the map images
	 */
	public GameMap<Integer> imageMap = new GameMap<Integer>();
	/**
	 * Representation of the map tiles (characteristics such as obstacle or not)
	 */
	public GameMap<Tile> tileMap = new GameMap<Tile>();

	/**
	 * Is the game currently in a battle scene?
	 */
	public boolean inBattle = false;
	/**
	 * Did the player win the battle yet?
	 */
	public boolean playerWin = false;

	// ==================== Setting options variables ======================//

	/**
	 * Master volume control
	 */
	public boolean option_sound = false;

	// ======================== User Data ==================================//

	/**
	 * The player's current speed
	 */
	public PLAYER_SPEED currentSpeed = PLAYER_SPEED.WALK;

	/**
	 * Create a string describing the current game state
	 * 
	 * @return string representing game state
	 */
	public String toString() {
		String retStr = "Sound on: " + option_sound + "\n";
		retStr += "* Game ID: " + gameSessionID + "\n";
		retStr += "* Current msg: " + currentMessage + "\n";
		retStr += Configuration.getConfig() + "\n";
		retStr += "* Current speed: " + this.currentSpeed;
		return retStr;
	}
}
