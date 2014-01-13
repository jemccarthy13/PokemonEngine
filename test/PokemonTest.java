package test;

import org.junit.Assert;

import data_structures.Pokemon;
import data_structures.PokemonData;

public class PokemonTest {
	public static void testPokemon() {

		PokemonData pData = new PokemonData("Data/Pokemon/BULBASAUR.PDAT");

		Pokemon bulbasaur = new Pokemon(pData, 5);
		Pokemon bulbasaur2 = new Pokemon(pData, 10);

		System.out.println(bulbasaur.toString());

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
		System.exit(0);
	}

	public static void main(String[] args) {
		testPokemon();
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: test.PokemonTest
 * 
 * JD-Core Version: 0.7.0.1
 */