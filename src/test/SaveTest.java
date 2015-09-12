package test;

import graphics.GamePanel;

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
		g.game.saveGame();
	}

	public static void main(String[] args) {
		testSave();
	}
}