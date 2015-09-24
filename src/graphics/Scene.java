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
	 * String locator for the party first member image
	 */
	static String partyFirstMember = "PartyFirst";
	/**
	 * String locator for the party member image box
	 */
	static String partyMember = "PartyBar";
	/**
	 * String locator for the party member background image
	 */
	static String partyBackground = "PartyBG";

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
	 * Series of events that should be performed when the scene receives a
	 * keypress call
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 * @param gameControl
	 *            - the controller to use to perform actions
	 */
	void keyPress(int keyCode, GameController gameControl);

	/**
	 * Get the ID by which this scene will be referenced
	 * 
	 * @return integer ID
	 */
	public Integer getId();

}
