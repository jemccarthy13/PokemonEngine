package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import model.Coordinate;
import utilities.DebugUtility;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class PokegearScene implements Scene {

	private static final long serialVersionUID = 8611728687807704267L;
	/**
	 * Singleton instance
	 */
	public static PokegearScene instance = new PokegearScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private PokegearScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 12;

	/**
	 * Render the title scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("PokegearBG"), 0, 0, null);
		switch (gameControl.getCurrentRowSelection()) {
		case 0:
			g.drawImage(SpriteLibrary.getImage("PokegearMap"), 0, 0, null);
			break;
		case 1:
			g.drawImage(SpriteLibrary.getImage("PokegearRadio"), 0, 0, null);
			break;
		case 2:
			g.drawImage(SpriteLibrary.getImage("PokegearPhone"), 0, 0, null);
			break;
		case 3:
			g.drawImage(SpriteLibrary.getImage("PokegearExit"), 0, 0, null);
			break;
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
		if (keyCode == KeyEvent.VK_UP && control.getCurrentRowSelection() > 0) {
			control.decrementRowSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN && control.getCurrentRowSelection() < 3) {
			control.incrementRowSelection();
		}
		if (keyCode == KeyEvent.VK_Z) {
			if (control.getCurrentRowSelection() == 0) {
				// TODO - add Map painting
				DebugUtility.printMessage("Map");
			} else if (control.getCurrentRowSelection() == 1) {
				// TODO - add Radio painting
				DebugUtility.printMessage("Radio");
			} else if (control.getCurrentRowSelection() == 2) {
				// TODO - add Phone painting
				DebugUtility.printMessage("Phone");
			} else if (control.getCurrentRowSelection() == 3) {
				control.setScreen(MenuScene.instance);
			}
			control.setCurrentSelection(new Coordinate(0, 0));
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
