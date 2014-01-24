package utilities;

import java.io.Serializable;

import javax.swing.Timer;

import tiles.TileMap;
import trainers.Player;

// ////////////////////////////////////////////////////////////////////////
//
// GameData - holds data related to gameplay logic control
//
// ////////////////////////////////////////////////////////////////////////
public class GameData implements Serializable {
	// ======================= Serialization ================================//
	public static final long serialVersionUID = 1L;
	// =========================== CHEATS ===================================//
	public boolean noClip = false; // walk anywhere
	public boolean noBattle = false; // no wild/trainer battles
	// ==================== Game Timing Data ================================//
	// holds how long the game has been played
	public TimeStruct gameTimeStruct = new TimeStruct();
	public long timeStarted; // time the game was started
	public Timer gameTimer; // time difference between game events
	// ======================== Map Data ===================================//
	public int[][] currentMap = new int[3][16500];
	// public Tile[][] tileMap = new Tile[200][200];
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
	public int introStage = 1;

	public int offsetX = 0;
	public int offsetY = 0;
	public int start_coorX, start_coorY; // teleportation graphics variables
	public int menuSelection = 0;
	// ======================== User Data ==================================//
	public Player player;
	// ======================== Version ====================================//
	public String version = EnumsAndConstants.VERSION;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Update the game time according to the time started
	//
	// ////////////////////////////////////////////////////////////////////////
	public void updateTime() {
		gameTimeStruct.updateTime(timeStarted);
	}
}
