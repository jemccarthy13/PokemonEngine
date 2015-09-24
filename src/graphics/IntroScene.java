package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import trainers.Actor.DIR;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class IntroScene implements Scene {

	private static final long serialVersionUID = 2833416784544170753L;
	/**
	 * Singleton instance
	 */
	public static IntroScene instance = new IntroScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private IntroScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 2;

	/**
	 * Render the Introduction scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Beginning"), 0, 0, null);
		g.drawImage(SpriteLibrary.getSpriteForDir("PROFESSOROAK_LARGE", DIR.SOUTH).getImage(), 150, 20, null);
	}

	/**
	 * Handle a key press at the intro scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_Z) {
			if (control.hasNextMessage()) {
				control.nextMessage();
			} else {
				control.playBackgroundMusic("NewBarkTown");
				control.setScreen(WorldScene.instance);
			}
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
