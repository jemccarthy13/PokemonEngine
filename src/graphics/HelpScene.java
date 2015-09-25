package graphics;

import java.awt.Color;
import java.awt.Graphics;

import model.Coordinate;
import utilities.DebugUtility;
import controller.GameController;

/**
 * A representation of pokegear scene
 */
public class HelpScene extends BaseScene {

	private static final long serialVersionUID = 8611728687807704267L;
	/**
	 * Singleton instance
	 */
	public static HelpScene instance = new HelpScene();

	/**
	 * Render the pokegear scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("Map"), 0, 0, null);

		// TODO paint rectangle around selection

		// TODO up arrow increment map selection, down arrow decrement

		/**
		 * g.setColor(Color.BLACK);
		 * g.drawImage(SpriteLibrary.getImage("PokegearBG"), 0, 0, null); switch
		 * (gameControl.getCurrentRowSelection()) { case 0:
		 * g.drawImage(SpriteLibrary.getImage("PokegearMap"), 0, 0, null);
		 * break; case 1: g.drawImage(SpriteLibrary.getImage("PokegearRadio"),
		 * 0, 0, null); break; case 2:
		 * g.drawImage(SpriteLibrary.getImage("PokegearPhone"), 0, 0, null);
		 * break; case 3: g.drawImage(SpriteLibrary.getImage("PokegearExit"), 0,
		 * 0, null); break; }
		 */
	}

	/**
	 * "x" button press at Pokegear scene
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * up arrow button press at Pokegear scene
	 */
	public void doUp(GameController control) {
		if (control.getCurrentRowSelection() > 0) {
			control.decrementRowSelection();
		}
	}

	/**
	 * up arrow button press at Pokegear scene
	 */
	public void doDown(GameController control) {
		if (control.getCurrentRowSelection() < 3) {
			control.incrementRowSelection();
		}
	}

	/**
	 * "z" button press at Pokegear scene
	 */
	public void doAction(GameController control) {
		if (control.getCurrentRowSelection() == 0) {
			// TODO - add Map painting
			DebugUtility.printMessage("Map");
		} else if (control.getCurrentRowSelection() == 1) {
			// TODO - add Radio painting
			DebugUtility.printMessage("Radio");
		} else if (control.getCurrentRowSelection() == 2) {
			// TODO - add Phone painting
			DebugUtility.printMessage("Phone");
		} else if (control.getCurrentRowSelection() == 3) {
			control.setScene(MenuScene.instance);
		}
		control.setCurrentSelection(new Coordinate(0, 0));
	}
}
