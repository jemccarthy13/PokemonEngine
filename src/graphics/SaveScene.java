package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import model.Coordinate;
import trainers.Player;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class SaveScene implements Scene {

	private static final long serialVersionUID = 7495740703088409291L;
	/**
	 * Singleton instance
	 */
	public static SaveScene instance = new SaveScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private SaveScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 15;

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {

		WorldScene.instance.render(g, gameControl);

		g.setColor(Color.BLACK);

		Player player = gameControl.getPlayer();

		g.drawImage(SpriteLibrary.getImage("Save"), 0, 0, null);
		g.drawString(player.getName(), 100, 68);
		g.drawString(((Integer) player.getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(gameControl.formatTime(), 76, 166);
		if (gameControl.getCurrentRowSelection() == 0) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 148, null);
		} else if (gameControl.getCurrentRowSelection() == 1) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 180, null);
		}
	}

	/**
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
		}
		if (keyCode == KeyEvent.VK_UP) {
			control.setCurrentSelection(new Coordinate(0, 0));
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.setCurrentSelection(new Coordinate(1, 0));
		}
		if (keyCode == KeyEvent.VK_Z) {
			if (control.getCurrentRowSelection() == 0) {
				control.saveGame();
				control.setCurrentMessage("Game saved successfully!");
			}
			control.setScreen(MenuScene.instance);
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
