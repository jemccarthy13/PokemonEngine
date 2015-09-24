package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import model.Configuration;
import tiles.Tile;
import trainers.Actor.DIR;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class NameScene implements Scene {

	private static final long serialVersionUID = 840972645178452462L;
	/**
	 * Singleton instance
	 */
	public static NameScene instance = new NameScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private NameScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 3;

	/**
	 * Render the Name scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Namescreen"), 0, 0, null);

		if (gameControl.getNameRowSelection() < 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"),
					(int) (40 + Tile.TILESIZE * 2 * gameControl.getNameColSelection()), 100 + Tile.TILESIZE
							* gameControl.getNameRowSelection(), null);
		}
		if (gameControl.getNameRowSelection() == 5) {
			g.drawImage(SpriteLibrary.getImage("Arrow"),
					(int) (100 + Tile.TILESIZE * 6 * gameControl.getNameColSelection()), 100 + Tile.TILESIZE
							* gameControl.getNameRowSelection(), null);
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
		if (gameControl.getNameColSelection() == 1 && gameControl.getChosenName().length() > 0) {
			gameControl.getPlayer().setName(gameControl.getChosenName());
			gameControl.resetNameBuilder();
			gameControl.setScreen(IntroScene.instance);
		}
		// del to backspace one character
		else if (gameControl.getNameColSelection() == 0)
			gameControl.removeChar();
	}

	/**
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {
		if (keyCode == KeyEvent.VK_X)
			gameControl.removeChar();
		if ((keyCode == KeyEvent.VK_Z)) {
			if (gameControl.getNameRowSelection() == 5) {
				doEndDel(gameControl);
			} else {
				gameControl.addSelectedChar();
			}
		}
		if (keyCode == KeyEvent.VK_DOWN && gameControl.getNameRowSelection() < 5) {
			gameControl.incrNameRowSelection();
			if (gameControl.getNameRowSelection() == 5)
				gameControl.setNameColSelection(0);
		} else if (keyCode == KeyEvent.VK_UP && gameControl.getNameRowSelection() > 0) {
			gameControl.decrNameRowSelection();
		} else if (keyCode == KeyEvent.VK_LEFT && gameControl.getNameColSelection() > 0) {
			gameControl.decrNameColSelection();
		} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getNameRowSelection() == 5
				&& gameControl.getNameColSelection() < 1) {
			gameControl.incrNameColSelection();
		} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getNameRowSelection() < 5
				&& gameControl.getNameColSelection() < 5) {
			gameControl.incrNameColSelection();
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
