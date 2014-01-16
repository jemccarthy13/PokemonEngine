package test;

import java.util.ArrayList;

import utilities.Utils;
import data_structures.Pokemon;

public class UtilsTest {

	public static void runAllTests() {
		testRandomLevel();
		testRandomBaseStat();
		testRandomPokemon();
	}

	public static void testRandomLevel() {
		System.out.println("Random Levels (2-4):");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomLevel(4, 2) + " ");
		}
		System.out.println();
		System.out.println("Random Levels: (4-9)");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomLevel(9, 4) + " ");
		}
		System.out.println();
		System.out.println("Random Levels: (20-30)");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomLevel(30, 20) + " ");
		}
		System.out.println();
		System.out.println();
	}

	public static void testRandomBaseStat() {
		System.out.println("Random Base Stats (Level 4):");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomBaseStat(4) + " ");
		}
		System.out.println();
		System.out.println("Random Base Stats (Level 10):");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomBaseStat(10) + " ");
		}
		System.out.println();
		System.out.println("Random Base Stats (Level 25):");
		for (int x = 0; x < 10; x++) {
			System.out.print(Utils.randomBaseStat(25) + " ");
		}
		System.out.println();
	}

	public static void testRandomPokemon() {
		// Location loc = new Route29();
		System.out.println("Random Pokemon (Route 29):");
		for (int x = 0; x < 10; x++) {
			// Pokemon p = Utils.randomPokemon(loc);
			// System.out.println(p);
		}
		System.out.println();

		ArrayList<Pokemon> trainer = new ArrayList<Pokemon>();
		for (int y = 0; y < 5; y++) {
			// trainer.add(Utils.randomPokemon(loc));
		}
		System.out.println();
		System.out.println("Trainer's Random Pokemon");
		for (int y = 0; y < trainer.size(); y++) {
			System.out.println(trainer.get(y));
		}
		System.out.println();
	}

	public static void main(String[] args) {
		runAllTests();
	}
}