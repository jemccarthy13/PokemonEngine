package test;

import graphics.GamePanel;

import org.junit.Test;

/**
 * Test saving and loading the game.
 */
public class SaveTest {

	/**
	 * Test to save the game
	 */
	@Test
	public static void testSave() {
		GamePanel g = new GamePanel();
		g.gameController.saveGame();
	}
}