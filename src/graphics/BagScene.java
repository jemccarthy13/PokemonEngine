package graphics;

import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of a title scene
 */
public class BagScene implements Scene {

	/**
	 * Singleton instance
	 */
	public static BagScene instance = new BagScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BagScene() {
		Painter.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public static int ID = 1;

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
	public void keyPress(int keyCode, GameController control) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the ID of this scene
	 */
	@Override
	public Integer getId() {
		return ID;
	}

}
