package test;

import org.junit.Assert;
import org.junit.Test;

import pokedex.Bulbasaur;

public class PokemonTest {
	@Test
	public void testPokemon() {
		Bulbasaur bulbasaur = new Bulbasaur(5);
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
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: test.PokemonTest
 * 
 * JD-Core Version: 0.7.0.1
 */