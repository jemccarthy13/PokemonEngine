package model;

import java.io.Serializable;
import java.util.HashMap;

import model.Configuration.PLAYER_SPEED;
import tiles.Tile;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// GameData - holds data related to gameplay logic control
//
// ////////////////////////////////////////////////////////////////////////
public class GameData implements Serializable {

	// ======================= Serialization ================================//

	private static final long serialVersionUID = 4753670767642154788L;

	//
	// Trying something new and crazy
	//
	// ==================== Game Logic Control ==============================//

	public boolean isPlayerWalking = false; // player animation counter
	public boolean movable = false;
	public String currentMessage;
	public int messageStage;

	public enum SCREEN {
		WORLD, MENU, INTRO, NAME, TITLE, CONTINUE, MESSAGE, BATTLE, BATTLE_FIGHT, BATTLE_MESSAGE, POKEDEX, POKEMON, BAG, POKEGEAR, TRAINERCARD, SAVE, OPTION, CONVERSATION;
	}

	public SCREEN screen = SCREEN.TITLE; // start at the title screen
	public int introStage = 1;

	// ============================ Game Data =============================//

	public int gameSessionID = RandomNumUtils.createTrainerID();

	// ======================== Map Data ===================================//

	public int map_width;
	public int map_height;

	// ====================== Graphics control variables ===================//
	public int offsetX = 0, offsetY = 0; // painting variables
	public int start_coorX, start_coorY; // teleportation graphics variables

	//
	// stored as a map so each SCREEN can store it's own current selection
	//
	public HashMap<SCREEN, Integer> currentSelection = new HashMap<SCREEN, Integer>();

	//
	// one map to store tiles, one map to store images
	//
	public GameMap<Integer> imageMap = new GameMap<Integer>();
	public GameMap<Tile> tileMap = new GameMap<Tile>();

	// ======================= Battle information ==========================//
	// ////////////////////////
	public boolean inBattle = false;
	public boolean playerWin = false;

	// ==================== Setting options variables ======================//

	public boolean option_sound = false;

	// ======================== User Data ==================================//

	public PLAYER_SPEED currentSpeed = PLAYER_SPEED.WALK;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Print the game information
	//
	// ////////////////////////////////////////////////////////////////////////
	public String toString() {
		String retStr = "Sound on: " + option_sound + "\n";
		retStr += "* Game ID: " + gameSessionID + "\n";
		retStr += "* Current msg: " + currentMessage + "\n";
		retStr += Configuration.getConfig() + "\n";
		retStr += "* Current speed: " + this.currentSpeed;
		return retStr;
	}
}
