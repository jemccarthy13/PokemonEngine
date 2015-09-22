package graphics;

import java.awt.Graphics;

import controller.GameController;

/**
 * Objects that implement Scene register to the Painter to be painted when a
 * specific SCREEN is displayed.
 * 
 * The Painter pulls Scenes out of the registration map and calls the render
 * method to paint the graphics.
 */
public interface Scene {

	/**
	 * Render graphics for a Scene.
	 * 
	 * @param g
	 *            - the graphics to paint
	 * @param gameControl
	 *            - the game controller to use
	 */
	public void render(Graphics g, GameController gameControl);
}
