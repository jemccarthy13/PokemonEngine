package scenes;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;
import model.GameTime;
import model.MessageQueue;
import trainers.Player;

/**
 * A representation of a save scene
 */
public class SaveScene extends SelectionScene {

	private static final long serialVersionUID = 7495740703088409291L;
	/**
	 * Singleton instance
	 */
	public static SaveScene instance = new SaveScene();

	private SaveScene() {
		this.maxColSelection = 0;
		this.maxRowSelection = 1;
	}

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
		g.drawString(Integer.valueOf(player.getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(GameTime.getInstance().formatTime(), 76, 166);
		if (this.rowSelection == 0) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 148, null);
		} else if (this.rowSelection == 1) {
			g.drawImage(SpriteLibrary.getImage("Arrow"), 394, 180, null);
		}
	}

	/**
	 * "x" button pressed at Save scene
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

	/**
	 * action button pressed at Save scene
	 */
	@Override
	public void doAction(GameController control) {
		if (this.rowSelection == 0) {
			try {
				control.saveGame();
				MessageQueue.getInstance().add("Game saved successfully!");
			} catch (IOException e) {
				MessageQueue.getInstance().add("Unable to save game!\n" + e.getMessage());
			}
		}
		GameGraphicsData.getInstance().setScene(MenuScene.instance);
	}

}
