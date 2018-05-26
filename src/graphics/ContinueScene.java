package graphics;

import java.awt.Graphics;

import controller.GameController;
import model.GameData;

/**
 * A representation of the continue scene
 */
public class ContinueScene extends SelectionScene {

	private static final long serialVersionUID = 2882429066379575720L;
	/**
	 * Singleton instance
	 */
	public static ContinueScene instance = new ContinueScene();

	private ContinueScene() {
		super.maxColSelection = 0;
		super.maxRowSelection = 2;
	}

	/**
	 * Render the continue scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Continue"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 13,
				20 + 32 * GameData.getInstance().getCurrentRowSelection(gameControl.getScene()), null);
	}

	/**
	 * "z" button press
	 */
	public void doAction(GameController gameControl) {
		if (GameData.getInstance().getCurrentRowSelection(gameControl.getScene()) == 0) {
			gameControl.startGame(true);
		} else if (GameData.getInstance().getCurrentRowSelection(gameControl.getScene()) == 1) {
			gameControl.startGame(false);
		}
	}
}
