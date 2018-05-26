package graphics;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import model.Coordinate;
import model.GameTime;
import model.MessageQueue;
import trainers.Player;

/**
 * A representation of a save scene
 */
public class SaveScene extends BaseScene {

	private static final long serialVersionUID = 7495740703088409291L;
	/**
	 * Singleton instance
	 */
	public static SaveScene instance = new SaveScene();

	/**
	 * Render the save scene.
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
		g.drawString(GameTime.getInstance().formatTime(), 76, 166);
		if (gameControl.getCurrentRowSelection() == 0) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 148, null);
		} else if (gameControl.getCurrentRowSelection() == 1) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 180, null);
		}
	}

	/**
	 * "x" button pressed at Save scene
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * up arrow button pressed at Save scene
	 */
	public void doUp(GameController control) {
		control.setCurrentSelection(new Coordinate(0, 0));
	}

	/**
	 * down arrow button pressed at Save scene
	 */
	public void doDown(GameController control) {
		control.setCurrentSelection(new Coordinate(1, 0));
	}

	/**
	 * action button pressed at Save scene
	 */
	public void doAction(GameController control) {
		if (control.getCurrentRowSelection() == 0) {
			control.saveGame();
			MessageQueue.getInstance().add("Game saved successfully!");
		}
		control.setScene(MenuScene.instance);
	}

}
