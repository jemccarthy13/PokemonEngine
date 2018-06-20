package test;

import java.awt.event.KeyEvent;

import org.junit.Assert;
import org.junit.Test;

import graphics.GameGraphicsData;
import model.Configuration;
import scenes.WorldScene;
import utilities.DebugUtility;

/**
 * Test the setup and starting of a new game.
 */
public class StartNewGameTest extends BaseTestCase {

	/**
	 * Start a new frame, load the game.
	 * 
	 * @Step 1. Press enter to go to the continue screen
	 * @Step 2. Press the down arrow to select "new game"
	 * @Step 3. Press 'z' to begin a new game
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public static void testStartUp() throws InterruptedException {

		if (Configuration.SHOWINTRO) {
			// test introductory scene
		}

		// finally the game is started and we're at the world
		Assert.assertEquals(WorldScene.instance, GameGraphicsData.getInstance().getScene());
		Thread.sleep(250);

		DebugUtility.printMessage(game.getPlayer().getPosition().toString());
		robot.keyPress(KeyEvent.VK_RIGHT);
		Thread.sleep(750);
		DebugUtility.printMessage(game.getPlayer().getPosition().toString());
		robot.keyPress(KeyEvent.VK_RIGHT);
		Thread.sleep(750);
		DebugUtility.printMessage(game.getPlayer().getPosition().toString());
		robot.keyPress(KeyEvent.VK_RIGHT);
		Thread.sleep(750);
		DebugUtility.printMessage(game.getPlayer().getPosition().toString());
		robot.keyPress(KeyEvent.VK_RIGHT);
		Thread.sleep(750);
		DebugUtility.printMessage(game.getPlayer().getPosition().toString());

		Thread.sleep(5000);

		pf.dispose();
	}
}
