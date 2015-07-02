package driver;

import java.io.Serializable;

import javax.swing.Timer;

import tiles.TileMap;
import trainers.Player;
import utilities.RandomNumUtils;
import utilities.TimeStruct;

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
	public boolean NOBATTLE = false; // no wild/trainer battles
	public static final int MAX_NAME_SIZE = 7;
	public static final int NPC_SIGHT_DISTANCE = 5;
	public static final String VERSION = "Metallic Silver";

	public static final int PLAYER_SPEED_WALK = 80;
	public static final int PLAYER_SPEED_RUN = 90;
	public static final int PLAYER_SPEED_BIKE = 95;
	// ==================== Game Timing Data ================================//
	public TimeStruct gameTimeStruct = new TimeStruct();
	public long timeStarted; // time the game was started
	public Timer gameTimer; // time difference between game events
	// ======================== Map Data ===================================//
	public int[][] currentMap = new int[3][21400];
	public TileMap tm = new TileMap();
	public int map_width;
	public int map_height;
	// ======================= Battle information ==========================//
	public boolean inBattle = false;
	public boolean playerWin = false;
	// ====================== Graphics control variables ===================//
	public boolean inMenu = false;
	public boolean inIntro = false;
	public boolean inNameScreen = false;
	public boolean atTitle = true;
	public boolean atContinueScreen = false;
	public boolean inMessage;

	public int introStage = 1;

	public int offsetX = 0;
	public int offsetY = 0;
	public int start_coorX, start_coorY; // teleportation graphics variables
	public int menuSelection = 0;

	// ==================== Setting options variables ======================//
	public boolean option_sound = false;
	// ======================== User Data ==================================//
	public Player player;
	public int currentSpeed = PLAYER_SPEED_WALK; // controls the speed of play

	public int id = RandomNumUtils.createTrainerID();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Update the game time according to the time started
	//
	// ////////////////////////////////////////////////////////////////////////
	public void updateTime() {
		gameTimeStruct.updateTime(timeStarted);
	}

	public String toString() {
		String retStr = "Player: " + "\n";
		retStr += player.tData.toString();
		retStr += player.toString();
		return retStr;
	}
}
