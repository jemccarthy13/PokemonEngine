package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import audio.AudioLibrary;
import controller.GameController;

/**
 * A representation of a title scene
 */
public class TitleScene extends BaseScene {

	private static final long serialVersionUID = 8904865279363446269L;
	/**
	 * Singleton instance
	 */
	public static TitleScene instance = new TitleScene();

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Title"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Start"), 0, 260, null);
	}

	/**
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {
		if (keyCode == KeyEvent.VK_ENTER) {
			AudioLibrary.playBackgroundMusic("Continue");
			gameControl.setScene(ContinueScene.instance);
		}
	}

}
