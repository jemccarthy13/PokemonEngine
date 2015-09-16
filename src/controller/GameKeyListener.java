package controller;

import graphics.SpriteLibrary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

import model.GameData.SCREEN;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.BattleEngine.TURN;
import utilities.DebugUtility;
import audio.AudioLibrary.SOUND_EFFECT;

public class GameKeyListener implements KeyListener, Serializable {

	private static final long serialVersionUID = 433796777156267003L;

	GameController gameControl;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Listen for key events, using a game controller to perform logic
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameKeyListener(GameController controller) {
		gameControl = controller;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Fire this method any time a key is pressed
	//
	// ////////////////////////////////////////////////////////////////////////
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
		case BATTLE_MESSAGE:
			handleBattleEvent(keyCode);
			break;
		case MESSAGE:
			// TODO - implement message queue such that VK_X or VK_Z here
			// pops a message to display off of the queue
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

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleBattleEvent handles a battle event - gets choices, deals damage
	//
	// ////////////////////////////////////////////////////////////////////////
	private void handleBattleEvent(int keyCode) {
		switch (gameControl.getScreen()) {
		case BATTLE_ITEM:
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
			// TODO - verify move exists at given selection
			// press the arrow keys to change the bit mask of the current
			// selection
			int selX = BattleEngine.getInstance().currentSelectionFightX;
			int selY = BattleEngine.getInstance().currentSelectionFightY;

			switch (keyCode) {
			case KeyEvent.VK_UP:
				selX = 0;
				break;
			case KeyEvent.VK_DOWN:
				selX = 1;
				break;
			case KeyEvent.VK_LEFT:
				selY = 0;
				break;
			case KeyEvent.VK_RIGHT:
				selY = 1;
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

			if (2 * selX + selY <= BattleEngine.getInstance().playerCurrentPokemon.getNumMoves()) {
				BattleEngine.getInstance().currentSelectionFightX = selX;
				BattleEngine.getInstance().currentSelectionFightY = selY;
			}
			// 00 - 0
			// 01 - 2
			// 11 - 3
			// 10 - 1

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
				// TODO - maybe shorten this to "changeToMenu(XX)"
				if ((BattleEngine.getInstance().currentSelectionMainX == 0)
						&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
					gameControl.setScreen(SCREEN.BATTLE_FIGHT);
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 1)
						&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
					gameControl.setScreen(SCREEN.BATTLE_POKEMON);
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 0)
						&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
					gameControl.setScreen(SCREEN.BATTLE_ITEM);
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 1)
						&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
					if (BattleEngine.getInstance().enemyName == null) {
						DebugUtility.printMessage("Got away safely!");
						gameControl.setScreen(SCREEN.WORLD);
					}
				}
			}
			gameControl.playClip(SOUND_EFFECT.SELECT);
			break;
		default:
			break;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getSelectedMove - returns the currently selected move based on
	// user's highlighted choice
	//
	// ////////////////////////////////////////////////////////////////////////
	private int getSelectedMove() {
		int move = -1;
		if ((BattleEngine.getInstance().currentSelectionFightX == 0)
				&& (BattleEngine.getInstance().currentSelectionFightY == 0)) {
			move = 0;
		}
		if ((BattleEngine.getInstance().currentSelectionFightX == 1)
				&& (BattleEngine.getInstance().currentSelectionFightY == 0)) {
			move = 1;
		}
		if ((BattleEngine.getInstance().currentSelectionFightX == 0)
				&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
			move = 2;
		}
		if ((BattleEngine.getInstance().currentSelectionFightX == 1)
				&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
			move = 3;
		}
		return move;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleMenuEvent handles a menu event - bag, options, party, save, etc
	// Pretty much just a large batch of logic switching to control graphics
	//
	// TODO - implement save feature
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleWorldEvent handles all world events - moveing the sprite, pausing
	// interacting with the environment, and conversing with characters
	//
	// TODO - add interactive environment (say, items on the ground, message
	// signs, surfing / water, etc
	// TODO - look into shortening or method-izing the border NPC check
	//
	// ////////////////////////////////////////////////////////////////////////
	private void handleWorldEvent(int keyCode) {
		// match the key to a direction
		DIR toTravel = null;
		boolean moving = true;

		switch (keyCode) {
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
		default:
			// the button pressed wasn't a moving button, so don't do movement
			moving = false;
			toTravel = DIR.SOUTH;
		}

		if (moving) {
			// one of the movement buttons was pressed, so try to move in that
			// direction
			gameControl.setPlayerDirection(toTravel);
			if (gameControl.canMoveInDir(toTravel)) {
				gameControl.setPlayerWalking(true);
			} else {
				gameControl.setPlayerSprite(SpriteLibrary.getSpriteForDir("PLAYER", toTravel));
				gameControl.playClip(SOUND_EFFECT.COLLISION);
			}
		} else if (keyCode == KeyEvent.VK_ENTER) {
			gameControl.playClip(SOUND_EFFECT.MENU);
			gameControl.setScreen(SCREEN.MENU);
		} else if (keyCode == KeyEvent.VK_Z) {
			gameControl.playClip(SOUND_EFFECT.SELECT);
			// overhead cost for following logic
			Player player = gameControl.getPlayer();
			DIR playerDir = player.getDirection();
			int playerCurX = player.getCurrentX();
			int playerCurY = player.getCurrentY();
			for (Actor curNPC : NPCLibrary.getInstance().values()) {
				int NPC_X = curNPC.getCurrentX();
				int NPC_Y = curNPC.getCurrentY();
				if (playerDir == DIR.WEST) {
					if ((playerCurX - 1 == NPC_X) && (playerCurY == NPC_Y)) {
						curNPC.setDirectionOpposite(playerDir);
						gameControl.setCurNPC(curNPC);
					}
				} else if (playerDir == DIR.NORTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y + 1)) {
						curNPC.setDirectionOpposite(playerDir);
						gameControl.setCurNPC(curNPC);
					}
				} else if (playerDir == DIR.EAST) {
					if ((playerCurX == NPC_X - 1) && (playerCurY == NPC_Y)) {
						curNPC.setDirectionOpposite(playerDir);
						gameControl.setCurNPC(curNPC);
					}
				} else if (playerDir == DIR.SOUTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y - 1)) {
						curNPC.setDirectionOpposite(playerDir);
						gameControl.setCurNPC(curNPC);
					}
				}
			}
			if (gameControl.getCurNPC() != null) {
				gameControl.setScreen(SCREEN.MESSAGE);
				gameControl.getCurNPC().setStationary(true);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Handle adding & removing characters to a temporary buffer
	//
	// ////////////////////////////////////////////////////////////////////////
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
