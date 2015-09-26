package test;

import java.awt.event.KeyEvent;

import org.junit.Test;

/**
 * Select each of the pause menu items
 */
public class MenuSelectionTest extends BaseTestCase {

	/**
	 * Test the first menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testEncyclopediaOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 0);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_Encyclopedia");
	}

	/**
	 * Test the first menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPartyMemberOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 1);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_PartyScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testBagOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 2);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_BagScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testMapOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 3);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_MapScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testPlayerInfoOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 4);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_PlayerScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testSaveOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 5);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_SaveScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testOptionOption() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 6);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_OptionScene");
	}

	/**
	 * Test the second menu item on the pause menu
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testExitMenuOptions() throws InterruptedException {
		TestUtils.selectPauseMenuItem(robot, game, 7);

		Thread.sleep(2000);

		this.takeScreenshot(this.getClass().getName() + "_ExitMenuStep1");

		TestUtils.pause(robot, game);
		Thread.sleep(1000);
		this.takeScreenshot(this.getClass().getName() + "_ExitMenuStep2");

		robot.keyPress(KeyEvent.VK_X);
		Thread.sleep(1000);
		this.takeScreenshot(this.getClass().getName() + "_ExitMenuStep3");

	}
}
