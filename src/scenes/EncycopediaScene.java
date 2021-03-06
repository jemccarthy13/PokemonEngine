package scenes;

import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;

/**
 * A representation of a pokedex scene
 */
public class EncycopediaScene extends SelectionScene {

	private static final long serialVersionUID = 5686250415092668247L;
	/**
	 * Singleton instance
	 */
	public static EncycopediaScene instance = new EncycopediaScene();

	private EncycopediaScene() {
		super.maxColSelection = 0;
		super.maxRowSelection = 150;
	}

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("PokedexBG"), 0, 0, null);
	}

	/**
	 * "x" button press at Pokedex scene
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}
}
