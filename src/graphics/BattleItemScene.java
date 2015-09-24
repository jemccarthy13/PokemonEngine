package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a battle item scene
 * 
 * TODO battle item scene painting of items and logic to use items
 */
public class BattleItemScene implements Scene {

	private static final long serialVersionUID = 4236362282505475572L;
	/**
	 * Singleton instance
	 */
	public static BattleItemScene instance = new BattleItemScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BattleItemScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 6;

	/**
	 * Render the battle item scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
	}

	/**
	 * Handle a key press at the battle item scene
	 * 
	 * TODO implement using an item
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
			control.setScreen(BattleScene.instance);
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
