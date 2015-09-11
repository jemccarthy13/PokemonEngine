package test;

import utilities.Utils;
import driver.GamePanel;

// ////////////////////////////////////////////////////////////////////////
//
// Unit testing for Pokemon constructors and information
// 
// TODO - needs updating
//
// ////////////////////////////////////////////////////////////////////////
public class SaveTest {
	public static void testSave() {
		GamePanel g = new GamePanel();
		Utils.saveGame(g);
	}

	public static void main(String[] args) {
		testSave();
	}
}