package driver;

import java.awt.event.KeyEvent;
import java.util.Random;

import audio.AudioLibrary;
import graphics.BattleEngine;
import graphics.SpriteLibrary;
import pokedex.MoveData;
import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// EventHandler handles all input events, menu changes, logic switching 
// and battle actions
//
// TODO - implement Bag, pokegear, party, save, options, and pokedex events
//
// ////////////////////////////////////////////////////////////////////////
public class EventHandler {

	Game game;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor for EventHandler. Makes a "pointer" to the Game
	// so it has all the logic references
	//
	// ////////////////////////////////////////////////////////////////////////
	public EventHandler(Game theGame) {
		game = theGame;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleBattleEvent handles a battle event - gets choices, deals damage
	//
	// ////////////////////////////////////////////////////////////////////////
	void handleBattleEvent(int keyCode) {
		if (BattleEngine.getInstance().inFight) {
			// at move selection menu
			Pokemon playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
			if (BattleEngine.getInstance().playerTurn) {
				if (keyCode == KeyEvent.VK_UP) {
					BattleEngine.getInstance().currentSelectionFightY = 0;
				} else if (keyCode == KeyEvent.VK_DOWN) {
					BattleEngine.getInstance().currentSelectionFightY = 1;
				} else if (keyCode == KeyEvent.VK_LEFT) {
					BattleEngine.getInstance().currentSelectionFightX = 0;
				} else if (keyCode == KeyEvent.VK_RIGHT) {
					BattleEngine.getInstance().currentSelectionFightX = 1;
				} else if (keyCode == KeyEvent.VK_Z) {

					checkIfThawed(playerPokemon);

					// get Player's move if not FZN or PAR
					int move = -1;
					move = ((playerPokemon.statusEffect != 4) || (playerPokemon.statusEffect != 5))
							? getSelectedMove(move) : -1;

					boolean checkPar = (playerPokemon.statusEffect == 1) ? true : false;
					evaluateAndDealDamage(checkPar, playerPokemon, move);

					if (playerPokemon.statusEffect == 2) { // burned
						playerPokemon.doDamage(2);
						AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE,
								this.game.gData.option_sound);
						System.out.println(playerPokemon.getName() + " has been hurt by its burn");
					} else if (playerPokemon.statusEffect == 3) { // PSN
						playerPokemon.doDamage(2);
						AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE,
								this.game.gData.option_sound);
						System.out.println(playerPokemon.getName() + " has been hurt by its poison");
					}
					resetBattleVars();
					BattleEngine.getInstance().playerTurn = false;
				}
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, game.gData.option_sound);
			}
			if (keyCode == KeyEvent.VK_X) {
				// cancel == exit to main battle menu
				resetBattleVars();
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, game.gData.option_sound);
			}
		}
		if (BattleEngine.getInstance().inMain) {
			// at main battle menu
			if (BattleEngine.getInstance().playerTurn) {
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
						BattleEngine.getInstance().inFightMenu();
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
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, game.gData.option_sound);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// checkIfThawed - a random proability of being woken or thawed during
	// a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	private void checkIfThawed(Pokemon p) {
		Random rr = new Random();
		int wakeupthaw = rr.nextInt(5);
		if (wakeupthaw <= 1) {
			if (p.statusEffect == 4) {
				System.out.println(p.getName() + " has woken up.");
			}
			if (p.statusEffect == 5) {
				System.out.println(p.getName() + " has broken free from the ice.");
			}
			p.statusEffect = 0;
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
	private void evaluateAndDealDamage(boolean checkPar, Pokemon playerPokemon, int move) {
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
				int damage = (int) (((2 * playerPokemon.getLevel() / 5 + 2) * chosen.power * attackStat / defStat / 50
						+ 2) * RandomNumUtils.generateRandom(85, 100) / 100.0);
				((Pokemon) BattleEngine.getInstance().enemyPokemon.get(0)).doDamage(damage);
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_DAMAGE, this.game.gData.option_sound);
			}
			chosen.movePP--;
		} else {
			System.out.println(playerPokemon.getName() + " is paralyzed. It can't move.");
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// resetBattleVars upon Return to main battle menu, or exiting a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	private void resetBattleVars() {
		BattleEngine.getInstance().inMain = true;
		BattleEngine.getInstance().inFight = false;
		BattleEngine.getInstance().currentSelectionMainX = 0;
		BattleEngine.getInstance().currentSelectionMainY = 0;
		BattleEngine.getInstance().currentSelectionFightX = 0;
		BattleEngine.getInstance().currentSelectionFightY = 0;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleMenuEvent handles a menu event - bag, options, party, save, etc
	// Pretty much just a large batch of logic switching to control graphics
	//
	// TODO - implement save feature
	//
	// ////////////////////////////////////////////////////////////////////////
	void handleMenuEvent(int keyCode) {
		if (game.menuScreen.MENU_inConversation) {
			// exit a conversation
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.Exit();
				game.gData.inMenu = false;
			}
		} else if (game.menuScreen.MENU_inMain) {
			// main pause menu screen
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.MENU_currentSelectionMain > 0) {
					game.menuScreen.MENU_currentSelectionMain -= 1;
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.MENU_currentSelectionMain < 7) {
					game.menuScreen.MENU_currentSelectionMain += 1;
				}
			} else if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.MENU_currentSelectionMain == 0) {
					game.menuScreen.PokeDex();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 1) {
					game.menuScreen.Pokemon();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 2) {
					game.menuScreen.Bag();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 3) {
					game.menuScreen.PokeGear();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 4) {
					game.menuScreen.TrainerCard();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 5) {
					game.menuScreen.Save();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 6) {
					game.menuScreen.Option();
				}
				if (game.menuScreen.MENU_currentSelectionMain == 7) {
					game.menuScreen.Exit();
					game.gData.inMenu = false;
				}
			} else if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.Exit();
				game.gData.inMenu = false;
			}

		} else if (game.menuScreen.MENU_inPokeDex) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inPokeDex = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inPokemon) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inPokemon = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inBag) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inBag = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inPokeGear) {
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.MENU_currentSelectionPokeGear > 0) {
					game.menuScreen.MENU_currentSelectionPokeGear -= 1;
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.MENU_currentSelectionPokeGear < 3) {
					game.menuScreen.MENU_currentSelectionPokeGear += 1;
				}
			} else if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.MENU_currentSelectionPokeGear == 0) {
					System.out.println("Map");
				} else if (game.menuScreen.MENU_currentSelectionPokeGear == 1) {
					System.out.println("Radio");
				} else if (game.menuScreen.MENU_currentSelectionPokeGear == 2) {
					System.out.println("Phone");
				} else if (game.menuScreen.MENU_currentSelectionPokeGear == 3) {
					System.out.println("Exit");
					game.menuScreen.MENU_currentSelectionPokeGear = 0;
					game.menuScreen.MENU_inPokeGear = false;
					game.menuScreen.MENU_inMain = true;
				}
			} else if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inPokeGear = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inTrainerCard) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inTrainerCard = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inSave) {
			if (keyCode == KeyEvent.VK_UP) {
				game.menuScreen.MENU_currentSelectionSave = 0;
			} else if (keyCode == KeyEvent.VK_DOWN) {
				game.menuScreen.MENU_currentSelectionSave = 1;
			} else if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.MENU_currentSelectionSave == 0) {
					// Utils.saveGame(game.gold);
					System.out.println(game.gData.player.getName() + "'s Game has been saved! (Not really though)");
				} else {
					game.menuScreen.MENU_inSave = false;
					game.menuScreen.MENU_inMain = true;
				}
			} else if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inSave = false;
				game.menuScreen.MENU_inMain = true;
			}
		} else if (game.menuScreen.MENU_inOption) {
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.MENU_currentSelectionOption > 0) {
					game.menuScreen.MENU_currentSelectionOption -= 1;
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.MENU_currentSelectionOption < 5) {
					game.menuScreen.MENU_currentSelectionOption += 1;
				}
			} else if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.MENU_currentSelectionOption == 5) {
					game.gData.option_sound = !game.gData.option_sound;
					if (game.gData.option_sound) {
						AudioLibrary.getInstance().playBackgroundMusic(game.gData.player.getCurLoc().getName(),
								game.gData.option_sound);
					} else {
						AudioLibrary.getInstance().pauseBackgrondMusic();
					}
				}
			} else if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.MENU_inOption = false;
				game.menuScreen.MENU_inMain = true;
			}
		}

		AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, game.gData.option_sound);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// handleWorldEvent handles all world events - moveing the sprite, pausing
	// interacting with the environment, and conversing with characters
	//
	// TODO - add interactive environment (say, items on the ground, message
	// signs, surfing / water, etc
	// TODO - look into shortening or method-izing the arrow key world events
	// TODO - look into shortening or method-izing the border NPC check
	//
	// ////////////////////////////////////////////////////////////////////////
	void handleWorldEvent(int keyCode) {
		if (keyCode == KeyEvent.VK_UP) {
			game.gData.player.setDir(DIR.NORTH);
			if (game.moveable_dir[game.gData.player.getDir().ordinal()]) {
				game.walking = true;
			} else {
				game.gData.player.setSprite(SpriteLibrary.getInstance().getSprites("PLAYER").get(9));
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_COLLISION, game.gData.option_sound);
			}
		} else if (keyCode == KeyEvent.VK_DOWN) {
			game.gData.player.setDir(DIR.SOUTH);
			if (game.moveable_dir[game.gData.player.getDir().ordinal()]) {
				game.walking = true;
			} else {
				game.gData.player.setSprite(SpriteLibrary.getInstance().getSprites("PLAYER").get(0));
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_COLLISION, game.gData.option_sound);
			}
		} else if (keyCode == KeyEvent.VK_LEFT) {
			game.gData.player.setDir(DIR.WEST);
			if (game.moveable_dir[game.gData.player.getDir().ordinal()]) {
				game.walking = true;
			} else {
				game.gData.player.setSprite(SpriteLibrary.getInstance().getSprites("PLAYER").get(3));
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_COLLISION, game.gData.option_sound);
			}
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			game.gData.player.setDir(DIR.EAST);
			if (game.moveable_dir[game.gData.player.getDir().ordinal()]) {
				game.walking = true;
			} else {
				game.gData.player.setSprite(SpriteLibrary.getInstance().getSprites("PLAYER").get(6));
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_COLLISION, game.gData.option_sound);
			}
		} else if (keyCode == KeyEvent.VK_ENTER) {
			AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_MENU, game.gData.option_sound);
			game.menuScreen.MENU_inMain = true;
			game.gData.inMenu = true;
		} else if (keyCode == KeyEvent.VK_Z) {
			AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT, game.gData.option_sound);
			Actor borderNPC = null;
			// overhead cost for following logic
			DIR playerDir = game.gData.player.getDir();
			int playerCurX = game.gData.player.getCurrentX();
			int playerCurY = game.gData.player.getCurrentY();
			for (Actor curNPC : NPCLibrary.getInstance().values()) {
				int NPC_X = curNPC.getCurrentX();
				int NPC_Y = curNPC.getCurrentY();
				if (playerDir == DIR.WEST) {
					if ((NPC_X == playerCurX - 1) && (playerCurY == NPC_Y)) {
						curNPC.setSpriteFacing(DIR.EAST);
						borderNPC = curNPC;
					}
				} else if (playerDir == DIR.NORTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y + 1)) {
						curNPC.setSpriteFacing(DIR.SOUTH);
						borderNPC = curNPC;
					}
				} else if (playerDir == DIR.EAST) {
					if ((playerCurX == NPC_X - 1) && (playerCurY == NPC_Y)) {
						curNPC.setSpriteFacing(DIR.WEST);
						borderNPC = curNPC;
					}
				} else if (playerDir == DIR.SOUTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y - 1)) {
						curNPC.setSpriteFacing(DIR.NORTH);
						borderNPC = curNPC;
					}
				}
			}
			if (borderNPC != null) {
				game.gData.inMenu = true;
				game.menuScreen.Message(borderNPC);
			}
		}
	}
}
