package location;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import trainers.Actor.DIR;

/**
 * Loads all data for a particular location from a file
 */
public class LocationData extends JSONObject {

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
		return new Object[] { this.name, this.canFlyOutOf, this.boundaries.get(DIR.NORTH),
				this.boundaries.get(DIR.EAST), this.boundaries.get(DIR.SOUTH), this.boundaries.get(DIR.WEST),
				this.pokemon, this.probabilities, this.minLevels, this.maxLevels };
	}

	/**
	 * Default constructor
	 */
	public LocationData() {
		this.boundaries = new HashMap<>();
		this.boundaries.put(DIR.NORTH, Integer.valueOf(0));
		this.boundaries.put(DIR.SOUTH, Integer.valueOf(0));
		this.boundaries.put(DIR.EAST, Integer.valueOf(0));
		this.boundaries.put(DIR.WEST, Integer.valueOf(0));
		this.pokemon = new ArrayList<>();
		this.probabilities = new ArrayList<>();
		this.minLevels = new ArrayList<>();
		this.maxLevels = new ArrayList<>();
		this.canFlyOutOf = Boolean.valueOf(false);
		this.name = "";

	}

	/**
	 * Check whether or not the location data is valid
	 * 
	 * @return whether or not the data is valid
	 */
	public boolean isValidData() {
		return (this.name != null && this.canFlyOutOf != null && this.boundaries != null && this.minLevels != null
				&& this.maxLevels != null);
	}

	/**
	 * Create a string representation of the data
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		String retStr = "Location: " + this.name + "\n";
		retStr += "Fly: " + this.canFlyOutOf + "\n";
		retStr += "Boundaries: " + "\n";
		retStr += "N: " + this.boundaries.get(DIR.NORTH) + "\n";
		retStr += "S: " + this.boundaries.get(DIR.SOUTH) + "\n";
		retStr += "E: " + this.boundaries.get(DIR.EAST) + "\n";
		retStr += "W: " + this.boundaries.get(DIR.WEST) + "\n";
		return retStr;
	}
}