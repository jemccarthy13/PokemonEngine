package test;

import java.util.ArrayList;

import location.Location;
import location.LocationLibrary;

import org.junit.Test;

import party.Battler;
import party.BattlerFactory;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * Test the utilities class
 */
public class UtilsTest {

	/**
	 * Test generating random levels
	 */
	@Test
	public static void testRandomLevel() {
		DebugUtility.printMessage("Random Levels (2-4):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomLevel(4, 2) + " ");
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage("Random Levels: (4-9)");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomLevel(9, 4) + " ");
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage("Random Levels: (20-30)");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomLevel(30, 20) + " ");
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage();
	}

	/**
	 * Test generating random base stats based on a level
	 */
	@Test
	public static void testRandomBaseStat() {
		DebugUtility.printMessage("Random Base Stats (Level 4):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomBaseStat(4) + " ");
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage("Random Base Stats (Level 10):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomBaseStat(10) + " ");
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage("Random Base Stats (Level 25):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.print(RandomNumUtils.randomBaseStat(25) + " ");
		}
		DebugUtility.printMessage();
	}

	/**
	 * Test creating a random pokemon from "route 29"
	 */
	@Test
	public static void testRandomPokemon() {
		Location loc = LocationLibrary.getLocation("Route 29");

		DebugUtility.printMessage("Random Pokemon (Route 29):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.printMessage("-------- " + x + " --------");
			Battler p = BattlerFactory.getInstance().randomPokemon(loc);
			DebugUtility.printMessage(p.toString());
		}

		DebugUtility.printMessage();

		ArrayList<Battler> trainer = new ArrayList<Battler>();
		for (int y = 0; y < 5; y++) {
			trainer.add(BattlerFactory.getInstance().randomPokemon(loc));
		}
		DebugUtility.printMessage();
		DebugUtility.printMessage("Trainer's Random Pokemon");
		DebugUtility.printMessage("============================");
		for (int y = 1; y <= trainer.size(); y++) {
			DebugUtility.printMessage("Trainer Pkmn #" + y);
			DebugUtility.printMessage(trainer.get(y - 1).toString());
		}
		DebugUtility.printMessage();
	}
}