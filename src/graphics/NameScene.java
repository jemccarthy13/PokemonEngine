package graphics;

import java.awt.Graphics;

import model.Configuration;
import model.Coordinate;
import tiles.Tile;
import trainers.Actor.DIR;
import controller.GameController;

/**
 * A representation of the rename scene
 */
public class NameScene extends BaseScene {

	private static final long serialVersionUID = 840972645178452462L;
	/**
	 * Singleton instance
	 */
	public static NameScene instance = new NameScene();

	/**
	 * Render the Name scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Namescreen"), 0, 0, null);

		if (gameControl.getCurrentRowSelection() < 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"),
					(int) (40 + Tile.TILESIZE * 2 * gameControl.getCurrentColSelection()), 100 + Tile.TILESIZE
							* gameControl.getCurrentRowSelection(), null);
		}
		if (gameControl.getCurrentRowSelection() == 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"),
					(int) (100 + Tile.TILESIZE * 6 * gameControl.getCurrentColSelection()), 100 + Tile.TILESIZE
							* gameControl.getCurrentRowSelection(), null);
		}

		String name = gameControl.getChosenName();

		for (int x = 0; x < Configuration.MAX_NAME_SIZE; x++) {
			g.drawImage(SpriteLibrary.getImage("_"), 150 + Tile.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			Painter.paintString(g, name, 150, 40);
		}
		if (name.length() < Configuration.MAX_NAME_SIZE) {
			g.drawImage(SpriteLibrary.getImage("CURSOR"), 150 + Tile.TILESIZE * name.length(), 40, null);
		}
		g.drawImage(SpriteLibrary.getSpriteForDir(gameControl.getToBeNamed(), DIR.SOUTH).getImage(), 80, 30, null);
	}

	private void doEndDel(GameController gameControl) {
		// end the name screen logic
		if (gameControl.getCurrentColSelection() == 1 && gameControl.getChosenName().length() > 0) {
			gameControl.getPlayer().setName(gameControl.getChosenName());
			gameControl.resetNameBuilder();
			gameControl.setScene(IntroScene.instance);
		}
		// del to backspace one character
		else if (gameControl.getCurrentColSelection() == 0)
			gameControl.removeChar();
	}

	/**
	 * Perform an action (the "Z" button was pressed)
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doAction(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() == 5) {
			doEndDel(gameControl);
		} else {
			gameControl.addSelectedChar();
		}
	}

	/**
	 * "x" button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doBack(GameController gameControl) {
		gameControl.removeChar();
	}

	/**
	 * up arrow button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doUp(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() > 0) {
			gameControl.decrementRowSelection();
		}
	}

	/**
	 * left arrow button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doLeft(GameController gameControl) {
		if (gameControl.getCurrentColSelection() > 0) {
			gameControl.decrementColSelection();
		}
	}

	/**
	 * right arrow button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doRight(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() == 5) {
			// last row, right key press moves to "END"
			gameControl.setCurrentSelection(new Coordinate(5, 1));
		} else {
			gameControl.incrementColSelection();
		}
	}

	/**
	 * down arrow button press
	 * 
	 * @param gameControl
	 *            - the controller to perform game functions
	 */
	public void doDown(GameController gameControl) {
		if (gameControl.getCurrentRowSelection() < 5) {
			gameControl.incrementRowSelection();
		}
	}

	/**
	 * Key was pressed
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {

		super.keyPress(keyCode, gameControl);

		if (gameControl.getCurrentRowSelection() == 5)
			gameControl.setCurrentSelection(new Coordinate(5, 0));
	}

}
