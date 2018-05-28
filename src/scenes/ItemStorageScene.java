package scenes;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;

/**
 * A representation of a bag scene
 */
public class ItemStorageScene extends SelectionScene {

	private static final long serialVersionUID = 6315775737915830498L;
	/**
	 * Singleton instance
	 */
	public static ItemStorageScene instance = new ItemStorageScene();

	private ItemStorageScene() {
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

		g.drawImage(SpriteLibrary.getImage("Arrow"), 200, 20 + 32 * this.rowSelection, null);

		switch (this.colSelection) {
		case 1:
			Painter.paintSmallString(g, "KEY ITEMS", 20, 20);
			break;
		case 2:
			Painter.paintSmallString(g, "BALLS", 20, 20);
			break;
		case 3:
			Painter.paintSmallString(g, "TM/HM", 20, 20);
			break;
		default:
			Painter.paintSmallString(g, "ITEMS", 20, 20);
			break;
		}
	}

	/**
	 * Perform back button press at BagScene
	 */
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

	/**
	 * Perform the right arrow
	 */
	public void doRight(GameController control) {
		this.colSelection++;
		if (this.colSelection >= this.maxColSelection) {
			this.colSelection = 0;
		}
		this.rowSelection = 0;
	}

	/**
	 * Perform the right arrow
	 */
	public void doLeft(GameController control) {
		this.colSelection--;
		if (this.colSelection < 0) {
			this.colSelection = this.maxColSelection;
		}
		this.rowSelection = 0;
	}
}
