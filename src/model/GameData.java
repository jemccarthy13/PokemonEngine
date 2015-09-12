package model;

import java.io.Serializable;
import java.util.HashMap;

import javax.swing.Timer;

import party.Party;
import tiles.TileMap;
import trainers.Actor;
import trainers.Player;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// GameData - holds data related to gameplay logic control
//
// ////////////////////////////////////////////////////////////////////////
public class GameData implements Serializable {

	// ======================= Serialization ================================//
	private static final long serialVersionUID = 4753670767642154788L;

	// =========================== CHEATS ===================================//
	public boolean NOCLIP = false; // walk anywhere
	public boolean DOBATTLES = true; // no wild/trainer battles
	public boolean SHOWINTRO = false; // false = skip Oak intro

	//
	// Trying something new and crazy
	//
	// ==================== Game Logic Control ==============================//

	public boolean isPlayerWalking = false; // player animation counter
	public boolean movable = false;

	// ==================== Game Data Control ==============================//

	public Actor currentNPC;
	public Player player;
	public String currentMessage;
	public int messageStage;
	public int gameSessionID = RandomNumUtils.createTrainerID();
	public Party currentEnemy = new Party();

	// Timing Data
	public TimeStruct gameTimeStruct = new TimeStruct();
	public Timer gameSpeed; // controls the speed game events are handled

	// ======================== Map Data ===================================//
	public int map_width;
	public int map_height;

	// ====================== Graphics control variables ===================//
	public enum SCREEN {
		WORLD, MENU, INTRO, NAME, TITLE, CONTINUE, MESSAGE, BATTLE, BATTLE_FIGHT, BATTLE_MESSAGE, POKEDEX, POKEMON, BAG, POKEGEAR, TRAINERCARD, SAVE, OPTION, CONVERSATION;
	}

	public SCREEN screen = SCREEN.TITLE; // start at the title screen
	public int introStage = 1;
	public int offsetX = 0, offsetY = 0; // painting variables
	public int start_coorX, start_coorY; // teleportation graphics variables

	// stored as a map so each SCREEN can store it's own current selection
	public HashMap<SCREEN, Integer> currentSelection = new HashMap<SCREEN, Integer>();

	public int[][] imageMap = new int[3][21400];
	public TileMap tileMap = new TileMap();

	// ======================= Battle information ==========================//
	// ////////////////////////
	public boolean inBattle = false;
	public boolean playerWin = false;

	// ==================== Setting options variables ======================//

	public boolean option_sound = false;

	// ======================== User Data ==================================//

	public int currentSpeed = Configuration.PLAYER_SPEED_WALK;

	public String toString() {
		String retStr = "Player: " + "\n";
		retStr += player.tData.toString();
		retStr += player.toString();
		return retStr;
	}
}
