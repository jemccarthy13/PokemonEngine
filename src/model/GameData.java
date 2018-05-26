package model;

import java.io.Serializable;

import graphics.BaseScene;
import graphics.TitleScene;
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
	 * Current message of the game
	 */
	public String currentMessage;
	/**
	 * Current message stage
	 */
	public int messageStage;

	/**
	 * The current screen, defaults to main credits
	 */
	public BaseScene scene = TitleScene.instance; // start at the title screen
	/**
	 * The stage of the introduction
	 */
	public int introStage = 1;

	// ============================ Game Data =============================//

	/**
	 * This session's 'unique' ID
	 */
	public int gameSessionID = RandomNumUtils.createTrainerID();

	// ====================== Graphics control variables ===================//
	/**
	 * Painting variable, map offset in the x direction
	 */
	private int offsetX = 0;
	/**
	 * Painting variable, map offset in the y direction
	 */
	private int offsetY = 0;

	public int getOffsetX() {
		return this.offsetX;
	}

	public int getOffsetY() {
		return this.offsetY;
	}

	public void addOffsetY(int toAdd) {
		this.offsetY = this.offsetY + toAdd;
	}

	public void addOffsetX(int toAdd) {
		this.offsetX = this.offsetX + toAdd;
	}

	/**
	 * Teleportation graphics x variable
	 */
	private int start_coorX;
	/**
	 * Teleportation graphics y variable
	 */
	private int start_coorY;

	public void setStartCoordX(int i) {
		this.start_coorX = i;
	}

	public void setStartCoordY(int i) {
		this.start_coorY = i;
	}

	public int getStartCoordX() {
		return this.start_coorX;
	}

	public int getStartCoordY() {
		return this.start_coorY;
	}

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
		retStr += "* Current msg: " + currentMessage + "\n";
		retStr += Configuration.getInstance().getConfig() + "\n";
		retStr += "* Current speed: " + currentSpeed + "\n";
		retStr += "* Current scene: " + scene;
		return retStr;
	}
}
