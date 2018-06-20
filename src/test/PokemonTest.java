package test;

import org.junit.Assert;
import org.junit.Test;

import controller.GameController;
import party.Battler;
import party.Battler.STAT;
import party.BattlerFactory;
import utilities.DebugUtility;
import utilities.DebugUtility.DEBUG_LEVEL;

/**
 * Unit testing for Party member constructors and information
 */
public class PokemonTest {

	/**
	 * Test doing damage and healing party members
	 */
	@Test
	public static void testPartyMemberHealth() {
		Battler member1 = BattlerFactory.createPokemon("Bulbasaur", 5);
		int health = member1.getMaxStat(STAT.HP);
		member1.takeDamage(health / 2);

		Assert.assertEquals(member1.getStat(STAT.HP), health / 2);

		member1.fullHeal();
		Assert.assertEquals(member1.getStat(STAT.HP), health);

		member1.takeDamage(health - 1);
		Assert.assertEquals(member1.getStat(STAT.HP), 1);
	}

	/**
	 * Test level up evolution of a party member
	 */
	@Test
	public static void testPartyMemberEvolution() {
		DebugUtility.setLevel(DEBUG_LEVEL.DEBUG);
		Battler member1 = BattlerFactory.createPokemon("Bulbasaur", 5);
		Battler member2 = BattlerFactory.createPokemon("Bulbasaur", 10);

		DebugUtility.printMessage(member1.toString());

		GameController game = new GameController();

		Assert.assertTrue(member1.getLevel() == 5);
		for (int m = 0; m < 10; m++) {
			member1.levelUp(game);
		}
		Assert.assertTrue(member1.getLevel() == 15);
		Assert.assertTrue(member1.getName().equals("Bulbasaur"));
		member1.levelUp(game);
		Assert.assertTrue(member1.getLevel() == 16);
		Assert.assertTrue(member1.getName().equals("Ivysaur"));
		for (int k = 0; k < 15; k++) {
			member1.levelUp(game);
		}
		Assert.assertTrue(member1.getLevel() == 31);
		Assert.assertTrue(member1.getName().equals("Ivysaur"));
		member1.levelUp(game);
		Assert.assertTrue(member1.getLevel() == 32);
		Assert.assertTrue(member1.getName().equals("Venusaur"));
		for (int z = 0; z < 30; z++) {
			member1.levelUp(game);
		}

		Assert.assertTrue(member2.getName().equals("Bulbasaur"));
		Assert.assertTrue(member2.getLevel() == 10);
	}

	/**
	 * Test level up moves learned (up to 4 and more than 4)
	 */
	public void testPartyMemberMovesLearned() {
		// do nothing
	}
}