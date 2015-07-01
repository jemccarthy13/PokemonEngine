package test;

import driver.Game;
import utilities.Utils;

// ////////////////////////////////////////////////////////////////////////
//
// Unit testing for Pokemon constructors and information
// 
// TODO - needs updating
//
// ////////////////////////////////////////////////////////////////////////
public class SaveTest {
	public static void testSave() {
		Game g = new Game();
		Utils.saveGame(g);
		System.exit(0);
	}

	public static void main(String[] args) {
		testSave();
	}
}