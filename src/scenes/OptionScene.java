package scenes;

import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;
import model.Configuration;

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
	 * todo implement other options
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		String sound = Configuration.getInstance().isSoundOn() ? "On" : "Off";
		g.drawImage(SpriteLibrary.getImage("OptionBG"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 22, 85 + 32 * this.rowSelection, null);
		Painter.paintSmallString(g, sound, 225, 250);
	}

	/**
	 * "z" button press
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

	/**
	 * "z" button press
	 */
	@Override
	public void doAction(GameController control) {
		if (this.rowSelection == 5) {
			System.out.println("toggling sound");
			Configuration.getInstance().toggleSound();
		}
	}
}
