package libraries;

import java.util.HashMap;
import java.util.Map;

import tiles.Coordinate;

//////////////////////////////////////////////////////////////////////////
//
// Populates a map of teleport locations
//
//////////////////////////////////////////////////////////////////////////
public class TeleportLibrary {

	Map<Coordinate, Coordinate> teleports = new HashMap<Coordinate, Coordinate>();

	public TeleportLibrary() {
		/*
		 * Coordinate first = new Coordinate(15, 7); Coordinate second = new
		 * Coordinate(EnumsAndConstants.MOMSHOUSEX,
		 * EnumsAndConstants.MOMSHOUSEY); teleports.put(first, second); first =
		 * new Coordinate(8, 5); second = new
		 * Coordinate(EnumsAndConstants.ELMSLABX, EnumsAndConstants.ELMSLABY);
		 * teleports.put(first, second);
		 */
	}

	public Map<Coordinate, Coordinate> getListofTeleports() {
		return teleports;
	}
}
