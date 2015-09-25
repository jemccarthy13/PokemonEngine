package graphics;

import java.awt.Graphics;

import trainers.Actor.DIR;
import controller.GameController;

/**
 * A representation of intro scene
 */
public class IntroScene extends BaseScene {

	private static final long serialVersionUID = 2833416784544170753L;
	/**
	 * Singleton instance
	 */
	public static IntroScene instance = new IntroScene();

	/**
	 * Render the Introduction scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Beginning"), 0, 0, null);
		g.drawImage(SpriteLibrary.getSpriteForDir("PROFESSOROAK_LARGE", DIR.SOUTH).getImage(), 150, 20, null);
	}

	/**
	 * Perform "Z" button click
	 */
	public void doAction(GameController control) {
		if (control.hasNextMessage()) {
			control.nextMessage();
		} else {
			control.playBackgroundMusic("NewBarkTown");
			control.setScene(WorldScene.instance);
		}
	}
}
