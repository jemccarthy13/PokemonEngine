package location;

import java.util.ArrayList;
import java.util.HashMap;

import tiles.Coordinate;
import trainers.Actor.DIR;

// ////////////////////////////////////////////////////////////////////////
//
// LocationData - loads all data for a particular location from a file
//
// ////////////////////////////////////////////////////////////////////////
public class LocationData {

	public String name = null;
	public Boolean canFlyOutOf = null;
	public Coordinate topLeft = null, bottomRight = null;
	public HashMap<DIR, Integer> boundaries = null;
	public ArrayList<String> pokemon = null;
	public ArrayList<Integer> probabilities = null;
	public ArrayList<Integer> minLevels = null, maxLevels = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructs a LocationData object
	//
	// ////////////////////////////////////////////////////////////////////////
	public LocationData() {}

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