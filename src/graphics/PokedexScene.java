package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class PokedexScene implements Scene {

	private static final long serialVersionUID = 5686250415092668247L;
	/**
	 * Singleton instance
	 */
	public static PokedexScene instance = new PokedexScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private PokedexScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 11;

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("PokedexBG"), 0, 0, null);
	}

	/**
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
		}
		if (keyCode == KeyEvent.VK_UP) {
			control.decrementRowSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.incrementRowSelection();
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
