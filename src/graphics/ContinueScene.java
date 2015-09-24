package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class ContinueScene implements Scene {

	private static final long serialVersionUID = 2882429066379575720L;
	/**
	 * Singleton instance
	 */
	public static ContinueScene instance = new ContinueScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private ContinueScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 1;

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Continue"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 13, 20 + 32 * gameControl.getCurrentSelection(), null);
	}

	/**
	 * Handle a key press at the continue scene
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {
		// continue screen choice select
		if (keyCode == KeyEvent.VK_UP) {
			if (gameControl.getCurrentSelection() > 0)
				gameControl.decrementSelection();
		} else if (keyCode == KeyEvent.VK_DOWN) {
			if (gameControl.getCurrentSelection() < 2)
				gameControl.incrementSelection();
		}
		if (keyCode == KeyEvent.VK_Z) {
			if (gameControl.getCurrentSelection() == 0) {
				gameControl.startGame(true);
			} else if (gameControl.getCurrentSelection() == 1) {
				gameControl.startGame(false);
			}
		}
		gameControl.playClip(SOUND_EFFECT.SELECT);
	}

	/**
	 * @return the ID of this scene
	 */
	@Override
	public Integer getId() {
		return this.ID;
	}

}
