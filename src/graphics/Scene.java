package graphics;

import java.awt.Graphics;
import java.io.Serializable;

import controller.GameController;

/**
 * Objects that implement Scene register to the Painter to be painted when a
 * specific SCREEN is displayed.
 * 
 * The Painter pulls Scenes out of the registration map and calls the render
 * method to paint the graphics.
 */
public interface Scene extends Serializable {

	/**
	 * Render graphics for a Scene
	 * 
	 * @param g
	 *            - the graphics to paint
	 * @param gameControl
	 *            - the game controller to use
	 */
	public void render(Graphics g, GameController gameControl);

	/**
	 * Perform Action ("z" button click)
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doAction(GameController gameControl);

	/**
	 * Perform back ("x" button click)
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doBack(GameController gameControl);

	/**
	 * Do left arrow press
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doLeft(GameController gameControl);

	/**
	 * Do right arrow press
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doRight(GameController gameControl);

	/**
	 * Do up arrow press
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doUp(GameController gameControl);

	/**
	 * Do down arrow press
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doDown(GameController gameControl);

	/**
	 * Do enter button press
	 * 
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	public void doEnter(GameController gameControl);
}
