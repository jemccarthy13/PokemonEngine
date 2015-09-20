package utilities;

import java.util.ArrayList;
import java.util.Random;

import model.Configuration;
import model.GameData.SCREEN;
import party.MoveData;
import party.Party;
import party.PartyMember;
import party.PartyMember.STAT;
import party.PartyMember.STATUS;
import trainers.Actor.DIR;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;

/**
 * Holds all logic for a Pokemon battle, either a wild encounter or trainer
 * encounter
 */
public class BattleEngine {
	/**
	 * Singleton instance variable for a BattleEngine
	 */
	private static BattleEngine m_instance = new BattleEngine();

	/**
	 * The GameController to perform game events
	 */
	private GameController game = null;
	/**
	 * Is it the player's turn?
	 */
	public boolean playerTurn = false;
	/**
	 * Has the player won?
	 */
	public boolean playerWon = false;
	/**
	 * The current selection (x) on the main battle menu
	 */
	public int currentSelectionMainX = 0;
	/**
	 * Current selection (y) on the main battle menu
	 */
	public int currentSelectionMainY = 0;
	/**
	 * The current selection (x) on the fight battle menu
	 */
	public int currentSelectionFightX = 0;
	/**
	 * The current selection (y) on the fight battle menu
	 */
	public int currentSelectionFightY = 0;
	/**
	 * Player's current fighting party member
	 */
	public PartyMember playerCurrentPokemon = null;
	/**
	 * enemy's current fighting party member
	 */
	public PartyMember enemyCurrentPokemon = null;
	/**
	 * The enemies party
	 */
	private Party enemyPokemon = null;
	/**
	 * The name of the enemy Trainer
	 */
	public String enemyName = null;

	/**
	 * An enumeration of whose turn it is
	 */
	public enum TURN {
		/**
		 * Enemy turn
		 */
		OPPONENT,
		/**
		 * Player turn
		 */
		PLAYER;

		/**
		 * Get the opposite Actor's turn from this one
		 * 
		 * @return opposite TURN
		 */
		public TURN opposite() {
			if (this == OPPONENT) {
				return PLAYER;
			} else {
				return OPPONENT;
			}
		}
	}

	/**
	 * Empty default constructor using singleton pattern
	 */
	private BattleEngine() {}

	/**
	 * Get the BattleEngine instance
	 * 
	 * @return the singleton instance of the BattleEngine
	 */
	public static BattleEngine getInstance() {
		return m_instance;
	}

	/**
	 * Set all variables for the start of a battle
	 * 
	 * @param enemyPkmn
	 *            - the enemy's party
	 * @param g
	 *            - the game controller
	 * @param opponentName
	 *            - the name of the opponent
	 */
	public void fight(Party enemyPkmn, GameController g, String opponentName) {
		if (Configuration.DOBATTLES) {
			game = g;
			m_instance.currentSelectionMainX = 0;
			m_instance.currentSelectionFightX = 0;
			m_instance.currentSelectionMainY = 0;
			m_instance.currentSelectionFightY = 0;

			int idx = -1;
			for (int x = 0; x < game.getPlayer().getParty().size(); x++) {
				if (game.getPlayer().getParty().get(x).getStat(STAT.HP) > 0) {
					idx = x;
					x = game.getPlayer().getParty().size() + 1;
				}
			}
			m_instance.playerCurrentPokemon = game.getPlayer().getParty().get(idx);
			System.err.println(m_instance.playerCurrentPokemon);
			m_instance.playerCurrentPokemon.setParticipated();
			m_instance.enemyPokemon = enemyPkmn;
			m_instance.enemyCurrentPokemon = enemyPkmn.get(0);
			m_instance.playerTurn = true;
			m_instance.enemyName = opponentName;

			game.setMovable(false);
			game.setScreen(SCREEN.BATTLE);
		}
	}

	/**
	 * Depending on the current TURN, handle switching party members
	 * 
	 * @TODO method-ize check for white out conditions
	 * 
	 * @param turn
	 *            the current turn
	 */
	public void switchPokemon(TURN turn) {
		switch (turn) {
		case PLAYER:
			boolean loss = true;
			Party playerParty = this.game.getPlayer().getParty();
			DebugUtility.printMessage("Party has: " + playerParty.size() + " party members.");

			ArrayList<Integer> stillAlive = new ArrayList<Integer>();
			for (int i = 0; i < playerParty.size(); i++) {
				if (playerParty.get(i).getStat(STAT.HP) > 0) {
					loss = false;
					stillAlive.add(i);
				}
			}
			if (loss) {
				Lose();
			} else {
				// random switch pokemon
				int choice = RandomNumUtils.generateRandom(0, stillAlive.size() - 1);
				this.playerCurrentPokemon = playerParty.get(stillAlive.get(choice));
			}
			break;
		case OPPONENT:
			boolean enemyLoss = true;

			DebugUtility.printMessage("Enemy had: " + this.enemyPokemon.size() + " party members.");
			stillAlive = new ArrayList<Integer>();
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
				this.enemyCurrentPokemon = this.enemyPokemon.get(stillAlive.get(choice));
			}
			break;
		}
	}

	/**
	 * Give experience to all PartyMembers that have participated. Experience is
	 * calculated in the PartyMembers class, based on the number of player's
	 * PartyMembers that have participated in battle.
	 */
	public void giveEXP() {
		int s = 0;
		for (int x = 0; x < game.getPlayer().getParty().size(); x++) {
			if (((PartyMember) game.getPlayer().getParty().get(x)).hasParticipated()) {
				s++;
			}
		}
		this.playerCurrentPokemon.gainExp(this.enemyCurrentPokemon.getExpGain(false, s));
	}

	/**
	 * Perform logic when the player wins the battle.
	 */
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

	/**
	 * Perform logic when the player loses the battle.
	 */
	public void Lose() {
		this.enemyCurrentPokemon.setStatusEffect(STATUS.NORMAL);
		// TODO - change to message boxes
		DebugUtility.printMessage("Player Pokemon has fainted");
		DebugUtility.printMessage(game.getPlayer().getName() + " is all out of usable Pokemon!");
		DebugUtility.printMessage(game.getPlayer().getName() + " whited out.");

		game.setPlayerDirection(DIR.SOUTH);
		Party playerParty = game.getPlayer().getParty();
		for (PartyMember member : playerParty) {
			member.fullHeal();
		}
		game.setScreen(SCREEN.WORLD);
	}

	/**
	 * Do TURN logic - assuming move has already been chosen
	 * 
	 * TODO - change to message boxes
	 * 
	 * @param turn
	 *            - the current TURN
	 * @param move
	 *            - the selected move
	 */
	public void takeTurn(TURN turn, int move) {
		DebugUtility.printHeader("Turn: " + turn);

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
		DebugUtility.printHeader("End turn");
	}

	/**
	 * enemy chooses move and deals damage to player
	 * 
	 * @TODO remove from BattleEngine?
	 */
	public void enemyTurn() {
		// if (!this.playerWon) {
		if (this.enemyCurrentPokemon.getStat(STAT.HP) > 0) {
			takeTurn(TURN.OPPONENT, RandomNumUtils.generateRandom(0, this.enemyCurrentPokemon.getNumMoves() - 1));
		}
		// }
	}
}