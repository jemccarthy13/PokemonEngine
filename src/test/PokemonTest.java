package test;

import org.junit.Assert;
import org.junit.Test;

import party.PartyMember;
import party.PartyMember.STAT;
import party.PartyMemberFactory;
import utilities.DebugUtility;
import utilities.DebugUtility.DEBUG_LEVEL;

/**
 * Unit testing for Pokemon constructors and information
 */
public class PokemonTest {

	/**
	 * Test doing damage and healing party members
	 */
	@Test
	public void testPartyMemberHealth() {
		PartyMember member1 = PartyMemberFactory.createPokemon("Bulbasaur", 5);
		int health = member1.getMaxStat(STAT.HP);
		member1.doDamage(health / 2);

		Assert.assertEquals(member1.getStat(STAT.HP), health / 2);

		member1.fullHeal();
		Assert.assertEquals(member1.getStat(STAT.HP), health);

		member1.doDamage(health - 1);
		Assert.assertEquals(member1.getStat(STAT.HP), 1);
	}

	/**
	 * Test level up party member
	 */
	@Test
	public void testPartyMemberLevelUp() {
		DebugUtility.setLevel(DEBUG_LEVEL.DEBUG);
		PartyMember member1 = PartyMemberFactory.createPokemon("Bulbasaur", 5);
		PartyMember member2 = PartyMemberFactory.createPokemon("Bulbasaur", 10);

		DebugUtility.printMessage(member1.toString());

		Assert.assertTrue(member1.getLevel() == 5);
		for (int m = 0; m < 10; m++) {
			member1.levelUp();
		}
		Assert.assertTrue(member1.getLevel() == 15);
		Assert.assertTrue(member1.getName().equals("Bulbasaur"));
		member1.levelUp();
		Assert.assertTrue(member1.getLevel() == 16);
		Assert.assertTrue(member1.getName().equals("Ivysaur"));
		for (int k = 0; k < 15; k++) {
			member1.levelUp();
		}
		Assert.assertTrue(member1.getLevel() == 31);
		Assert.assertTrue(member1.getName().equals("Ivysaur"));
		member1.levelUp();
		Assert.assertTrue(member1.getLevel() == 32);
		Assert.assertTrue(member1.getName().equals("Venusaur"));
		for (int z = 0; z < 30; z++) {
			member1.levelUp();
		}

		Assert.assertTrue(member2.getName().equals("Bulbasaur"));
		Assert.assertTrue(member2.getLevel() == 10);
	}
}