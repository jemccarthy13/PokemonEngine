package scenes;

import java.awt.Graphics;
import java.io.IOException;

import controller.GameController;
import graphics.SpriteLibrary;
import model.MessageQueue;

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
		g.drawImage(SpriteLibrary.getImage("Arrow"), 13, 20 + 32 * this.rowSelection, null);
	}

	/**
	 * "z" button press
	 */
	@Override
	public void doAction(GameController gameControl) {
		try {
			if (this.rowSelection == 0) {
				gameControl.startGame(true);
			} else if (this.rowSelection == 1) {
				gameControl.startGame(false);
			}
		} catch (IOException e) {
			MessageQueue.getInstance().add("Unable to start game!\n " + e.getMessage());
		}
	}
}
