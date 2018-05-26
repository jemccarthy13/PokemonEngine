package graphics;

import java.awt.Graphics;

import controller.GameController;
import model.Configuration;
import model.GameData;

/**
 * A representation of a option scene
 */
public class OptionScene extends SelectionScene {

	private static final long serialVersionUID = 8790393353456574423L;
	/**
	 * Singleton instance
	 */
	public static OptionScene instance = new OptionScene();

	private OptionScene() {
		super.maxColSelection = 0;
		super.maxRowSelection = 5;
	}

	/**
	 * Render the option scene.
	 * 
	 * TODO more choice in options
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		String sound = Configuration.getInstance().isSoundOn() ? "On" : "Off";
		g.drawImage(SpriteLibrary.getImage("OptionBG"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 22,
				85 + 32 * GameData.getInstance().getCurrentRowSelection(gameControl.getScene()), null);
		Painter.paintSmallString(g, sound, 225, 250);
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
		if (GameData.getInstance().getCurrentRowSelection(control.getScene()) == 5) {
			Configuration.getInstance().toggleSound();
		}
	}
}
