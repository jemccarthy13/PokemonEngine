package graphics;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import model.GameTime;
import trainers.Player;

/**
 * A representation of player information
 */
public class PlayerInfoScene extends BaseScene {

	private static final long serialVersionUID = 3015914725120653475L;
	/**
	 * Singleton instance
	 */
	public static PlayerInfoScene instance = new PlayerInfoScene();

	/**
	 * Pad a given string with appropriate spacing
	 * 
	 * @param toBePadded
	 *            - the string to be padded
	 * @return string + 12-len(string) spaces
	 */
	public static String getPadding(String toBePadded) {
		int numSpaces = 12 - toBePadded.length();
		String retStr = "";
		for (int x = 0; x < numSpaces; x++) {
			retStr += " ";
		}
		return retStr;
	}

	/**
	 * Render the trainer card scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("TrainerCard"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Male"), 320, 100, null);

		Player player = gameControl.getPlayer();

		Painter.paintSmallString(g, "ID " + player.getID(), 295, 40);
		Painter.paintSmallString(g, "Name:" + getPadding("Name:") + player.getName(), 65, 80);
		Painter.paintSmallString(g, "Money:" + getPadding("Money:") + "$" + player.getMoney(), 65, 140);
		Painter.paintSmallString(g, "Pokedex:" + getPadding("Pokedex:") + player.getNumPokemonOwned(), 65, 170);
		Painter.paintSmallString(g, "Time:" + getPadding("Time:") + GameTime.getInstance().formatTime(), 65, 200);
	}

	/**
	 * "x" button press at Trainer Card scene
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * up arrow button press at Trainer Card scene
	 */
	public void doUp(GameController control) {
		control.decrementRowSelection();
	}

	/**
	 * up arrow button press at Trainer Card scene
	 */
	public void doDown(GameController control) {
		control.incrementRowSelection();
	}
}
