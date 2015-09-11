package test;

import org.junit.Assert;

import pokedex.Pokemon;
import pokedex.PokemonFactory;
import utilities.DebugUtility;

// ////////////////////////////////////////////////////////////////////////
//
// Unit testing for Pokemon constructors and information
// 
// TODO - needs updating
//
// ////////////////////////////////////////////////////////////////////////
public class PokemonTest {
	public static void testPokemon() {
		Pokemon bulbasaur = PokemonFactory.createPokemon("Bulbasaur", 5);
		Pokemon bulbasaur2 = PokemonFactory.createPokemon("Bulbasaur", 10);

		DebugUtility.printMessage(bulbasaur.toString());

		Assert.assertTrue(bulbasaur.getLevel() == 5);
		for (int m = 0; m < 10; m++) {
			bulbasaur.levelUp();
		}
		Assert.assertTrue(bulbasaur.getLevel() == 15);
		Assert.assertTrue(bulbasaur.getName().equals("Bulbasaur"));
		bulbasaur.levelUp();
		Assert.assertTrue(bulbasaur.getLevel() == 16);
		Assert.assertTrue(bulbasaur.getName().equals("Ivysaur"));
		for (int k = 0; k < 15; k++) {
			bulbasaur.levelUp();
		}
		Assert.assertTrue(bulbasaur.getLevel() == 31);
		Assert.assertTrue(bulbasaur.getName().equals("Ivysaur"));
		bulbasaur.levelUp();
		Assert.assertTrue(bulbasaur.getLevel() == 32);
		Assert.assertTrue(bulbasaur.getName().equals("Venusaur"));
		for (int z = 0; z < 30; z++) {
			bulbasaur.levelUp();
		}

		Assert.assertTrue(bulbasaur2.getName().equals("Bulbasaur"));
		Assert.assertTrue(bulbasaur2.getLevel() == 10);
	}

	public static void main(String[] args) {
		testPokemon();
	}
}