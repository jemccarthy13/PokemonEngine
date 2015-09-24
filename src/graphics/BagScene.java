package graphics;

import java.awt.Color;
import java.awt.Graphics;

import com.sun.glass.events.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class BagScene implements Scene {

	private static final long serialVersionUID = 6315775737915830498L;
	/**
	 * Singleton instance
	 */
	public static BagScene instance = new BagScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BagScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 9;

	/**
	 * Render the bag scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
	}

	/**
	 * Handle a key press at the bag scene
	 * 
	 * TODO implement item selection / use logic and rendering
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
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
