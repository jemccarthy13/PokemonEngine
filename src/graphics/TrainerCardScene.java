package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import trainers.Player;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class TrainerCardScene implements Scene {

	private static final long serialVersionUID = 3015914725120653475L;
	/**
	 * Singleton instance
	 */
	public static TrainerCardScene instance = new TrainerCardScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private TrainerCardScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 14;

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
	 * Render the title scene.
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
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
		}
		if (keyCode == KeyEvent.VK_UP) {
			control.decrementRowSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.incrementRowSelection();
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
