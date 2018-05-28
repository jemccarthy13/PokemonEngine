package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.HashMap;

import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;
import graphics.GameGraphicsData;
import model.MessageQueue;
import scenes.BaseScene;
import scenes.BattleMessageScene;
import scenes.IntroScene;
import scenes.NameScene;
import scenes.SelectionScene;

/**
 * Listens for key presses in the game
 */
public class GameKeyListener implements KeyListener, Serializable {

	/**
	 * Serialization value
	 */
	private static final long serialVersionUID = 433796777156267003L;

	/**
	 * Game logic controller
	 */
	GameController gameControl;

	/**
	 * Hashmap of actions to occur at registered scenes
	 */
	static HashMap<Integer, BaseScene> actionPerformers = new HashMap<Integer, BaseScene>();

	/**
	 * GameKeyListener instance
	 */
	private static GameKeyListener instance = new GameKeyListener();

	/**
	 * Set a new game controller to be used
	 * 
	 * @param controller
	 *            - the controller to use
	 */
	public void setGameController(GameController controller) {
		instance.gameControl = controller;
	}

	/**
	 * If there is no instance already, create one.
	 * 
	 * @param controller
	 *            - the game controller to use
	 * @return - the GameKeyListener instance
	 */
	public static GameKeyListener getInstance() {
		if (instance == null) {
			instance = new GameKeyListener();
		}
		return instance;
	}

	/**
	 * Register scene's actions to be performed
	 * 
	 * @param scene
	 *            - the scene to register
	 */
	public void register(BaseScene scene) {
		actionPerformers.put(scene.getClass().hashCode(), scene);
	}

	/**
	 * Handles the game logic of converting the key press to a series of game
	 * controller commands.
	 * 
	 * @param e
	 *            - the key that was pressed
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		boolean processed = false;
		if ((keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) && MessageQueue.getInstance().hasNextMessage()
				&& GameGraphicsData.getInstance().getScene() != NameScene.instance) {
			MessageQueue.getInstance().nextMessage();
			processed = true;
		}
		if ((!processed && MessageQueue.getInstance().hasNextMessage() == false)
				|| GameGraphicsData.getInstance().getScene() == BattleMessageScene.instance
				|| GameGraphicsData.getInstance().getScene() == IntroScene.instance
				|| GameGraphicsData.getInstance().getScene() == NameScene.instance) {
			Integer actionToPerform = GameGraphicsData.getInstance().getScene().getClass().hashCode();
			actionPerformers.get(actionToPerform).keyPress(keyCode, gameControl);
		}

		if (GameGraphicsData.getInstance().getScene() instanceof SelectionScene) {
			// play key press sound effect
			AudioLibrary.playClip(SOUND_EFFECT.SELECT);
		}
	}

	/**
	 * Handles logic on a key being released
	 * 
	 * @param e
	 *            - the key that was released
	 */
	public void keyReleased(KeyEvent e) {}

	/**
	 * Handles logic on a key being typed
	 * 
	 * @param e
	 *            - the key that was typed
	 */
	public void keyTyped(KeyEvent e) {}
}
