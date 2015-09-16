package utilities;

import java.util.ArrayList;
import java.util.Random;

import model.GameData.SCREEN;
import party.MoveData;
import party.Party;
import party.PartyMember;
import party.PartyMember.STAT;
import party.PartyMember.STATUS;
import trainers.Actor.DIR;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;

//////////////////////////////////////////////////////////////////////////
//
// BattleScene - holds all logic for a Pokemon battle - w/wild or Trainer
//
// TODO - change to message boxes for messages
//
//////////////////////////////////////////////////////////////////////////
public class BattleEngine {
	private static BattleEngine m_instance = new BattleEngine();

	private GameController game = null;
	public boolean playerTurn = false;
	public boolean playerWon = false;
	public int currentSelectionMainX = 0;
	public int currentSelectionMainY = 0;
	public int currentSelectionFightX = 0;
	public int currentSelectionFightY = 0;
	public PartyMember playerCurrentPokemon = null;
	public PartyMember enemyCurrentPokemon = null;
	private Party enemyPokemon = null;
	public String enemyName = null;

	public enum TURN {
		ENEMY, PLAYER;

		public TURN opposite() {
			if (this == ENEMY) {
				return PLAYER;
			} else {
				return ENEMY;
			}
		}
	}

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
		m_instance.enemyCurrentPokemon = enemyPkmn.get(0);
		m_instance.playerTurn = true;
		m_instance.enemyName = opponentName;

		game.setMovable(false);
		game.setScreen(SCREEN.BATTLE);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// TODO check for white out condition, make comments
	//
	// ////////////////////////////////////////////////////////////////////////
	public void switchPokemon(TURN turn) {
		switch (turn) {
		case PLAYER:
			boolean loss = true;
			for (PartyMember p : game.getPlayer().getPokemon()) {
				if (p.getStat(STAT.HP) > 0) {
					loss = false;
				}
			}
			if (loss)
				Lose();
			else {
				// chose pokemon
			}
		case ENEMY:
			boolean enemyLoss = true;

			DebugUtility.printMessage("Enemy had: " + this.enemyPokemon.size() + " party members.");
			ArrayList<Integer> stillAlive = new ArrayList<Integer>();
			for (int i = 0; i < this.enemyPokemon.size(); i++) {
				if (this.enemyPokemon.get(i).getStat(STAT.HP) > 0) {
					enemyLoss = false;
					stillAlive.add(i);
				}
			}
			if (enemyLoss) {
				Win();
			} else {
				// random switch pokemon
				int choice = RandomNumUtils.generateRandom(0, stillAlive.size() - 1);
				for (int x = 0; x < stillAlive.size(); x++) {
					System.err.println("Party member: " + stillAlive.get(x) + " is alive.");
					System.err.println("switching to " + choice);
				}
				this.enemyCurrentPokemon = this.enemyPokemon.get(stillAlive.get(choice));

			}
		}
	}

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
		this.playerCurrentPokemon.gainExp(this.enemyCurrentPokemon.getExpGain(false, s));
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

		enemyPokemon.get(0).setStatusEffect(STATUS.NORMAL);

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
		this.enemyCurrentPokemon.setStatusEffect(STATUS.NORMAL);
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
	// Do TURN logic - assuming move has already been chosen
	//
	// TODO - change to message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public void takeTurn(TURN turn, int move) {
		// get the attacker and defender based on this TURN
		PartyMember attacker = (turn == TURN.PLAYER) ? playerCurrentPokemon : enemyCurrentPokemon;
		PartyMember defender = (turn == TURN.PLAYER) ? enemyCurrentPokemon : playerCurrentPokemon;

		// get the chosen move
		MoveData chosen = attacker.getMove(move);

		// try to thaw / wake the attacker if they are affected
		attacker.tryToThaw();

		STATUS status = attacker.getStatusEffect();
		switch (status) {
		case FRZ:
		case SLP:
			// do nothing if still frozen or asleep
			break;
		case PAR:
			// do paralyzed logic
			Random r = new Random();
			if (r.nextInt(2) <= 0) {
				// do damage to the defender based on the chosen move
				defender.doDamage(chosen);
				game.playClip(SOUND_EFFECT.DAMAGE);

				// decrement move counter, print result
				// TODO convert to message
				DebugUtility.printMessage(turn + "'s turn is over");
				chosen.movePP--;
			} else {
				DebugUtility.printMessage(attacker.getName() + " is paralyzed. It can't move.");
			}
			break;
		case BRN: // fall through, BRN and PZN are the same
		case PZN:
			// TODO - deal % damage for burn / psn
			game.playClip(SOUND_EFFECT.DAMAGE);
			DebugUtility.printMessage(attacker.getName() + " has been hurt by it's " + status);
		default:
			defender.doDamage(chosen);
			game.playClip(SOUND_EFFECT.DAMAGE);
		}

		if (defender.getStat(STAT.HP) <= 0) {
			switchPokemon(turn.opposite());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// enemyTurn - enemy chooses move and deals damage to player
	//
	// TODO - change to message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	public void enemyTurn() {
		// if (!this.playerWon) {
		if (this.enemyCurrentPokemon.getStat(STAT.HP) > 0) {
			takeTurn(TURN.ENEMY, RandomNumUtils.generateRandom(0, this.enemyCurrentPokemon.getNumMoves() - 1));
		}
		// }
	}
}