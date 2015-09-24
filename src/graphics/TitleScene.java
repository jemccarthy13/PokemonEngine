package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class TitleScene implements Scene {

	private static final long serialVersionUID = 8904865279363446269L;
	/**
	 * Singleton instance
	 */
	public static TitleScene instance = new TitleScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private TitleScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 0;

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
			gameControl.playBackgroundMusic("Continue");
			gameControl.setScreen(ContinueScene.instance);
		}
	}

	/**
	 * @return the ID of this scene
	 */
	@Override
	public Integer getId() {
		return this.ID;
	}

}
