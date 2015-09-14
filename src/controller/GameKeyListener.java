package controller;

import graphics.SpriteLibrary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.Random;

import model.GameData.SCREEN;
import party.MoveData;
import party.PartyMember;
import party.PartyMember.STATS;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.DebugUtility;
import utilities.RandomNumUtils;
import audio.AudioLibrary.SOUND_EFFECT;

public class GameKeyListener implements KeyListener, Serializable {

	GameController gameControl;

	public GameKeyListener(GameController controller) {
		gameControl = controller;
	}

	private static final long serialVersionUID = 433796777156267003L;

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
			if (keyCode == KeyEvent.VK_X)
				gameControl.removeChar();
			if ((keyCode == KeyEvent.VK_Z)) {
				if (gameControl.getRowSelection() == 5) {
					// check for end or del
					if (gameControl.getColSelection() == 1 && gameControl.getChosenName().length() > 0) {
						gameControl.getPlayer().setName(gameControl.getChosenName());
						gameControl.reset();
						gameControl.setScreen(SCREEN.INTRO);
					} else if (gameControl.getColSelection() == 0)
						gameControl.removeChar();
				} else {
					gameControl.addSelectedChar();
				}
			}
			if (keyCode == KeyEvent.VK_DOWN && gameControl.getRowSelection() < 5) {
				gameControl.incrRowSelection();
				if (gameControl.getRowSelection() == 5 && gameControl.getColSelection() < 3)
					gameControl.setColSelection(0);
				if (gameControl.getRowSelection() == 5 && gameControl.getColSelection() >= 3)
					gameControl.setColSelection(1);
			} else if (keyCode == KeyEvent.VK_UP && gameControl.getRowSelection() > 0) {
				gameControl.decrRowSelection();
			} else if (keyCode == KeyEvent.VK_LEFT && gameControl.getColSelection() > 0) {
				gameControl.decrColSelection();
			} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getRowSelection() == 5
					&& gameControl.getColSelection() < 1) {
				gameControl.incrColSelection();
			} else if (keyCode == KeyEvent.VK_RIGHT && gameControl.getRowSelection() < 5
					&& gameControl.getColSelection() < 5) {
				gameControl.incrColSelection();
			}
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
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				// TODO messages with stages
				gameControl.setScreen(SCREEN.WORLD);
				gameControl.setMovable(true);
			}
			break;
		default:
			// otherwise, fire the eventHandler
			if (gameControl.isMovable() && !gameControl.isInBattle() && !gameControl.isPlayerWalking()) {
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
	public void handleBattleEvent(int keyCode) {
		switch (gameControl.getScreen()) {
		case BATTLE_ITEM:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(SCREEN.BATTLE);
			}
			break;
		case BATTLE_MESSAGE:
			if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
				gameControl.setScreen(SCREEN.WORLD);
				BattleEngine.getInstance().inPokemon = false;
				gameControl.setMovable(true);
			}
			break;
		case BATTLE_FIGHT:
			// at move selection menu
			PartyMember playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
			if (keyCode == KeyEvent.VK_X) {
				gameControl.setScreen(SCREEN.BATTLE);
			} else if (keyCode == KeyEvent.VK_UP) {
				BattleEngine.getInstance().currentSelectionFightY = 0;
			} else if (keyCode == KeyEvent.VK_DOWN) {
				BattleEngine.getInstance().currentSelectionFightY = 1;
			} else if (keyCode == KeyEvent.VK_LEFT) {
				BattleEngine.getInstance().currentSelectionFightX = 0;
			} else if (keyCode == KeyEvent.VK_RIGHT) {
				BattleEngine.getInstance().currentSelectionFightX = 1;
			} else if (keyCode == KeyEvent.VK_Z) {

				playerPokemon.tryToThaw();

				// get Player's move if not FZN or SLP
				// TODO convert to use BATTLE_STATUS enum
				int move = -1;
				move = ((playerPokemon.getStatusEffect() != 4) || (playerPokemon.getStatusEffect() != 5)) ? getSelectedMove(move)
						: -1;

				boolean checkPar = (playerPokemon.getStatusEffect() == 1) ? true : false;
				evaluateAndDealDamage(checkPar, playerPokemon, move);

				if (playerPokemon.getStatusEffect() == 2) { // burned
					playerPokemon.doDamage(2);
					gameControl.playClip(SOUND_EFFECT.DAMAGE);
					// TODO - convert to use message box
					DebugUtility.printMessage(playerPokemon.getName() + " has been hurt by its burn");
				} else if (playerPokemon.getStatusEffect() == 3) { // PSN
					playerPokemon.doDamage(2);
					gameControl.playClip(SOUND_EFFECT.DAMAGE);
					// TODO - convert to use message box
					DebugUtility.printMessage(playerPokemon.getName() + " has been hurt by its poison");
				}
				gameControl.setScreen(SCREEN.BATTLE);
				BattleEngine.getInstance().playerTurn = false;
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
				// TODO - maybe shorten this to "changeToMenu(XX)"
				if ((BattleEngine.getInstance().currentSelectionMainX == 0)
						&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
					gameControl.setScreen(SCREEN.BATTLE_FIGHT);
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 1)
						&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
					BattleEngine.getInstance().inPokemonMenu();
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 0)
						&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
					BattleEngine.getInstance().inItemMenu();
				}
				if ((BattleEngine.getInstance().currentSelectionMainX == 1)
						&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
					BattleEngine.getInstance().Run();
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
	private int getSelectedMove(int move) {
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
	// evaluateAndDealDamage - based on the seleted move and whether or not
	// the pokemon was parlyzed, calculate the damage and deal it
	//
	// TODO - make this usable by the enemy as well. Possibly move to
	// BattleEngine
	// ////////////////////////////////////////////////////////////////////////
	private void evaluateAndDealDamage(boolean checkPar, PartyMember playerPokemon, int move) {
		Random r = new Random();
		boolean par = (checkPar) ? r.nextInt(2) < 0 : false;
		if (!par) {
			MoveData chosen = playerPokemon.getMove(move);

			int attackStat = 0;
			int defStat = 0;

			if (chosen.isStat) {
				// TODO - logic for stat based moves
				attackStat = playerPokemon.getStat(STATS.SP_ATTACK);
				defStat = BattleEngine.getInstance().enemyPokemon.get(0).getStat(STATS.SP_DEFENSE);
			} else {
				// calculate and deal physical damage
				attackStat = playerPokemon.getStat(STATS.ATTACK);
				defStat = BattleEngine.getInstance().enemyPokemon.get(0).getStat(STATS.DEFENSE);
				int damage = (int) (((2 * playerPokemon.getLevel() / 5 + 2) * chosen.power * attackStat / defStat / 50 + 2)
						* RandomNumUtils.generateRandom(85, 100) / 100.0);
				((PartyMember) BattleEngine.getInstance().enemyPokemon.get(0)).doDamage(damage);

				gameControl.playClip(SOUND_EFFECT.DAMAGE);
			}
			chosen.movePP--;
		} else {
			DebugUtility.printMessage(playerPokemon.getName() + " is paralyzed. It can't move.");
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleMenuEvent handles a menu event - bag, options, party, save, etc
	// Pretty much just a large batch of logic switching to control graphics
	//
	// TODO - implement save feature
	//
	// ////////////////////////////////////////////////////////////////////////
	public void handleMenuEvent(int keyCode) {
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
	public void handleWorldEvent(int keyCode) {
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
}
