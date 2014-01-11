package test;

import locations.Location;

import org.junit.Assert;
import org.junit.Test;

import utilities.EnumsAndConstants;

public class LocationTest {
	@Test
	public void testLocation() {
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.MOMSHOUSE).getName().equals("Mom's House"));
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getTrainers() == null);
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getMaxLevel("Pidgey") == 4);
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getMinLevel("Pidgey") == 2);
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getPokemon(60).equals("Rattatta"));
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getPokemon(43).equals("Pidgey"));
		Assert.assertTrue(((Location) EnumsAndConstants.loc_lib.ROUTE29).getPokemon(80).equals("Sentret"));
	}
}