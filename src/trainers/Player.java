package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import controller.GameController;
import graphics.GameMap;
import location.Location;
import location.LocationLibrary;
import model.Configuration;
import model.Coordinate;
import party.Storage;
import utilities.RandomNumUtils;

/**
 * Player - holds data for the current user
 */
public class Player extends Actor implements Serializable {

	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 4426558941037291067L;
	/**
	 * The player's current rival
	 */
	public Actor rival;
	/**
	 * The list of names of beaten trainers
	 */
	public ArrayList<String> beatenTrainers = new ArrayList<String>();
	/**
	 * The number of badges this player has earned
	 */
	public int badges = 0;
	/**
	 * Player (Trainer) id - also used as game session ID
	 */
	public int id;
	public boolean canMove = false;
	/**
	 * The current town / section of map where the player is
	 */
	public Location curLoc;
	/**
	 * Item and Party Member storage
	 */
	public Storage storage = new Storage();

	private static String spriteName = "PLAYER";

	/**
	 * Player constructor given location and a name
	 * 
	 * @param x
	 *            - x location
	 * @param y
	 *            - y location
	 * @param n
	 *            - name of player
	 */
	public Player(int x, int y, String n) {
		super(x, y, n, spriteName);
		curLoc = new Location(LocationLibrary.getInstance().get("New Bark Town"));
		id = RandomNumUtils.createTrainerID();
	}

	/**
	 * Get the player's current town / section of map
	 * 
	 * @return current location
	 */
	public Location getCurLoc() {
		return curLoc;
	}

	public void doAnimation(GameController gameController) {
		if (isWalking()) {
			gameController.setOffsetY(getDirection());
			gameController.setOffsetX(getDirection());
		}
		super.doAnimation(gameController);
	}

	/**
	 * Set the player's current town / section of map
	 * 
	 * @param location
	 *            - player's new location
	 */
	public void setCurLocation(Location location) {
		this.curLoc = location;
	}

	/**
	 * Sets the player's rival's name
	 * 
	 * @param n
	 *            - the rival's new name
	 */
	public void setRivalName(String n) {
		this.rival.setName(n);
	}

	/**
	 * Get the name of the player's rival
	 * 
	 * @return name of player rival
	 */
	public String getRivalName() {
		return this.rival.getName();
	}

	/**
	 * Get the number of badges this player won
	 * 
	 * @return - number of badges won
	 */
	public int getBadges() {
		return this.badges;
	}

	/**
	 * Set the number of badges this player won
	 * 
	 * @param badges
	 */
	public void setBadges(int badges) {
		this.badges = badges;
	}

	/**
	 * Set the trainer ID
	 * 
	 * @param input
	 *            - the new ID
	 */
	public void setID(int input) {
		this.id = input;
	}

	/**
	 * Get the player's trainer ID
	 * 
	 * @return trainer ID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Check whether or not a player can move in a given direction
	 * 
	 * @param dir
	 *            - the direction to move
	 * @return - true iff the next tile is available
	 */
	public boolean canMoveInDir(DIR dir) {
		boolean can = false;

		if (Configuration.getInstance().isNoClip()) {
			// if noclip is turned on, player can always move
			can = true;
		} else {
			// temporarily store the location the player would move to
			Coordinate loc = getPosition().move(dir);

			// if the potential location is in bounds, can only move if tile is
			// not obstacle
			if (GameMap.getInstance().isInBounds(loc)) {
				can = !GameMap.getInstance().isObstacleAt(loc);
			}
		}
		return can;
	}
}