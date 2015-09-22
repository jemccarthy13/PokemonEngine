package location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import trainers.Actor.DIR;

/**
 * Loads all data for a particular location from a file
 */
public class LocationData extends JSONObject implements Serializable {

	private static final long serialVersionUID = 5824633156334142566L;
	/**
	 * Name of this location
	 */
	public String name = null;
	/**
	 * Can the player quick travel from here?
	 */
	public Boolean canFlyOutOf = null;
	/**
	 * The boundaries of this location
	 */
	public HashMap<DIR, Integer> boundaries = null;
	/**
	 * The names of the battlers found here
	 */
	public ArrayList<String> pokemon = null;
	/**
	 * The probability that those battlers will be found
	 */
	public ArrayList<Integer> probabilities = null;
	/**
	 * The minimum levels corresponding to the battlers found here
	 */
	public ArrayList<Integer> minLevels = null;
	/**
	 * The maximum levels corresponding to the battlers found here
	 */
	public ArrayList<Integer> maxLevels = null;

	/**
	 * Convert this location data to an array of Object. For use in tables (see
	 * editors.locationeditor.LocationTableModel)
	 * 
	 * @return array of objects of this data entry
	 */
	public Object[] toArray() {
		return new Object[] { name, canFlyOutOf, boundaries.get(DIR.NORTH), boundaries.get(DIR.EAST),
				boundaries.get(DIR.SOUTH), boundaries.get(DIR.WEST), pokemon, probabilities, minLevels, maxLevels };
	}

	/**
	 * Default constructor
	 */
	public LocationData() {
		boundaries = new HashMap<DIR, Integer>();
		boundaries.put(DIR.NORTH, 0);
		boundaries.put(DIR.SOUTH, 0);
		boundaries.put(DIR.EAST, 0);
		boundaries.put(DIR.WEST, 0);
		pokemon = new ArrayList<String>();
		probabilities = new ArrayList<Integer>();
		minLevels = new ArrayList<Integer>();
		maxLevels = new ArrayList<Integer>();
		canFlyOutOf = false;
		name = "";

	}

	/**
	 * Check whether or not the location data is valid
	 * 
	 * @return whether or not the data is valid
	 */
	public boolean isValidData() {
		return (name != null && canFlyOutOf != null && boundaries != null && minLevels != null && maxLevels != null);
	}

	/**
	 * Create a string representation of the data
	 * 
	 * @return string representation
	 */
	public String toString() {
		String retStr = "Location: " + this.name + "\n";;
		retStr += "Fly: " + this.canFlyOutOf + "\n";
		retStr += "Boundaries: " + "\n";
		retStr += "N: " + boundaries.get(DIR.NORTH) + "\n";
		retStr += "S: " + boundaries.get(DIR.SOUTH) + "\n";
		retStr += "E: " + boundaries.get(DIR.EAST) + "\n";
		retStr += "W: " + boundaries.get(DIR.WEST) + "\n";
		return retStr;
	}
}