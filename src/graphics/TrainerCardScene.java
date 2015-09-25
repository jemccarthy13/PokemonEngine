package graphics;

import java.awt.Color;
import java.awt.Graphics;

import trainers.Player;
import controller.GameController;

/**
 * A representation of a title scene
 */
public class TrainerCardScene extends BaseScene {

	private static final long serialVersionUID = 3015914725120653475L;
	/**
	 * Singleton instance
	 */
	public static TrainerCardScene instance = new TrainerCardScene();

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
		g.drawString("ID:  " + player.getID(), 295, 54);
		g.drawString("Name:" + getPadding("Name:") + player.getName(), 64, 93);
		g.drawString("Money:" + getPadding("Money:") + "$" + player.getMoney(), 64, 150);
		g.drawString("Pokedex:" + getPadding("Pokedex:") + player.getNumPokemonOwned(), 64, 183);
		g.drawString("Time:  " + getPadding("Time:") + gameControl.formatTime(), 64, 213);
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
