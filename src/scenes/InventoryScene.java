package scenes;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;
import storage.Bag;
import storage.Item;
import storage.ItemList;

/**
 * A representation of a bag scene
 */
public class InventoryScene extends SelectionScene {

	private static final long serialVersionUID = 6315775737915830498L;
	/**
	 * Singleton instance
	 */
	public static InventoryScene instance = new InventoryScene();

	protected InventoryScene() {
		maxRowSelection = 5;
		maxColSelection = 4;
	}

	/**
	 * Render the bag scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Bag"), 20, 65, null);

		Painter.paintSmallString(g, Bag.getPocket(this.colSelection).toString(), 20, 20);

		ItemList items = gameControl.getPlayer().getInventory().getCurrentItemList();

		int n = 0;
		for (Item it : items) {
			Painter.paintSmallString(g, it.getName(), 200, 25 + 32 * n);
			Painter.paintSmallString(g, String.valueOf(items.get(it)), 400, 25 + 32 * n);
			n++;
		}

		if (items.size() != 0 && !(this instanceof ItemConfirmScene))
			g.drawImage(SpriteLibrary.getImage("Arrow"), 180, 20 + 32 * this.rowSelection, null);
	}

	/**
	 * Perform back button press at BagScene
	 */
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

	/**
	 * Perform select ("action")
	 * 
	 * @param control
	 */
	public void doAction(GameController control) {
		GameGraphicsData.getInstance().setScene(ItemConfirmScene.instance);
	}

	public void setMaxRow(GameController control) {
		int numItems = control.getPlayer().getBag().getCurrentItemList().size() - 1;
		if (numItems > 5)
			numItems = 5;
		this.maxRowSelection = numItems;
	}

	/**
	 * Perform the right arrow
	 */
	public void doRight(GameController control) {
		this.colSelection++;
		if (this.colSelection >= this.maxColSelection) {
			this.colSelection = 0;
		}
		control.getPlayer().getBag().setCurrentPocket(Bag.getPocket(this.colSelection));
		setMaxRow(control);

		this.rowSelection = 0;
	}

	/**
	 * Perform the right arrow
	 */
	public void doLeft(GameController control) {
		this.colSelection--;
		if (this.colSelection < 0) {
			this.colSelection = this.maxColSelection - 1;
		}
		control.getPlayer().getBag().setCurrentPocket(Bag.getPocket(this.colSelection));
		setMaxRow(control);
		this.rowSelection = 0;
	}
}
