package utilities;

import java.util.Random;

import pokedex.MoveData;
import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonList;
import trainers.Actor.DIR;
import audio.AudioLibrary;
import driver.GameData.SCREEN;
import driver.GamePanel;

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

	private GamePanel game = null;
	public boolean playerTurn = false;
	// public boolean inMain = true;
	public boolean inFight = false;
	public boolean inItem = false;
	public boolean inPokemon = false;
	public boolean playerWon = false;
	public int currentSelectionMainX = -1;
	public int currentSelectionMainY = -1;
	public int currentSelectionFightX = -1;
	public int currentSelectionFightY = -1;
	public Pokemon playerCurrentPokemon = null;;
	public PokemonList enemyPokemon = null;;
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
	public void fight(PokemonList enemyPkmn, GamePanel g, String opponentName) {
		m_instance.currentSelectionMainX = 0;
		m_instance.currentSelectionFightX = 0;
		m_instance.currentSelectionMainY = 0;
		m_instance.currentSelectionFightY = 0;
		m_instance.game = g;
		m_instance.playerCurrentPokemon = g.game.getPlayer().getPokemon().get(0);
		m_instance.playerCurrentPokemon.setParticipated();
		m_instance.enemyPokemon = enemyPkmn;
		m_instance.playerTurn = true;
		m_instance.enemyName = opponentName;

		m_instance.game.game.setMovable(false);
		m_instance.game.game.setScreen(SCREEN.BATTLE);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Item - set variables for the item menu
	//
	// ////////////////////////////////////////////////////////////////////////
	public void inItemMenu() {
		this.inItem = true;
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
		for (Pokemon p : game.game.getPlayer().getPokemon()) {
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
		for (int x = 0; x < game.game.getPlayer().getPokemon().size(); x++) {
			if (((Pokemon) game.game.getPlayer().getPokemon().get(x)).hasParticipated()) {
				s++;
			}
		}
		this.playerCurrentPokemon.gainExp(((Pokemon) this.enemyPokemon.get(0)).getExpGain(false, s));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Run - set variables for running away from battle (or exiting the fight)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Run() {
		if (enemyName == null) {
			((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

			game.game.setScreen(SCREEN.WORLD);
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

		// reset logic
		game.game.setPlayerWin(false);
		game.game.setMovable(false);
		inFight = false;
		game.game.setScreen(SCREEN.BATTLE_MESSAGE);
		game.game.setMessage("Player won!");

		game.game.getPlayer().beatenTrainers.add(enemyName);

		((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;

		// reset the music
		// TODO - verify playBackgroundMusic doesn't automatically pause/stop
		// when switching music
		AudioLibrary.getInstance().pauseBackgrondMusic();
		AudioLibrary.getInstance().playBackgroundMusic("NewBarkTown");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Lose - set the variables for a player loss (white out) in a battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Lose() {
		((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;
		// TODO - convert to message box
		DebugUtility.printMessage("Player Pokemon has fainted");
		DebugUtility.printMessage(game.game.getPlayer().getName() + " is all out of usable Pokemon!");
		DebugUtility.printMessage(game.game.getPlayer().getName() + " whited out.");

		game.game.setPlayerDirection(DIR.SOUTH);
		game.game.getPlayer().getPokemon().get(0).heal(-1);
		game.game.setScreen(SCREEN.WORLD);
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
			if ((((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4)
					|| (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5)) {
				// Do frozen or sleep logic
				Random rr = new Random();
				int wakeupthaw = rr.nextInt(5);
				if (wakeupthaw <= 1) {
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4) {
						DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName() + " has woken up.");
					}
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5) {
						DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName()
								+ " has broken free from the ice.");
					}
					((Pokemon) this.enemyPokemon.get(0)).statusEffect = 0;
				} else {
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 4) {
						DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName() + " is still asleep.");
					}
					if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 5) {
						DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName() + " is frozen solid.");
					}
				}
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 1) {
				// do paralyzed logic
				Random r = new Random();
				int rand = r.nextInt(2);
				if (rand <= 0) {
					int choice = RandomNumUtils.generateRandom(0,
							((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
					MoveData chosen = ((Pokemon) this.enemyPokemon.get(0)).getMove(choice);
					int attackStat = 0;
					int defStat = 0;
					if (chosen.type.equals("PHYSICAL")) {
						attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(STATS.ATTACK);
						defStat = this.playerCurrentPokemon.getStat(STATS.DEFENSE);
					}
					if (chosen.type.equals("SPECIAL")) {
						attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(STATS.SP_ATTACK);
						defStat = this.playerCurrentPokemon.getStat(STATS.SP_DEFENSE);
					}
					int damage = 0;
					if (!chosen.type.equals("STAT")) {
						damage = (int) (((2 * ((Pokemon) this.enemyPokemon.get(0)).getLevel() / 5 + 2) * chosen.power
								* attackStat / 50 / defStat + 2)
								* RandomNumUtils.generateRandom(85, 100) / 100.0);
					}
					this.playerCurrentPokemon.doDamage(damage);
					game.game.playClip(AudioLibrary.SE_DAMAGE);
					// TODO - convert to message box
					DebugUtility.printMessage("Enemy's turn is over");
					chosen.movePP--;
				} else {
					DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName()
							+ " is paralyzed. It can't move.");
				}
			} else {
				// otherwise do nomral battle order of events
				int choice = RandomNumUtils.generateRandom(0, ((Pokemon) this.enemyPokemon.get(0)).getNumMoves() - 1);
				MoveData chosen = ((Pokemon) this.enemyPokemon.get(0)).getMove(choice);

				int attackStat = 0;
				int defStat = 1;
				if (chosen.type.equals("PHYSICAL")) {
					attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(STATS.ATTACK);
					defStat = this.playerCurrentPokemon.getStat(STATS.DEFENSE);
				}
				if (chosen.type.equals("SPECIAL")) {
					attackStat = ((Pokemon) this.enemyPokemon.get(0)).getStat(STATS.SP_ATTACK);
					defStat = this.playerCurrentPokemon.getStat(STATS.SP_DEFENSE);
				}
				if (!chosen.type.equals("STAT")) {
					@SuppressWarnings("unused")
					int damage = (int) (((2 * ((Pokemon) this.enemyPokemon.get(0)).getLevel() / 5 + 2) * chosen.power
							* attackStat / defStat / 50 + 2)
							* RandomNumUtils.generateRandom(85, 100) / 100.0D);

					// TODO implement stat damage types
				}
				chosen.movePP--;
				game.game.playClip(AudioLibrary.SE_SELECT);
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 2) {
				game.game.playClip(AudioLibrary.SE_DAMAGE);
				// TODO convert to message box
				DebugUtility
						.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName() + " has been hurt by its burn");
			}
			if (((Pokemon) this.enemyPokemon.get(0)).statusEffect == 3) {
				game.game.playClip(AudioLibrary.SE_DAMAGE);
				// TODO convert to message box
				DebugUtility.printMessage(((Pokemon) this.enemyPokemon.get(0)).getName()
						+ " has been hurt by its poison");
			}
			this.playerTurn = true;
		}
	}

}