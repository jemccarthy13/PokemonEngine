package factories;

import java.util.HashMap;
import java.util.Map;

import tiles.Coordinate;

public class TeleportFactory {

	Map<Coordinate, Coordinate> teleports = new HashMap<Coordinate, Coordinate>();

	public TeleportFactory() {
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
