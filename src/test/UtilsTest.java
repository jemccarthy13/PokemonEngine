package test;

import java.util.ArrayList;

import location.Location;
import location.LocationLibrary;
import party.PartyMember;
import party.PartyMemberFactory;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

public class UtilsTest {

	public static void runAllTests() {
		testRandomLevel();
		testRandomBaseStat();
		testRandomPokemon();
	}

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

	public static void testRandomPokemon() {
		Location loc = LocationLibrary.getLocation("Route 29");

		DebugUtility.printMessage("Random Pokemon (Route 29):");
		for (int x = 0; x < 10; x++) {
			DebugUtility.printMessage("-------- " + x + " --------");
			PartyMember p = PartyMemberFactory.getInstance().randomPokemon(loc);
			DebugUtility.printMessage(p.toString());
		}

		DebugUtility.printMessage();

		ArrayList<PartyMember> trainer = new ArrayList<PartyMember>();
		for (int y = 0; y < 5; y++) {
			trainer.add(PartyMemberFactory.getInstance().randomPokemon(loc));
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

	public static void main(String[] args) {
		runAllTests();
	}
}