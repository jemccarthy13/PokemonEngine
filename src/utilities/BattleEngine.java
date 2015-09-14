package utilities;

import java.util.Random;

import model.GameData.SCREEN;
import party.MoveData;
import party.Party;
import party.PartyMember;
import party.PartyMember.STATS;
import trainers.Actor.DIR;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;

//////////////////////////////////////////////////////////////////////////
//
// BattleScene - holds all logic for a Pokemon battle - w/wild or Trainer
//
// TODO - implement player switching Pokemon
// TODO - implement enemy switching Pokemon
// TODO - change to message boxes for messages
//
//////////////////////////////////////////////////////////////////////////
public class BattleEngine {
	private static BattleEngine m_instance = new BattleEngine();

	private GameController game = null;
	public boolean playerTurn = false;
	// public boolean inItem = false;
	public boolean inPokemon = false;
	public boolean playerWon = false;
	public int currentSelectionMainX = -1;
	public int currentSelectionMainY = -1;
	public int currentSelectionFightX = -1;
	public int currentSelectionFightY = -1;
	public PartyMember playerCurrentPokemon = null;;
	public Party enemyPokemon = null;;
	public String enemyName = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - keeps a local copy of the game and the current npc
	//
	// ////////////////////////////////////////////////////////////////////////
	private BattleEngine() {}

	public static BattleEngine getInstance() {
		return m_instance;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Start - set all variables for the start of a battle
	//
	// TODO - first pokemon isn't always the pokemon to fight: change to first
	// Pokemon above 0 health
	//
	// ////////////////////////////////////////////////////////////////////////
	public void fight(Party enemyPkmn, GameController g, String opponentName) {
		game = g;
		m_instance.currentSelectionMainX = 0;
		m_instance.currentSelectionFightX = 0;
		m_instance.currentSelectionMainY = 0;
		m_instance.currentSelectionFightY = 0;
		m_instance.playerCurrentPokemon = game.getPlayer().getPokemon().get(0);
		m_instance.playerCurrentPokemon.setParticipated();
		m_instance.enemyPokemon = enemyPkmn;
		m_instance.playerTurn = true;
		m_instance.enemyName = opponentName;

		game.setMovable(false);
		game.setScreen(SCREEN.BATTLE);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Item - set variables for the item menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void inItemMenu() {
		game.setScreen(SCREEN.BATTLE_ITEM);
		// TODO item menu logic
		DebugUtility.printMessage("Item");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pokemon - set variables for the party menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void inPokemonMenu() {
		this.inPokemon = true;
		// TODO - pokemon screen logic
		DebugUtility.printMessage("Pokemon");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// playerSwitchPokemon - if players Pokemon are out of HP, white out -
	// otherwise, switch pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public void playerSwitchPokemon() {
		boolean loss = true;
		for (PartyMember p : game.getPlayer().getPokemon()) {
			if (p.getStat(STATS.HP) > 0) {
				loss = false;
			}
		}
		if (loss)
			Lose();

		// TODO - else switch pokemon
	}

	public void enemySwitchPokemon() {}

	// ////////////////////////////////////////////////////////////////////////
	//
	// giveExp - give experience to all Pokemon that have participated
	//
	// Experience is calculated in the Pokemon class- based on the number
	// of player's Pokemon that have participated in battle.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void giveEXP() {
		int s = 0;
		for (int x = 0; x < game.getPlayer().getPokemon().size(); x++) {
			if (((PartyMember) game.getPlayer().getPokemon().get(x)).hasParticipated()) {
				s++;
			}
		}
		this.playerCurrentPokemon.gainExp(((PartyMember) this.enemyPokemon.get(0)).getExpGain(false, s));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Run - set variables for running away from battle (or exiting the fight)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Run() {
		if (enemyName == null) {
			((PartyMember) this.enemyPokemon.get(0)).setStatusEffect(0);

			game.setScreen(SCREEN.WORLD);
			// TODO - convert to message box
			DebugUtility.printMessage("Got away safely!");
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Win - set the variables for a player win in a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Win() {
		giveEXP();
		DebugUtility.printMessage("Player won!");
		// reset logic
		game.setPlayerWin(false);
		game.setMovable(false);

		game.setCurrentMessage("Player won!");
		game.setScreen(SCREEN.BATTLE_MESSAGE);

		game.getPlayer().beatenTrainers.add(enemyName);

		((PartyMember) this.enemyPokemon.get(0)).setStatusEffect(0);

		// reset the music
		// TODO - verify playBackgroundMusic doesn't automatically pause/stop
		// when switching music
		game.playBackgroundMusic();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Lose - set the variables for a player loss (white out) in a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Lose() {
		((PartyMember) this.enemyPokemon.get(0)).setStatusEffect(0);
		// TODO - convert to message box
		DebugUtility.printMessage("Player Pokemon has fainted");
		DebugUtility.printMessage(game.getPlayer().getName() + " is all out of usable Pokemon!");
		DebugUtility.printMessage(game.getPlayer().getName() + " whited out.");

		game.setPlayerDirection(DIR.SOUTH);
		game.getPlayer().getPokemon().get(0).heal(-1);
		game.setScreen(SCREEN.WORLD);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// enemyTurn - enemy chooses move and deals damage to player
	//
	// TODO - change to message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public void enemyTurn() {
		if (!this.playerWon) {
			if ((((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 4)
					|| (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 5)) {
				// Do frozen or sleep logic
				Random rr = new Random();
				int wakeupthaw = rr.nextInt(5);
				if (wakeupthaw <= 1) {
					if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 4) {
						DebugUtility
								.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName() + " has woken up.");
					}
					if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 5) {
						DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
								+ " has broken free from the ice.");
					}
					((PartyMember) this.enemyPokemon.get(0)).setStatusEffect(0);
				} else {
					if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 4) {
						DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
								+ " is still asleep.");
					}
					if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 5) {
						DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
								+ " is frozen solid.");
					}
				}
			}
			if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 1) {
				// do paralyzed logic
				Random r = new Random();
				int rand = r.nextInt(2);
				if (rand <= 0) {
					int choice = RandomNumUtils.generateRandom(0,
							((PartyMember) this.enemyPokemon.get(0)).getNumMoves() - 1);
					MoveData chosen = ((PartyMember) this.enemyPokemon.get(0)).getMove(choice);
					int attackStat = 0;
					int defStat = 0;
					if (chosen.type.equals("PHYSICAL")) {
						attackStat = ((PartyMember) this.enemyPokemon.get(0)).getStat(STATS.ATTACK);
						defStat = this.playerCurrentPokemon.getStat(STATS.DEFENSE);
					}
					if (chosen.type.equals("SPECIAL")) {
						attackStat = ((PartyMember) this.enemyPokemon.get(0)).getStat(STATS.SP_ATTACK);
						defStat = this.playerCurrentPokemon.getStat(STATS.SP_DEFENSE);
					}
					int damage = 0;
					if (!chosen.type.equals("STAT")) {
						damage = (int) (((2 * ((PartyMember) this.enemyPokemon.get(0)).getLevel() / 5 + 2)
								* chosen.power * attackStat / 50 / defStat + 2)
								* RandomNumUtils.generateRandom(85, 100) / 100.0);
					}
					this.playerCurrentPokemon.doDamage(damage);
					game.playClip(SOUND_EFFECT.DAMAGE);
					// TODO - convert to message box
					DebugUtility.printMessage("Enemy's turn is over");
					chosen.movePP--;
				} else {
					DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
							+ " is paralyzed. It can't move.");
				}
			} else {
				// otherwise do nomral battle order of events
				int choice = RandomNumUtils.generateRandom(0,
						((PartyMember) this.enemyPokemon.get(0)).getNumMoves() - 1);
				MoveData chosen = ((PartyMember) this.enemyPokemon.get(0)).getMove(choice);

				int attackStat = 0;
				int defStat = 1;
				if (chosen.type.equals("PHYSICAL")) {
					attackStat = ((PartyMember) this.enemyPokemon.get(0)).getStat(STATS.ATTACK);
					defStat = this.playerCurrentPokemon.getStat(STATS.DEFENSE);
				}
				if (chosen.type.equals("SPECIAL")) {
					attackStat = ((PartyMember) this.enemyPokemon.get(0)).getStat(STATS.SP_ATTACK);
					defStat = this.playerCurrentPokemon.getStat(STATS.SP_DEFENSE);
				}
				if (!chosen.type.equals("STAT")) {
					@SuppressWarnings("unused")
					int damage = (int) (((2 * ((PartyMember) this.enemyPokemon.get(0)).getLevel() / 5 + 2)
							* chosen.power * attackStat / defStat / 50 + 2)
							* RandomNumUtils.generateRandom(85, 100) / 100.0D);

					// TODO implement stat damage types
				}
				chosen.movePP--;
				game.playClip(SOUND_EFFECT.SELECT);
			}
			if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 2) {
				game.playClip(SOUND_EFFECT.DAMAGE);
				// TODO convert to message box
				DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
						+ " has been hurt by its burn");
			}
			if (((PartyMember) this.enemyPokemon.get(0)).getStatusEffect() == 3) {
				game.playClip(SOUND_EFFECT.DAMAGE);
				// TODO convert to message box
				DebugUtility.printMessage(((PartyMember) this.enemyPokemon.get(0)).getName()
						+ " has been hurt by its poison");
			}
			this.playerTurn = true;
		}
	}

}