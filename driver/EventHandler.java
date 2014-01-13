package driver;

import graphics.NPC;

import java.awt.event.KeyEvent;
import java.util.Random;

import utilities.EnumsAndConstants;
import utilities.Utils;
import data_structures.Move;
import data_structures.Pokemon;

public class EventHandler {

	Main game;

	public EventHandler(Main theGame) {
		game = theGame;
	}

	void handleBattleEvent(int keyCode) {
		if (game.encounter.inFight) {
			Pokemon playerPokemon = game.encounter.playerPokemon;
			if (game.encounter.playerTurn) {
				if (keyCode == KeyEvent.VK_UP) {
					game.encounter.currentSelectionFightY = 0;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_DOWN) {
					game.encounter.currentSelectionFightY = 1;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_LEFT) {
					game.encounter.currentSelectionFightX = 0;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_RIGHT) {
					game.encounter.currentSelectionFightX = 1;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_Z) {

					checkIfThawed(playerPokemon);

					// get Player's move if not FZN or PAR
					int move = -1;
					move = ((playerPokemon.statusEffect != 4) || (playerPokemon.statusEffect != 5)) ? getSelectedMove(move)
							: -1;

					boolean checkPar = (playerPokemon.statusEffect == 1) ? true : false;
					evaluateAndDealDamage(checkPar, playerPokemon, move);

					if (playerPokemon.statusEffect == 2) { // burned
						playerPokemon.doDamage(2);
						System.out.println(playerPokemon.getName() + " has been hurt by its burn");
					} else if (playerPokemon.statusEffect == 3) { // PSN
						playerPokemon.doDamage(2);
						System.out.println(playerPokemon.getName() + " has been hurt by its poison");
					}
					resetBattleVars();
				} else {
					System.out.println("Can't Attack");
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_X) {
				resetBattleVars();
				Utils.playSelectSound();
			}
		}
		if (game.encounter.inMain) {
			if (game.encounter.playerTurn) {
				if (keyCode == KeyEvent.VK_UP) {
					game.encounter.currentSelectionMainY = 0;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_DOWN) {
					game.encounter.currentSelectionMainY = 1;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_LEFT) {
					game.encounter.currentSelectionMainX = 0;
					Utils.playSelectSound();
				} else if (keyCode == KeyEvent.VK_RIGHT) {
					game.encounter.currentSelectionMainX = 1;
					Utils.playSelectSound();
				}
				if (keyCode == KeyEvent.VK_Z) {
					if ((game.encounter.currentSelectionMainX == 0) && (game.encounter.currentSelectionMainY == 0)) {
						game.encounter.Fight();
					}
					if ((game.encounter.currentSelectionMainX == 1) && (game.encounter.currentSelectionMainY == 0)) {
						game.encounter.Pokemon();
					}
					if ((game.encounter.currentSelectionMainX == 0) && (game.encounter.currentSelectionMainY == 1)) {
						game.encounter.Item();
					}
					if ((game.encounter.currentSelectionMainX == 1) && (game.encounter.currentSelectionMainY == 1)) {
						game.encounter.Run();
					}
					Utils.playSelectSound();
				}
			}
		}
	}

	private void resetBattleVars() {
		game.encounter.playerTurn = false;
		game.encounter.inMain = true;
		game.encounter.inFight = false;
		game.encounter.currentSelectionMainX = 0;
		game.encounter.currentSelectionMainY = 0;
		game.encounter.currentSelectionFightX = 0;
		game.encounter.currentSelectionFightY = 0;
	}

	private void evaluateAndDealDamage(boolean checkPar, Pokemon playerPokemon, int move) {
		Random r = new Random();
		boolean par = (checkPar) ? r.nextInt(2) < 0 : false;
		if (!par) {
			Move chosen = playerPokemon.getMove(move);

			int attackStat = 0;
			int defStat = 0;
			if (chosen.getType().equals("PHYSICAL")) {
				attackStat = playerPokemon.getStat(EnumsAndConstants.STATS.ATTACK);
				defStat = game.encounter.enemyPokemon.get(0).getStat(EnumsAndConstants.STATS.DEFENSE);
			}
			if (chosen.getType().equals("SPECIAL")) {
				attackStat = playerPokemon.getStat(EnumsAndConstants.STATS.SP_ATTACK);
				defStat = game.encounter.enemyPokemon.get(0).getStat(EnumsAndConstants.STATS.SP_DEFENSE);
			}
			int damage = 0;
			if (!chosen.getType().equals("STAT")) {
				damage = (int) (((2 * playerPokemon.getLevel() / 5 + 2) * chosen.getStrength() * attackStat / defStat
						/ 50 + 2)
						* Utils.generateRandom(85, 100) / 100.0);
				((Pokemon) game.encounter.enemyPokemon.get(0)).doDamage(damage);
			}
			System.out.println(((Pokemon) game.encounter.enemyPokemon.get(0)).getStat(EnumsAndConstants.STATS.HP));
		} else {
			System.out.println(playerPokemon.getName() + " is paralyzed. It can't move.");
		}
	}

	private void checkIfThawed(Pokemon playerPokemon) {
		Random rr = new Random();
		int wakeupthaw = rr.nextInt(5);
		if (wakeupthaw <= 1) {
			if (playerPokemon.statusEffect == 4) {
				System.out.println(playerPokemon.getName() + " has woken up.");
			}
			if (playerPokemon.statusEffect == 5) {
				System.out.println(playerPokemon.getName() + " has broken free from the ice.");
			}
			playerPokemon.statusEffect = 0;
		}
	}

	private int getSelectedMove(int move) {
		if ((game.encounter.currentSelectionFightX == 0) && (game.encounter.currentSelectionFightY == 0)) {
			move = 0;
		}
		if ((game.encounter.currentSelectionFightX == 1) && (game.encounter.currentSelectionFightY == 0)) {
			move = 1;
		}
		if ((game.encounter.currentSelectionFightX == 0) && (game.encounter.currentSelectionFightY == 1)) {
			move = 2;
		}
		if ((game.encounter.currentSelectionFightX == 1) && (game.encounter.currentSelectionFightY == 1)) {
			move = 3;
		}
		return move;
	}

	void handleMenuEvent(int keyCode) {
		if (game.menuScreen.inConversation) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.Exit();
			}
		} else if (game.menuScreen.inMain) {
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.currentSelectionMain > 0) {
					game.menuScreen.currentSelectionMain -= 1;
				}
				Utils.playSelectSound();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.currentSelectionMain < 7) {
					game.menuScreen.currentSelectionMain += 1;
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.currentSelectionMain == 0) {
					game.menuScreen.PokeDex();
				}
				if (game.menuScreen.currentSelectionMain == 1) {
					game.menuScreen.Pokemon();
				}
				if (game.menuScreen.currentSelectionMain == 2) {
					game.menuScreen.Bag();
				}
				if (game.menuScreen.currentSelectionMain == 3) {
					game.menuScreen.PokeGear();
				}
				if (game.menuScreen.currentSelectionMain == 4) {
					game.menuScreen.TrainerCard();
				}
				if (game.menuScreen.currentSelectionMain == 5) {
					game.menuScreen.Save();
				}
				if (game.menuScreen.currentSelectionMain == 6) {
					game.menuScreen.Option();
				}
				if (game.menuScreen.currentSelectionMain == 7) {
					game.menuScreen.Exit();
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.Exit();
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inPokeDex) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inPokeDex = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inPokemon) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inPokemon = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inBag) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inBag = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inPokeGear) {
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.currentSelectionPokeGear > 0) {
					game.menuScreen.currentSelectionPokeGear -= 1;
				}
				Utils.playSelectSound();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.currentSelectionPokeGear < 3) {
					game.menuScreen.currentSelectionPokeGear += 1;
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.currentSelectionPokeGear == 0) {
					System.out.println("Map");
				} else if (game.menuScreen.currentSelectionPokeGear == 1) {
					System.out.println("Radio");
				} else if (game.menuScreen.currentSelectionPokeGear == 2) {
					System.out.println("Phone");
				} else if (game.menuScreen.currentSelectionPokeGear == 3) {
					System.out.println("Exit");
					game.menuScreen.currentSelectionPokeGear = 0;
					game.menuScreen.inPokeGear = false;
					game.menuScreen.inMain = true;
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inPokeGear = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inTrainerCard) {
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inTrainerCard = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inSave) {
			if (keyCode == KeyEvent.VK_UP) {
				game.menuScreen.currentSelectionSave = 0;
				Utils.playSelectSound();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				game.menuScreen.currentSelectionSave = 1;
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_Z) {
				if (game.menuScreen.currentSelectionSave == 0) {
					// Utils.saveGame(game.gold);
					System.out.println(game.playerName + "'s Game has been saved!");
				} else {
					game.menuScreen.inSave = false;
					game.menuScreen.inMain = true;
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inSave = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		} else if (game.menuScreen.inOption) {
			if (keyCode == KeyEvent.VK_UP) {
				if (game.menuScreen.currentSelectionOption > 0) {
					game.menuScreen.currentSelectionOption -= 1;
				}
				Utils.playSelectSound();
			} else if (keyCode == KeyEvent.VK_DOWN) {
				if (game.menuScreen.currentSelectionOption < 5) {
					game.menuScreen.currentSelectionOption += 1;
				}
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_Z) {
				Utils.playSelectSound();
			}
			if (keyCode == KeyEvent.VK_X) {
				game.menuScreen.inOption = false;
				game.menuScreen.inMain = true;
				Utils.playSelectSound();
			}
		}
	}

	void handleWorldEvent(int keyCode) {
		if (keyCode == KeyEvent.VK_UP) {
			game.gold.setDir(EnumsAndConstants.DIR.NORTH);
			if (game.movable_up) {
				game.walking = true;
			} else {
				game.gold.setSprite(EnumsAndConstants.sprite_lib.PLAYER_UP);
				Utils.playCollisionSound();
			}
		} else if (keyCode == KeyEvent.VK_DOWN) {
			game.gold.setDir(EnumsAndConstants.DIR.SOUTH);
			if (game.movable_down) {
				game.walking = true;
			} else {
				game.gold.setSprite(EnumsAndConstants.sprite_lib.PLAYER_DOWN);
				Utils.playCollisionSound();
			}
		} else if (keyCode == KeyEvent.VK_LEFT) {
			game.gold.setDir(EnumsAndConstants.DIR.WEST);
			if (game.movable_left) {
				game.walking = true;
			} else {
				game.gold.setSprite(EnumsAndConstants.sprite_lib.PLAYER_LEFT);
				Utils.playCollisionSound();
			}
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			game.gold.setDir(EnumsAndConstants.DIR.EAST);
			if (game.movable_right) {
				game.walking = true;
			} else {
				game.gold.setSprite(EnumsAndConstants.sprite_lib.PLAYER_RIGHT);
				Utils.playCollisionSound();
			}
		} else if (keyCode == KeyEvent.VK_ENTER) {
			Utils.playMenuSound();
			game.menuScreen.inMain = true;
			game.inMenu = true;
		}
		if (keyCode == KeyEvent.VK_Z) {
			System.out.println("Action Button");
			Utils.playSelectSound();
			NPC borderNPC = null;
			EnumsAndConstants.DIR playerDir = game.gold.getDir();
			int playerCurX = game.gold.getCurrentX();
			int playerCurY = game.gold.getCurrentY();
			for (int i = 0; i < game.currentMapNPC.length; i++) {
				NPC curNPC = game.currentMapNPC[i];
				int NPC_X = curNPC.getCurrentX();
				int NPC_Y = curNPC.getCurrentY();
				if (playerDir == EnumsAndConstants.DIR.WEST) {
					if ((NPC_X == playerCurX - 1) && (playerCurY == NPC_Y)) {
						curNPC.setSpriteFacing(EnumsAndConstants.DIR.EAST);
						borderNPC = curNPC;
					}
				} else if (playerDir == EnumsAndConstants.DIR.NORTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y + 1)) {
						curNPC.setSpriteFacing(EnumsAndConstants.DIR.SOUTH);
						borderNPC = curNPC;
					}
				} else if (playerDir == EnumsAndConstants.DIR.EAST) {
					if ((playerCurX == NPC_X - 1) && (playerCurY == NPC_Y)) {
						curNPC.setSpriteFacing(EnumsAndConstants.DIR.WEST);
						borderNPC = curNPC;
					}
				} else if (playerDir == EnumsAndConstants.DIR.SOUTH) {
					if ((playerCurX == NPC_X) && (playerCurY == NPC_Y - 1)) {
						curNPC.setSpriteFacing(EnumsAndConstants.DIR.NORTH);
						borderNPC = curNPC;
					}
				}
			}
			if (borderNPC != null) {
				game.inMenu = true;
				game.menuScreen.Message(borderNPC);
			}
		} else if (keyCode == KeyEvent.VK_X) {
			System.out.println("Cancel Button");
		}
	}

}
