package test;

import graphics.GameFrame;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import model.Configuration;
import model.GameData.SCREEN;

import org.junit.Assert;
import org.junit.Test;

import utilities.DebugUtility;
import utilities.DebugUtility.DEBUG_LEVEL;
import controller.GameController;

/**
 * Test the setup and starting of a new game.
 */
public class StartNewGameTest {

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
	public void testStartUp() throws InterruptedException {
		// quiet startup
		DebugUtility.setLevel(DEBUG_LEVEL.OFF);

		GameFrame pf = new GameFrame();
		pf.setVisible(true);
		pf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pf.setResizable(false);
		pf.pack();
		pf.setLocationRelativeTo(null);

		DebugUtility.setLevel(DEBUG_LEVEL.DEBUG);

		GameController game = pf.getController();
		DebugUtility.printHeader("Startup completed");

		Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			Assert.fail(e.getMessage());
		}

		// assert we are at the tile screen
		Assert.assertEquals(SCREEN.TITLE, game.getScreen());

		// assert the game controller has been loaded
		Assert.assertNotNull(game);

		// assert we don't have a character yet
		Assert.assertNull(game.getPlayer());

		// move to the continue screen
		Thread.sleep(250);
		robot.keyPress(KeyEvent.VK_ENTER);
		Thread.sleep(250);

		// assert the continue screen was loaded
		Assert.assertEquals(SCREEN.CONTINUE, game.getScreen());

		robot.keyPress(KeyEvent.VK_DOWN);
		Thread.sleep(250);
		robot.keyPress(KeyEvent.VK_Z);

		Thread.sleep(250);

		if (Configuration.SHOWINTRO) {
			// test introductory scene
		}

		// finally the game is started and we're at the world
		Assert.assertEquals(SCREEN.WORLD, game.getScreen());
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
