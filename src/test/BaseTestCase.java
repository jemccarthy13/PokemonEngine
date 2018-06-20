package test;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import controller.GameController;
import graphics.GameFrame;
import utilities.DebugUtility;

/**
 * The base test case
 */
public class BaseTestCase {

	static Robot robot;
	static GameFrame pf;
	static GameController game;

	/**
	 * Capture the current game frame and save to a file.
	 * 
	 * @param title
	 *            - the name of the file to create (usually "StepXYZ")
	 */
	public static void takeScreenshot(String title) {

		BufferedImage capture = robot
				.createScreenCapture(new Rectangle(pf.getX(), pf.getY(), pf.getWidth(), pf.getHeight()));
		File f = new File("test_results/" + title + ".jpg");
		DebugUtility.printMessage("Saving: " + f.getAbsolutePath());
		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				Assert.fail("Unable to create new test result " + e.getMessage());
			}
		}
		try {
			ImageIO.write(capture, "jpg", f);
		} catch (IOException e) {
			Assert.fail("Unable to capture screen: " + e.getMessage());
		}
	}

	/**
	 * Set up necessary variables for unit test
	 */
	@Before
	public static void InitializeForTests() {
		pf = new GameFrame();
		pf.setVisible(true);
		pf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pf.setResizable(false);
		pf.pack();
		pf.setLocationRelativeTo(null);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			Assert.fail("Unable to create robot");
		}
		game = GameFrame.getController();

		// quiet startup
		try {
			TestUtils.startNewGame(robot, game);
		} catch (InterruptedException e) {
			Assert.fail("Thread sleep failed");
		}

	}

	/**
	 * Shutdown the game
	 */
	@After
	public static void Shutdown() {
		pf.dispose();
	}
}
