package trainers;

import java.io.Serializable;
import java.util.ArrayList;

import location.Location;
import location.LocationLibrary;
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
	/**
	 * The current town / section of map where the player is
	 */
	public Location curLoc;
	/**
	 * Item and Party Member storage
	 */
	public Storage storage = new Storage();

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
		super(x, y, n, "PLAYER");
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
}