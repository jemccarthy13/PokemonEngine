package graphics;

import java.awt.Graphics;

import com.sun.glass.events.KeyEvent;

import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A base scene - has keyPress to determine which action to call on the child
 * classes. This class's constructor also performs the registration of the child
 * classes with the renderer and the key listener.
 * 
 * If a child class does not implement an action, then no-op is performed
 */
public class BaseScene implements Scene {

	/**
	 * Serialization information
	 */
	public static final long serialVersionUID = 7044599140923943638L;

	/**
	 * Register the created scene with the renderer and action performer
	 */
	protected BaseScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	}

	/**
	 * Render the scene
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		throw new IllegalStateException("Cannot render BaseScene");
	}

	/**
	 * Handle key pressed at the scene
	 * 
	 * @param keyCode
	 *            - key pressed
	 * @param gameControl
	 *            - the game controller to use
	 */
	public void keyPress(int keyCode, GameController gameControl) {
		switch (keyCode) {
		case KeyEvent.VK_ENTER:
			doEnter(gameControl);
			break;
		case KeyEvent.VK_Z:
			doAction(gameControl);
			break;
		case KeyEvent.VK_X:
			doBack(gameControl);
			break;
		case KeyEvent.VK_UP:
			doUp(gameControl);
			break;
		case KeyEvent.VK_DOWN:
			doDown(gameControl);
			break;
		case KeyEvent.VK_LEFT:
			doLeft(gameControl);
			break;
		case KeyEvent.VK_RIGHT:
			doRight(gameControl);
			break;
		}

		// play sound when any button is pressed
		AudioLibrary.playClip(SOUND_EFFECT.SELECT);
	}

	/**
	 * Handle enter button press at scene
	 */
	@Override
	public void doEnter(GameController gameControl) {}

	/**
	 * Handle "Z" button press at scene
	 */
	@Override
	public void doAction(GameController gameControl) {}

	/**
	 * Handle "x" button press at scene
	 */
	@Override
	public void doBack(GameController gameControl) {}

	/**
	 * Handle left arrow press at scene
	 */
	@Override
	public void doLeft(GameController gameControl) {}

	/**
	 * Handle right arrow press at scene
	 */
	@Override
	public void doRight(GameController gameControl) {}

	/**
	 * Up arrow press at scene
	 */
	@Override
	public void doUp(GameController gameControl) {}

	/**
	 * Down arrow press at scene
	 */
	@Override
	public void doDown(GameController gameControl) {}
}
