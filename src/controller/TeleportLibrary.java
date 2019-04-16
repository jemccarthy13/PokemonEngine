package controller;

import java.util.HashMap;
import java.util.Map;

import model.Coordinate;

/**
 * Populates a map of teleport locations
 */
public class TeleportLibrary {

	/**
	 * The container of one way teleport locations
	 * 
	 * @todo reciprocal teleportation
	 */
	static Map<Coordinate, Coordinate> teleports = new HashMap<>();

	/**
	 * Constructs a new teleportation library
	 */
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

	/**
	 * Returns the list of teleport coordinates
	 * 
	 * @return a map of teleport coordinates
	 */
	public static Map<Coordinate, Coordinate> getList() {
		return teleports;
	}
}
