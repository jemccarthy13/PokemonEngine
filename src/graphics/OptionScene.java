package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class OptionScene implements Scene {

	private static final long serialVersionUID = 8790393353456574423L;
	/**
	 * Singleton instance
	 */
	public static OptionScene instance = new OptionScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private OptionScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 10;

	/**
	 * Render the title scene.
	 * 
	 * TODO more choice in options
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		String imageName = gameControl.isSoundOn() ? "OptionBG_SoundOn" : "OptionBG_SoundOff";
		g.drawImage(SpriteLibrary.getImage(imageName), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 22, 85 + 32 * gameControl.getCurrentSelection(), null);
	}

	/**
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_UP) {
			control.decrementSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.incrementSelection();
		}
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
		}
		if (keyCode == KeyEvent.VK_Z) {
			if (control.getCurrentSelection() == 5) {
				control.toggleSound();
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
