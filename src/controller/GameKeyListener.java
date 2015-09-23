package controller;

import graphics.BagScene;
import graphics.BattleMessageScene;
import graphics.BattleScene;
import graphics.IntroScene;
import graphics.MenuScene;
import graphics.MessageScene;
import graphics.NameScene;
import graphics.OptionScene;
import graphics.PokedexScene;
import graphics.PokegearScene;
import graphics.PokemonScene;
import graphics.SaveScene;
import graphics.Scene;
import graphics.TrainerCardScene;
import graphics.WorldScene;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.HashMap;

import utilities.DebugUtility;
import audio.AudioLibrary.SOUND_EFFECT;

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
	static HashMap<Integer, Scene> actionPerformers = new HashMap<Integer, Scene>();

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
	public void register(Scene scene) {
		actionPerformers.put(scene.getId(), scene);
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

		if (gameControl.getScene() == NameScene.instance) {
			actionPerformers.get(3).keyPress(keyCode, gameControl);
		} else if ((keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) && gameControl.getCurrentMessage() != null) {
			if (gameControl.getScene() != NameScene.instance) {
				gameControl.nextMessage();
			}
			if (gameControl.getScene() == IntroScene.instance) {
				gameControl.incrIntroStage();
			}
			if (gameControl.getScene() == IntroScene.instance && gameControl.getIntroStage() == 15) {
				if (gameControl.getIntroStage() == 15) {
					gameControl.setScreen(NameScene.instance);
					gameControl.setToBeNamed("PLAYER");
				}
			} else if (gameControl.getScene() == BattleMessageScene.instance
					|| gameControl.getScene() == NameScene.instance) {
				gameControl.setScreen(WorldScene.instance);
			}
		} else if (gameControl.getCurrentMessage() == null) {
			switch (gameControl.getScene().getId()) {
			case 0:
				actionPerformers.get(0).keyPress(keyCode, gameControl);
				break;
			case 1:
				actionPerformers.get(1).keyPress(keyCode, gameControl);
				break;
			case 2: // intro screen, advance oak's text
				actionPerformers.get(2).keyPress(keyCode, gameControl);
				break;
			case 3:// name screen, add or remove chars
				actionPerformers.get(3).keyPress(keyCode, gameControl);
				break;
			case 4:
				actionPerformers.get(4).keyPress(keyCode, gameControl);
				break;
			case 5:
				actionPerformers.get(5).keyPress(keyCode, gameControl);
				break;
			case 6:
				actionPerformers.get(6).keyPress(keyCode, gameControl);
				break;
			case 7:
				actionPerformers.get(7).keyPress(keyCode, gameControl);
				break;
			case 8:
				actionPerformers.get(8).keyPress(keyCode, gameControl);
				break;
			case 9:
				actionPerformers.get(9).keyPress(keyCode, gameControl);
				break;
			case 10:
			case 11:
				handleMenuEvent(keyCode);
				break;
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
				handleBattleEvent(keyCode);
				break;
			case 17:
				// TODO - implement message queue such that VK_X or VK_Z here
				// pops a
				// message to display off of the queue
				// if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				// gameControl.setScreen(SCREEN.WORLD);
				// gameControl.setMovable(true);
				// }
				// break;
				break;
			default:
				// otherwise, fire the eventHandler
				actionPerformers.get(69).keyPress(keyCode, gameControl);
				// handleWorldEvent(keyCode);
			}
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

	/**
	 * handles a battle event: handles fighting key presses
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void handleBattleEvent(int keyCode) {
		switch (gameControl.getScene().getId()) {
		case 0: // temporary does the same thing
		case 1:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(BattleScene.instance);
			}
			break;
		case 2:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(WorldScene.instance);
				gameControl.setMovable(true);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Handles a menu event - bag, options, party, save, etc Pretty much just a
	 * large batch of logic switching to control graphics
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void handleMenuEvent(int keyCode) {
		Scene curScreen = gameControl.getScene();

		if (keyCode == KeyEvent.VK_X) {
			if (curScreen == MenuScene.instance || curScreen == MessageScene.instance) {
				gameControl.setScreen(WorldScene.instance);
			} else {
				gameControl.setScreen(MenuScene.instance);
			}
		}

		if (keyCode == KeyEvent.VK_UP) {
			gameControl.decrementSelection();
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			gameControl.incrementSelection();
		}

		if (keyCode == KeyEvent.VK_ENTER) {
			if (curScreen == MenuScene.instance) {
				gameControl.setScreen(WorldScene.instance);
			}
		}
		if (keyCode == KeyEvent.VK_Z) {
			switch (gameControl.getScene().getId()) {
			case 0:
				if (gameControl.getCurrentSelection() == 0) {
					gameControl.setScreen(PokedexScene.instance);
				}
				if (gameControl.getCurrentSelection() == 1) {
					gameControl.setScreen(PokemonScene.instance);
				}
				if (gameControl.getCurrentSelection() == 2) {
					gameControl.setScreen(BagScene.instance);
				}
				if (gameControl.getCurrentSelection() == 3) {
					gameControl.setScreen(PokegearScene.instance);
				}
				if (gameControl.getCurrentSelection() == 4) {
					gameControl.setScreen(TrainerCardScene.instance);
				}
				if (gameControl.getCurrentSelection() == 5) {
					gameControl.setScreen(SaveScene.instance);
				}
				if (gameControl.getCurrentSelection() == 6) {
					gameControl.setScreen(OptionScene.instance);
				}
				if (gameControl.getCurrentSelection() == 7) {
					gameControl.setScreen(WorldScene.instance);
				}
				break;
			case 1:
				if (gameControl.getCurrentSelection() == 0) {
					// TODO - add Map painting
					DebugUtility.printMessage("Map");
				} else if (gameControl.getCurrentSelection() == 1) {
					// TODO - add Radio painting
					DebugUtility.printMessage("Radio");
				} else if (gameControl.getCurrentSelection() == 2) {
					// TODO - add Phone painting
					DebugUtility.printMessage("Phone");
				} else if (gameControl.getCurrentSelection() == 3) {
					gameControl.setScreen(MenuScene.instance);
				}
				gameControl.setCurrentSelection(0);
				break;
			case 2:
				if (gameControl.getCurrentSelection() == 0) {
					gameControl.saveGame();
					gameControl.setCurrentMessage("Game saved successfully!");
					gameControl.setScreen(MessageScene.instance);
				} else {
					gameControl.setScreen(MenuScene.instance);
				}
				break;
			case 3:
				if (keyCode == KeyEvent.VK_Z) {
					if (gameControl.getCurrentSelection() == 5) {
						gameControl.toggleSound();
					}
				}
			default:
				break;
			}
		}

		// play key press sound effect
		gameControl.playClip(SOUND_EFFECT.SELECT);
	}
}
