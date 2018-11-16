package test;

import java.io.IOException;

import org.junit.Test;

import graphics.GamePanel;
import utilities.DebugUtility;

/**
 * Test saving and loading the game.
 */
public class SaveTest {

	/**
	 * Test to save the game
	 */
	@Test
	public static void testSave() {
		GamePanel g = GamePanel.getInstance();
		try {
			g.gameController.saveGame();
		} catch (IOException e) {
			DebugUtility.printError(e.getMessage());
		}
	}
}