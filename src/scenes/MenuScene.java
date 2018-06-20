package scenes;

import java.awt.Graphics;
import java.util.HashMap;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;

/**
 * A representation of the pause menu scene
 */
public class MenuScene extends SelectionScene {

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

		this.menuSelections.put(0, EncycopediaScene.instance);
		this.menuSelections.put(1, PartyScene.instance);
		this.menuSelections.put(2, InventoryScene.instance);
		this.menuSelections.put(3, HelpScene.instance);
		this.menuSelections.put(4, PlayerInfoScene.instance);
		this.menuSelections.put(5, SaveScene.instance);
		this.menuSelections.put(6, OptionScene.instance);
		this.menuSelections.put(7, WorldScene.instance);

		super.maxColSelection = 0;
		super.maxRowSelection = 7;
	}

	/**
	 * Render the pause scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		WorldScene.instance.render(g, gameControl);
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 335, 20 + 32 * this.rowSelection, null);
	}

	/**
	 * "Z" button pressed
	 */
	@Override
	public void doAction(GameController control) {
		if (this.menuSelections.get(this.rowSelection) == InventoryScene.instance) {
			InventoryScene.instance.setMaxRow(control);
		}
		GameGraphicsData.getInstance().setScene(this.menuSelections.get(this.rowSelection));
	}

	/**
	 * "x" button pressed
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(WorldScene.instance);
	}

	/**
	 * Enter button pressed
	 */
	@Override
	public void doEnter(GameController control) {
		GameGraphicsData.getInstance().setScene(WorldScene.instance);
	}
}
