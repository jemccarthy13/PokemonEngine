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
	public boolean DOBATTLES = true; // no wild/trainer battles
	public boolean SHOWINTRO = false; // false = skip Oak intro

	//
	// Trying something new and crazy
	//
	// ==================== Game Logic Control ==============================//

	public boolean isPlayerWalking = false; // player animation counter
	public boolean movable = false;

	// ==================== Game Data Control ==============================//

	public Player player;

	// Timing Data
	public TimeStruct gameTimeStruct = new TimeStruct();
	public Timer gameSpeed; // controls the speed game events are handled

	// ======================== Map Data ===================================//
	public int map_width;
	public int map_height;

	// ////////////////////////
	public int[][] imageMap = new int[3][21400];
	public TileMap tileMap = new TileMap();
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

	public int offsetX = 0, offsetY = 0; // painting variables
	public int start_coorX, start_coorY; // teleportation graphics variables
	public int menuSelection = 0;

	// ==================== Setting options variables ======================//
	public boolean option_sound = false;
	// ======================== User Data ==================================//

	public int currentSpeed = Configuration.PLAYER_SPEED_WALK;

	public int id = RandomNumUtils.createTrainerID();

	public String toString() {
		String retStr = "Player: " + "\n";
		retStr += player.tData.toString();
		retStr += player.toString();
		return retStr;
	}
}
