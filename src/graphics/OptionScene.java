package graphics;

import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of a option scene
 */
public class OptionScene extends BaseScene {

	private static final long serialVersionUID = 8790393353456574423L;
	/**
	 * Singleton instance
	 */
	public static OptionScene instance = new OptionScene();

	/**
	 * Render the option scene.
	 * 
	 * TODO more choice in options
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		String imageName = gameControl.isSoundOn() ? "OptionBG_SoundOn" : "OptionBG_SoundOff";
		g.drawImage(SpriteLibrary.getImage(imageName), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 22, 85 + 32 * gameControl.getCurrentRowSelection(), null);
	}

	/**
	 * up arrow press
	 */
	public void doUp(GameController control) {
		if (control.getCurrentRowSelection() > 0)
			control.decrementRowSelection();
	}

	/**
	 * down arrow press
	 */
	public void doDown(GameController control) {
		if (control.getCurrentRowSelection() < 6)
			control.incrementRowSelection();
	}

	/**
	 * "z" button press
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * "z" button press
	 */
	public void doAction(GameController control) {
		if (control.getCurrentRowSelection() == 5) {
			control.toggleSound();
		}
	}
}
