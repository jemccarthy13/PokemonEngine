package scenes;

import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;

/**
 * A representation of a bag scene
 */
public class ItemConfirmScene extends InventoryScene {

	private static final long serialVersionUID = -8128814959473744472L;
	/**
	 * Singleton instance
	 */
	static ItemConfirmScene instance = new ItemConfirmScene();

	private ItemConfirmScene() {
		this.maxRowSelection = 1;
		this.maxColSelection = 0;
	}

	/**
	 * Render the bag scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		super.render(g, gameControl);
		g.drawImage(SpriteLibrary.getImage("ConfirmBox"), 380, 130, null);
		Painter.paintSmallString(g, "USE", 410, 155);
		Painter.paintSmallString(g, "GIVE", 410, 187);
		g.drawImage(SpriteLibrary.getImage("Arrow"), 390, 150 + 32 * this.rowSelection, null);
	}

	/**
	 * Perform back button press at BagScene
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(InventoryScene.instance);
	}

	/**
	 * Perform select ("action")
	 * 
	 * @param control
	 */
	@Override
	public void doAction(GameController control) {
		// use item
	}
}
