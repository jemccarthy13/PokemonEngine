package scenes;

import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;
import model.Configuration;
import model.Coordinate;
import model.NameBuilder;
import tiles.Tile;
import trainers.Actor.DIR;

/**
 * A representation of the rename scene
 */
public class NameScene extends SelectionScene {

	private static final long serialVersionUID = 840972645178452462L;
	/**
	 * Singleton instance
	 */
	public static NameScene instance = new NameScene();

	private NameScene() {
		super.maxRowSelection = 5;
		super.maxColSelection = 5;
	}

	/**
	 * Render the Name scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Namescreen"), 0, 0, null);

		if (this.rowSelection < 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), (int) (40 + Tile.TILESIZE * 2 * this.colSelection),
					100 + Tile.TILESIZE * this.rowSelection, null);
		}
		if (this.rowSelection == 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), (int) (100 + Tile.TILESIZE * 6 * this.colSelection),
					100 + Tile.TILESIZE * this.rowSelection, null);
		}

		String name = NameBuilder.getInstance().toString();

		for (int x = 0; x < Configuration.MAX_NAME_SIZE; x++) {
			g.drawImage(SpriteLibrary.getImage("_"), 150 + Tile.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			Painter.paintString(g, name, 150, 40);
		}
		if (name.length() < Configuration.MAX_NAME_SIZE) {
			g.drawImage(SpriteLibrary.getImage("CURSOR"), 150 + Tile.TILESIZE * name.length(), 40, null);
		}
		g.drawImage(SpriteLibrary.getSpriteForDir(NameBuilder.getInstance().getToBeNamed(), DIR.SOUTH).getImage(), 80,
				30, null);
	}

	private void doEndDel(GameController gameControl) {
		String name = NameBuilder.getInstance().toString();
		// end the name screen logic
		if (this.colSelection == 1 && name.length() > 0) {
			gameControl.getPlayer().setName(name);
			NameBuilder.getInstance().reset();
			GameGraphicsData.getInstance().setScene(IntroScene.instance);
		}
		// del to backspace one character
		else if (this.colSelection == 0)
			NameBuilder.getInstance().removeChar();
	}

	/**
	 * Perform an action (the "Z" button was pressed)
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doAction(GameController gameControl) {
		if (this.rowSelection == 5) {
			doEndDel(gameControl);
		} else {
			NameBuilder.getInstance().addSelectedChar(new Coordinate(this.rowSelection, this.colSelection));
		}
	}

	/**
	 * "x" button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doBack(GameController gameControl) {
		NameBuilder.getInstance().removeChar();
	}

	/**
	 * right arrow button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doRight(GameController gameControl) {
		super.doRight(gameControl);
		if (this.rowSelection == 5 && this.colSelection > 1) {
			this.colSelection = 1;
		}
	}

	/**
	 * Down arrow button press
	 * 
	 * @param gameControl
	 *            - a controller to perform game functions
	 */
	public void doDown(GameController gameControl) {
		if (this.rowSelection == 4) {
			this.colSelection = 1;
			this.rowSelection = 5;
		} else {
			super.doDown(gameControl);
		}
	}
}
