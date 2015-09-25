package graphics;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of a bag scene
 */
public class BagScene extends BaseScene {

	private static final long serialVersionUID = 6315775737915830498L;
	/**
	 * Singleton instance
	 */
	public static BagScene instance = new BagScene();

	/**
	 * Render the bag scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
	}

	/**
	 * Perform back button press at BagScene
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}
}
