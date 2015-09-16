package location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import model.Coordinate;

import org.json.simple.JSONObject;

import trainers.Actor.DIR;

// ////////////////////////////////////////////////////////////////////////
//
// LocationData - loads all data for a particular location from a file
//
// ////////////////////////////////////////////////////////////////////////
public class LocationData extends JSONObject implements Serializable {

	private static final long serialVersionUID = 5824633156334142566L;
	public String name = null;
	public Boolean canFlyOutOf = null;
	public Coordinate topLeft = null, bottomRight = null;
	public HashMap<DIR, Integer> boundaries = null;
	public ArrayList<String> pokemon = null;
	public ArrayList<Integer> probabilities = null;
	public ArrayList<Integer> minLevels = null, maxLevels = null;

	public Object[] toArray() {
		return new Object[] { name, canFlyOutOf, boundaries.get(DIR.NORTH), boundaries.get(DIR.EAST),
				boundaries.get(DIR.SOUTH), boundaries.get(DIR.WEST), pokemon, probabilities, minLevels, maxLevels };
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructs a LocationData object
	//
	// ////////////////////////////////////////////////////////////////////////
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

	public boolean isValidData() {
		return (name != null && canFlyOutOf != null && boundaries != null && minLevels != null && maxLevels != null);
	}

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