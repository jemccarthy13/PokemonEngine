package scenes;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;

/**
 * A representation of pokegear scene
 */
public class HelpScene extends SelectionScene {

	private static final long serialVersionUID = 8611728687807704267L;
	/**
	 * Singleton instance
	 */
	public static HelpScene instance = new HelpScene();

	private HelpScene() {
		super.maxColSelection = 0;
		super.maxRowSelection = 3;
	}

	/**
	 * Render the pokegear scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("Map"), 0, 0, null);

		// todo paint rectangle around selection

		// todo up arrow increment map selection, down arrow decrement

		/**
		 * g.setColor(Color.BLACK); g.drawImage(SpriteLibrary.getImage("PokegearBG"), 0,
		 * 0, null); switch (gameControl.getCurrentRowSelection()) { case 0:
		 * g.drawImage(SpriteLibrary.getImage("PokegearMap"), 0, 0, null); break; case
		 * 1: g.drawImage(SpriteLibrary.getImage("PokegearRadio"), 0, 0, null); break;
		 * case 2: g.drawImage(SpriteLibrary.getImage("PokegearPhone"), 0, 0, null);
		 * break; case 3: g.drawImage(SpriteLibrary.getImage("PokegearExit"), 0, 0,
		 * null); break; }
		 */
	}

	/**
	 * "x" button press at Pokegear scene
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

	/**
	 * "z" button press at Pokegear scene
	 */
	@Override
	public void doAction(GameController control) {
		// control.setCurrentSelection(new Coordinate(0, 0));
		// todo map "Z" button press
	}
}
