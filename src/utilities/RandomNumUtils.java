package utilities;

import java.util.Random;

import pokedex.Pokemon;
import trainers.Actor.DIR;

public class RandomNumUtils {

	static Random randomGenerator = new Random();

	// ////////////////////////////////////////////////////////////////////////
	//
	// generateRandom - the heart of random number generation in game
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int generateRandom(int start, int end) {
		double range = end - start + 1.0;
		double fraction = (range * randomGenerator.nextDouble());
		int num = (int) (fraction + start);
		return num;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// createTrainerID - creates a random Trainer ID for the player
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int createTrainerID() {
		int a = generateRandom(0, 9);
		int b = generateRandom(0, 9);
		int c = generateRandom(0, 9);
		int d = generateRandom(0, 9);
		int e = generateRandom(0, 9);
		return (10000 * a + 1000 * b + 100 * c + 10 * d + e);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomDirection - generates a random direction for NPCs
	//
	// ////////////////////////////////////////////////////////////////////////
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
		}
		return DIR.SOUTH;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// for an enemy pokemon, pick a random move
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int getMove(Pokemon p) {
		return generateRandom(0, p.getNumMoves() - 1);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomLevel - generates a random level between start and end, inclusive
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomLevel(int start, int end) {
		return generateRandom(start + 1, end - 1);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomBaseStat - generates a random base stat based on the given level
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomBaseStat(int level) {
		int start = (int) (2.4D * level) - 5;
		int end = (int) (2.4D * level) + 5;
		return generateRandom(start, end);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// randomStatIncr - generates a number to incrase a stat by:
	// 75% chance of +2
	// 25% chance of +3
	//
	// ////////////////////////////////////////////////////////////////////////
	public static int randomStatIncr() {
		double fraction = (100 * randomGenerator.nextDouble());
		if (fraction < 75) {
			return 2;
		}
		return 3;
	}
}
