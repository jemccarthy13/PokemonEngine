package graphics;

import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of the continue scene
 */
public class ContinueScene extends BaseScene {

	private static final long serialVersionUID = 2882429066379575720L;
	/**
	 * Singleton instance
	 */
	public static ContinueScene instance = new ContinueScene();

	/**
	 * Render the continue scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Continue"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 13, 20 + 32 * gameControl.getCurrentRowSelection(), null);
	}

	/**
	 * Up arrow button press
	 */
	public void doUp(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() > 0)
			gameControl.decrementRowSelection();
	}

	/**
	 * Down arrow button press
	 */
	public void doDown(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() < 2)
			gameControl.incrementRowSelection();
	}

	/**
	 * "z" button press
	 */
	public void doAction(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() == 0) {
			gameControl.startGame(true);
		} else if (gameControl.getCurrentRowSelection() == 1) {
			gameControl.startGame(false);

		}
	}
}
