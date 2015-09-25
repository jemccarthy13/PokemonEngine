package graphics;

import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of a pokedex scene
 */
public class EncycopediaScene extends BaseScene {

	private static final long serialVersionUID = 5686250415092668247L;
	/**
	 * Singleton instance
	 */
	public static EncycopediaScene instance = new EncycopediaScene();

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
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * up arrow button press at Pokedex scene
	 */
	public void doUp(GameController control) {
		control.decrementRowSelection();
	}

	/**
	 * up arrow button press at Pokedex scene
	 */
	public void doDown(GameController control) {
		control.incrementRowSelection();
	}
}
