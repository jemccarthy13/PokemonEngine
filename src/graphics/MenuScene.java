package graphics;

import java.awt.Graphics;
import java.util.HashMap;

import controller.GameController;

/**
 * A representation of the pause menu scene
 */
public class MenuScene extends BaseScene {

	private static final long serialVersionUID = 6499638524909742225L;

	/**
	 * Singleton instance
	 */
	public static MenuScene instance = new MenuScene();

	/**
	 * A map of the menu selection to the corresponding scene
	 */
	HashMap<Integer, BaseScene> menuSelections = new HashMap<Integer, BaseScene>();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private MenuScene() {
		super();

		menuSelections.put(0, PokedexScene.instance);
		menuSelections.put(1, PokemonScene.instance);
		menuSelections.put(2, BagScene.instance);
		menuSelections.put(3, PokegearScene.instance);
		menuSelections.put(4, TrainerCardScene.instance);
		menuSelections.put(5, SaveScene.instance);
		menuSelections.put(6, OptionScene.instance);
		menuSelections.put(7, WorldScene.instance);
	};

	/**
	 * Render the pause scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		WorldScene.instance.render(g, gameControl);
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 335, 20 + 32 * gameControl.getCurrentRowSelection(), null);
	}

	/**
	 * "Z" button pressed
	 */
	public void doAction(GameController control) {
		control.setScene(menuSelections.get(control.getCurrentRowSelection()));
	}

	/**
	 * up button pressed
	 */
	public void doUp(GameController control) {
		if (control.getCurrentRowSelection() > 0)
			control.decrementRowSelection();
	}

	/**
	 * down button pressed
	 */
	public void doDown(GameController control) {
		if (control.getCurrentRowSelection() < 7)
			control.incrementRowSelection();
	}

	/**
	 * "x" button pressed
	 */
	public void doBack(GameController control) {
		control.setScene(WorldScene.instance);
	}

	/**
	 * Enter button pressed
	 */
	public void doEnter(GameController control) {
		control.setScene(WorldScene.instance);
	}
}
