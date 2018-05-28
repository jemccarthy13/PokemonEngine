package model;

import java.io.Serializable;

import model.Configuration.PLAYER_SPEED;
import utilities.RandomNumUtils;

/**
 * Holds data related to gameplay logic control
 */
public class GameData implements Serializable {

	// ======================= Serialization ================================//

	private static final long serialVersionUID = 4753670767642154788L;

	private static GameData instance = new GameData();

	private GameData() {}

	public static GameData getInstance() {
		return instance;
	}

	//
	// Converting to use game controller to convert to MVC design pattern
	//
	// ==================== Game Logic Control ==============================//

	/**
	 * The stage of the introduction
	 */
	public int introStage = 1;

	// ============================ Game Data =============================//

	/**
	 * This session's 'unique' ID
	 */
	public int gameSessionID = RandomNumUtils.createTrainerID();

	/**
	 * Is the game currently in a battle scene?
	 */
	public boolean inBattle = false;

	/**
	 * Did the player win the battle yet?
	 */
	public boolean playerWin = false;

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
		String retStr = "Sound on: " + Configuration.getInstance().isSoundOn() + "\n";
		retStr += "* Game ID: " + gameSessionID + "\n";
		retStr += Configuration.getInstance().getConfig() + "\n";
		retStr += "* Current speed: " + currentSpeed + "\n";
		return retStr;
	}

	public static void setInstance(GameData readObject) {
		instance = readObject;
	}
}
