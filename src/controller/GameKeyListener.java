package controller;

import graphics.SpriteLibrary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import model.Coordinate;
import model.GameData.SCREEN;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.BattleEngine.TURN;
import utilities.DebugUtility;
import audio.AudioLibrary.SOUND_EFFECT;

/**
 * Listens for keypresses in the game
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
	 * Constructor given a game controller to use to perform logic
	 * 
	 * @param controller
	 */
	public GameKeyListener(GameController controller) {
		gameControl = controller;
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
		switch (gameControl.getScreen()) {
		case TITLE: // title screen "press enter"
			if (keyCode == KeyEvent.VK_ENTER) {
				gameControl.playBackgroundMusic("Continue");
				gameControl.setScreen(SCREEN.CONTINUE);
			}
			break;
		case CONTINUE: // continue screen choice select
			if (keyCode == KeyEvent.VK_UP) {
				if (gameControl.getCurrentSelection() > 0)
					gameControl.decrementSelection();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (gameControl.getCurrentSelection() < 2)
					gameControl.incrementSelection();
			}
			if (keyCode == KeyEvent.VK_Z) {
				if (gameControl.getCurrentSelection() == 0) {
					gameControl.startGame(true);
				} else if (gameControl.getCurrentSelection() == 1) {
					gameControl.startGame(false);
				}
			}
			gameControl.playClip(SOUND_EFFECT.SELECT);
			break;
		case INTRO: // intro screen, advance oak's text
			if (keyCode == KeyEvent.VK_Z) {
				gameControl.incrIntroStage();
				if (gameControl.getIntroStage() > NPCLibrary.getInstance().get("Professor Oak").getTextLength() - 1) {
					gameControl.playBackgroundMusic("NewBarkTown");
					gameControl.setScreen(SCREEN.WORLD);
				} else if (gameControl.getIntroStage() == 15) {
					gameControl.setScreen(SCREEN.NAME);
					gameControl.setToBeNamed("PLAYER");
				}
			}
			break;
		case NAME:// name screen, add or remove chars
			handleNameEvent(keyCode);
			break;
		case POKEMON:
		case BAG:
		case SAVE:
		case OPTION:
		case POKEDEX:
		case POKEGEAR:
		case TRAINERCARD:
		case MENU:
			handleMenuEvent(keyCode);
			break;
		case BATTLE:
		case BATTLE_FIGHT:
		case BATTLE_ITEM:
		case BATTLE_POKEMON:
		case BATTLE_MESSAGE:
			handleBattleEvent(keyCode);
			break;
		case MESSAGE:
			// TODO - implement message queue such that VK_X or VK_Z here pops a
			// message to display off of the queue
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(SCREEN.WORLD);
				gameControl.setMovable(true);
			}
			break;
		default:
			// otherwise, fire the eventHandler
			if (gameControl.isMovable() && !gameControl.isPlayerWalking()) {
				handleWorldEvent(keyCode);
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
		switch (gameControl.getScreen()) {
		case BATTLE_ITEM: // temporary does the same thing
		case BATTLE_POKEMON:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(SCREEN.BATTLE);
			}
			break;
		case BATTLE_MESSAGE:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(SCREEN.WORLD);
				gameControl.setMovable(true);
			}
			break;
		case BATTLE_FIGHT:
			// at move selection menu
			int selX = BattleEngine.getInstance().currentSelectionFightX;
			int selY = BattleEngine.getInstance().currentSelectionFightY;

			switch (keyCode) {
			case KeyEvent.VK_UP:
				selY = 0;
				break;
			case KeyEvent.VK_DOWN:
				selY = 1;
				break;
			case KeyEvent.VK_LEFT:
				selX = 0;
				break;
			case KeyEvent.VK_RIGHT:
				selX = 1;
				break;
			case KeyEvent.VK_X:
				gameControl.setScreen(SCREEN.BATTLE);
				break;
			case KeyEvent.VK_Z:
				int move = getSelectedMove();
				BattleEngine.getInstance().takeTurn(TURN.PLAYER, move);
				BattleEngine.getInstance().enemyTurn();
				break;
			}

			// make sure the move exists before we move the arrow there
			double x = Math.pow(2, selX);
			if (x + selY <= BattleEngine.getInstance().playerCurrentPokemon.getNumMoves() - 1) {
				BattleEngine.getInstance().currentSelectionFightX = selX;
				BattleEngine.getInstance().currentSelectionFightY = selY;
			}

			// play sound when any button is pressed
			gameControl.playClip(SOUND_EFFECT.SELECT);

			break;
		case BATTLE:
			if (keyCode == KeyEvent.VK_UP) {
				BattleEngine.getInstance().currentSelectionMainY = 0;
			} else if (keyCode == KeyEvent.VK_DOWN) {
				BattleEngine.getInstance().currentSelectionMainY = 1;
			} else if (keyCode == KeyEvent.VK_LEFT) {
				BattleEngine.getInstance().currentSelectionMainX = 0;
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				BattleEngine.getInstance().currentSelectionMainX = 1;
			}
			if (keyCode == KeyEvent.VK_Z) {
				// do logic based on current selection
				switch ((int) Math.pow(2, BattleEngine.getInstance().currentSelectionMainY)
						+ BattleEngine.getInstance().currentSelectionMainX) {
				case 0:
					gameControl.setScreen(SCREEN.BATTLE_FIGHT);
					break;
				case 1:
					gameControl.setScreen(SCREEN.BATTLE_POKEMON);
					break;
				case 2:
					gameControl.setScreen(SCREEN.BATTLE_ITEM);
					break;
				case 3:
					if (BattleEngine.getInstance().enemyName == null) {
						gameControl.setCurrentMessage("Got away safely!");
						gameControl.setScreen(SCREEN.MESSAGE);
					} else {
						gameControl.setCurrentMessage("Can't run away from a trainer!");
						gameControl.setScreen(SCREEN.BATTLE_MESSAGE);
						// TODO BATTLE_MESSAGE should only quit battle when
						// someone is out of pokemon
						// otherwise, resume the battle (like this message-
						// non-fatal message during battle)
					}
					break;
				}
			}
			gameControl.playClip(SOUND_EFFECT.SELECT);
			break;
		default:
			break;
		}
	}

	/**
	 * Return the currently selected mvoe based on the user's highlighted choice
	 * 
	 * 2^y + x is the conversion from X,Y to move number
	 * 
	 * @return the number of the selected move
	 */
	private int getSelectedMove() {
		int move = (int) Math.pow(2, BattleEngine.getInstance().currentSelectionFightY);
		move += BattleEngine.getInstance().currentSelectionFightX;
		return move;
	}

	/**
	 * Handles a menu event - bag, options, party, save, etc Pretty much just a
	 * large batch of logic switching to control graphics
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void handleMenuEvent(int keyCode) {
		SCREEN curScreen = gameControl.getScreen();

		if (keyCode == KeyEvent.VK_X) {
			if (curScreen == SCREEN.MENU || curScreen == SCREEN.MESSAGE) {
				gameControl.setScreen(SCREEN.WORLD);
			} else {
				gameControl.setScreen(SCREEN.MENU);
			}
		}

		if (keyCode == KeyEvent.VK_UP) {
			gameControl.decrementSelection();
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			gameControl.incrementSelection();
		}

		if (keyCode == KeyEvent.VK_ENTER) {
			if (curScreen == SCREEN.MENU) {
				gameControl.setScreen(SCREEN.WORLD);
			}
		}
		if (keyCode == KeyEvent.VK_Z) {
			switch (gameControl.getScreen()) {
			case MENU:
				if (gameControl.getCurrentSelection() == 0) {
					gameControl.setScreen(SCREEN.POKEDEX);
				}
				if (gameControl.getCurrentSelection() == 1) {
					gameControl.setScreen(SCREEN.POKEMON);
				}
				if (gameControl.getCurrentSelection() == 2) {
					gameControl.setScreen(SCREEN.BAG);
				}
				if (gameControl.getCurrentSelection() == 3) {
					gameControl.setScreen(SCREEN.POKEGEAR);
				}
				if (gameControl.getCurrentSelection() == 4) {
					gameControl.setScreen(SCREEN.TRAINERCARD);
				}
				if (gameControl.getCurrentSelection() == 5) {
					gameControl.setScreen(SCREEN.SAVE);
				}
				if (gameControl.getCurrentSelection() == 6) {
					gameControl.setScreen(SCREEN.OPTION);
				}
				if (gameControl.getCurrentSelection() == 7) {
					gameControl.setScreen(SCREEN.WORLD);
				}
				break;
			case POKEGEAR:
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
					gameControl.setScreen(SCREEN.MENU);
				}
				gameControl.setCurrentSelection(0);
				break;
			case SAVE:
				if (gameControl.getCurrentSelection() == 0) {
					gameControl.saveGame();
					gameControl.setCurrentMessage("Game saved successfully!");
					gameControl.setScreen(SCREEN.MESSAGE);
				} else {
					gameControl.setScreen(SCREEN.MENU);
				}
				break;
			case OPTION:
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

	/**
	 * Check cur NPC coordinate w/ an adjusted coordinate & player direction.
	 * 
	 * @param curNpc
	 *            - the NPC to check against
	 * @param c
	 *            - the coordinate of the player
	 * @param playerDir
	 *            - the direction the player is facing
	 * @param keyCode
	 *            - the key code of the key pressed
	 */
	public void tryBorderNPC(Actor curNpc, Coordinate c, DIR playerDir) {
		Coordinate c1 = curNpc.getPosition();
		if (c1.equals(c)) {
			curNpc.setDirectionOpposite(playerDir);
			gameControl.setCurNPC(curNpc);
			gameControl.setCurrentMessage(curNpc.getConversationText().get(0));
			gameControl.setScreen(SCREEN.MESSAGE);
			gameControl.getCurNPC().setStationary(true);
		}
	}

	/**
	 * Handles all world events - moving the sprite, pausing interacting with
	 * the environment, and conversing with characters
	 * 
	 * TODO - add interactive environment (items on the ground, message signs,
	 * surfing / water, etc)
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void handleWorldEvent(int keyCode) {
		// match the key to a direction
		DIR toTravel = null;
		switch (keyCode) {
		case KeyEvent.VK_ENTER:
			gameControl.playClip(SOUND_EFFECT.MENU);
			gameControl.setScreen(SCREEN.MENU);
			break;
		case KeyEvent.VK_UP:
			toTravel = DIR.NORTH;
			break;
		case KeyEvent.VK_DOWN:
			toTravel = DIR.SOUTH;
			break;
		case KeyEvent.VK_LEFT:
			toTravel = DIR.WEST;
			break;
		case KeyEvent.VK_RIGHT:
			toTravel = DIR.EAST;
			break;
		case KeyEvent.VK_Z:
			gameControl.playClip(SOUND_EFFECT.SELECT);
			// overhead cost for following logic
			Player player = gameControl.getPlayer();
			DIR playerDir = player.getDirection();
			for (Actor curNPC : NPCLibrary.getInstance().values()) {
				if (playerDir == DIR.WEST) {
					tryBorderNPC(curNPC, new Coordinate(player.getCurrentX() - 1, player.getCurrentY()), playerDir);
				} else if (playerDir == DIR.NORTH) {
					tryBorderNPC(curNPC, new Coordinate(player.getCurrentX(), player.getCurrentY() - 1), playerDir);
				} else if (playerDir == DIR.EAST) {
					tryBorderNPC(curNPC, new Coordinate(player.getCurrentX() + 1, player.getCurrentY()), playerDir);
				} else if (playerDir == DIR.SOUTH) {
					tryBorderNPC(curNPC, new Coordinate(player.getCurrentX(), player.getCurrentY() + 1), playerDir);
				}
			}
			break;
		default:
			break;
		}

		if (toTravel != null) {
			// one of the movement buttons was pressed, so try to move in that
			// direction
			gameControl.setPlayerDirection(toTravel);
			if (gameControl.canMoveInDir(toTravel)) {
				gameControl.setPlayerWalking(true);
			} else {
				gameControl.setPlayerSprite(SpriteLibrary.getSpriteForDir("PLAYER", toTravel));
				gameControl.playClip(SOUND_EFFECT.COLLISION);
			}
		} else if (keyCode == KeyEvent.VK_Z) {

		}
	}

	/**
	 * Handles a name screen event (adding / removing characters, custom names
	 * of PartyMembers)
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void handleNameEvent(int keyCode) {
		if (keyCode == KeyEvent.VK_X)
			gameControl.removeChar();
		if ((keyCode == KeyEvent.VK_Z)) {
			if (gameControl.getNameRowSelection() == 5) {
				// check for end or del
				if (gameControl.getNameColSelection() == 1 && gameControl.getChosenName().length() > 0) {
					gameControl.getPlayer().setName(gameControl.getChosenName());
					gameControl.resetNameBuilder();
					gameControl.setScreen(SCREEN.INTRO);
				} else if (gameControl.getNameColSelection() == 0)
					gameControl.removeChar();
			} else {
				gameControl.addSelectedChar();
			}
		}
		if (keyCode == KeyEvent.VK_DOWN && gameControl.getNameRowSelection() < 5) {
			gameControl.incrNameRowSelection();
			if (gameControl.getNameRowSelection() == 5 && gameControl.getNameColSelection() < 3)
				gameControl.setNameColSelection(0);
			if (gameControl.getNameRowSelection() == 5 && gameControl.getNameColSelection() >= 3)
				gameControl.setNameColSelection(1);
		} else if (keyCode == KeyEvent.VK_UP && gameControl.getNameRowSelection() > 0) {
			gameControl.decrNameRowSelection();
		} else if (keyCode == KeyEvent.VK_LEFT && gameControl.getNameColSelection() > 0) {
			gameControl.decrNameColSelection();
		} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getNameRowSelection() == 5
				&& gameControl.getNameColSelection() < 1) {
			gameControl.incrNameColSelection();
		} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getNameRowSelection() < 5
				&& gameControl.getNameColSelection() < 5) {
			gameControl.incrNameColSelection();
		}
	}
}
