package utilities;

import java.util.Random;

import party.Battler;
import trainers.Actor.DIR;

/**
 * Holds utilities for random number generation
 */
public class RandomNumUtils {

	/**
	 * The random number generator
	 */
	private static Random randomGenerator = new Random();

	/**
	 * The core random number generator; generate a random number between start
	 * and end
	 * 
	 * @param start
	 *            - the minimum number
	 * @param end
	 *            - the maximum number
	 * @return - a random number between start and end
	 */
	public static int generateRandom(int start, int end) {
		double range = end - start + 1.0;
		double fraction = (range * randomGenerator.nextDouble());
		int num = (int) (fraction + start);
		return num;
	}

	/**
	 * Create a random trainer ID for the player.
	 * 
	 * @return a random 6 digit ID
	 */
	public static int createTrainerID() {
		int a = generateRandom(0, 9);
		int b = generateRandom(0, 9);
		int c = generateRandom(0, 9);
		int d = generateRandom(0, 9);
		int e = generateRandom(0, 9);
		return (10000 * a + 1000 * b + 100 * c + 10 * d + e);
	}

	/**
	 * Generate a random direction (for moving NPCs)
	 * 
	 * @return a random DIR
	 */
	public static DIR randomDirection() {
		int dir = generateRandom(1, 4);
		switch (dir) {
		case 1:
			return DIR.NORTH;
		case 2:
			return DIR.EAST;
		case 3:
			return DIR.SOUTH;
		case 4:
			return DIR.WEST;
		default:
			break;
		}
		return DIR.SOUTH;
	}

	/**
	 * Pick a random move for a Party Member
	 * 
	 * @param p
	 *            - the party member
	 * @return a random move index
	 */
	public static int getMove(Battler p) {
		return generateRandom(0, p.getNumMoves() - 1);
	}

	/**
	 * Generate a random level between start and end, inclusive
	 * 
	 * @param start
	 *            - the minimum possible value
	 * @param end
	 *            - the maximum possible value
	 * @return a random level
	 */
	public static int randomLevel(int start, int end) {
		return generateRandom(start + 1, end - 1);
	}

	/**
	 * Generates a random base stat based on the given level
	 * 
	 * @param level
	 *            - the level of the Party Member
	 * @return a random int base stat for that level
	 */
	public static Integer randomBaseStat(int level) {
		int start = (int) (2.4 * level) - 5;
		int end = (int) (2.4 * level) + 5;
		int stat = generateRandom(start, end);
		return Integer.valueOf((stat < 5) ? 5 : stat);
	}

	/**
	 * generates a number to incrase a stat by: 75% chance of +2, 25% chance of
	 * +3
	 * 
	 * @TODO verify max stats increase when stats
	 * 
	 * @return a random stat increase
	 */
	public static int randomStatIncr() {
		double fraction = (100 * randomGenerator.nextDouble());
		if (fraction < 75) {
			return 2;
		}
		return 3;
	}

	/**
	 * Given a level of 'rarity', calculate whether there is a wild encounter
	 * 
	 * @return whether or not there should be a wild encounter
	 */
	public static boolean isWildEncounter() {
		// common: 10
		// rather common: 8.5
		// semi-rare: 6.75
		// rare: 3.33
		// very rare: 1.25

		int rarity = 9;

		boolean isEncounter = false;
		if (generateRandom(0, 188) <= rarity) {
			isEncounter = true;
		}
		return isEncounter;
	}
}
