package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class MenuScene implements Scene {

	private static final long serialVersionUID = 6499638524909742225L;

	/**
	 * Singleton instance
	 */
	public static MenuScene instance = new MenuScene();

	/**
	 * A map of the menu selection to the corresponding scene
	 */
	HashMap<Integer, Scene> menuSelections = new HashMap<Integer, Scene>();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private MenuScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);

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
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 8;

	/**
	 * Render the pause scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		WorldScene.instance.render(g, gameControl);
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 335, 20 + 32 * gameControl.getCurrentSelection(), null);
	}

	/**
	 * Handle a key press at the pause scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X)
			control.setScreen(WorldScene.instance);
		if (keyCode == KeyEvent.VK_UP) {
			control.decrementSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.incrementSelection();
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			control.setScreen(WorldScene.instance);
		}
		if (keyCode == KeyEvent.VK_Z) {
			control.setScreen(menuSelections.get(control.getCurrentSelection()));
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
