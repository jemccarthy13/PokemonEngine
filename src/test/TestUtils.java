package test;

import scenes.ContinueScene;
import scenes.TitleScene;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.junit.Assert;

import utilities.DebugUtility;
import utilities.DebugUtility.DEBUG_LEVEL;
import controller.GameController;

/**
 * Utilities to perform common actions.
 */
public class TestUtils {

	/**
	 * Start a new game
	 * 
	 * @param robot
	 *            - the robot to use
	 * @param game
	 *            - the controller for this test
	 * @throws InterruptedException
	 */
	public static void startNewGame(Robot robot, GameController game) throws InterruptedException {
		DebugUtility.setLevel(DEBUG_LEVEL.OFF);
		DebugUtility.setLevel(DEBUG_LEVEL.DEBUG);

		DebugUtility.printHeader("Startup completed");

		Thread.sleep(2000);

		// assert we are at the tile screen
		Assert.assertEquals(TitleScene.instance, game.getScene());

		// assert the game controller has been loaded
		Assert.assertNotNull(game);

		// assert we don't have a character yet
		Assert.assertNull(game.getPlayer());

		// move to the continue screen
		Thread.sleep(500);
		robot.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(500);

		// assert the continue screen was loaded
		Assert.assertEquals(ContinueScene.instance, game.getScene());

		robot.keyPress(KeyEvent.VK_DOWN);
		Thread.sleep(250);
		robot.keyPress(KeyEvent.VK_Z);

		Thread.sleep(250);
	}

	/**
	 * Open the pause menu and choose a given item
	 * 
	 * @param robot
	 *            - perform mouse actions
	 * @param game
	 *            - the game controller
	 * @param choice_num
	 *            - the menu option to choose
	 * @throws InterruptedException
	 */
	public static void selectPauseMenuItem(Robot robot, GameController game, int choice_num)
			throws InterruptedException {
		robot.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(500);

		for (int x = 0; x < choice_num; x++) {
			robot.keyPress(KeyEvent.VK_DOWN);
			Thread.sleep(500);
		}

		robot.keyPress(KeyEvent.VK_Z);
		Thread.sleep(5000);
	}

	/**
	 * open the pause menu
	 * 
	 * @param robot
	 *            robot to use to perform actions
	 * @param game
	 *            the controller for the game
	 * @throws InterruptedException
	 */
	public static void pause(Robot robot, GameController game) throws InterruptedException {
		robot.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(500);
	}
}
