package controller;

import java.util.ArrayList;
import java.util.Random;

import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;
import graphics.GameGraphicsData;
import model.Configuration;
import model.MessageQueue;
import party.Battler;
import party.Battler.STAT;
import party.Battler.STATUS;
import party.MoveData;
import party.Party;
import scenes.BattleMessageScene;
import scenes.BattleScene;
import scenes.WorldScene;
import trainers.Actor.DIR;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

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
	 * Player's current fighting party member
	 */
	public Battler playerCurrentPokemon = null;
	/**
	 * enemy's current fighting party member
	 */
	public Battler enemyCurrentPokemon = null;
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
			TURN retTurn = OPPONENT;
			if (this == OPPONENT) {
				retTurn = PLAYER;
			}
			return retTurn;
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
			this.game = g;
			m_instance.currentSelectionMainX = 0;
			m_instance.currentSelectionMainY = 0;

			int idx = -1;
			for (int x = 0; x < this.game.getPlayer().getParty().size(); x++) {
				if (this.game.getPlayer().getParty().get(x).getStat(STAT.HP) > 0) {
					idx = x;
					x = this.game.getPlayer().getParty().size() + 1;
				}
			}
			m_instance.playerCurrentPokemon = this.game.getPlayer().getParty().get(idx);
			System.err.println(m_instance.playerCurrentPokemon);
			m_instance.playerCurrentPokemon.setParticipated();
			m_instance.enemyPokemon = enemyPkmn;
			m_instance.enemyCurrentPokemon = enemyPkmn.get(0);
			m_instance.playerTurn = true;
			m_instance.enemyName = opponentName;

			this.game.getPlayer().canMove = false;
			GameGraphicsData.getInstance().setScene(BattleScene.instance);
		}
	}

	/**
	 * Depending on the current TURN, handle changing party members
	 * 
	 * @todo method-ize check for white out conditions
	 * 
	 * @param turn
	 *            the current turn
	 */
	public void changePokemon(TURN turn) {
		switch (turn) {
		case PLAYER:
			boolean loss = true;
			Party playerParty = this.game.getPlayer().getParty();
			DebugUtility.printMessage("Party has: " + playerParty.size() + " party members.");

			ArrayList<Integer> stillAlive = new ArrayList<>();
			for (int i = 0; i < playerParty.size(); i++) {
				if (playerParty.get(i).getStat(STAT.HP) > 0) {
					loss = false;
					stillAlive.add(Integer.valueOf(i));
				}
			}
			if (loss) {
				Lose();
			} else {
				// random switch pokemon
				int choice = RandomNumUtils.generateRandom(0, stillAlive.size() - 1);
				this.playerCurrentPokemon = playerParty.get(stillAlive.get(choice).intValue());
			}
			break;
		case OPPONENT:
			boolean enemyLoss = true;

			DebugUtility.printMessage("Enemy had: " + this.enemyPokemon.size() + " party members.");
			stillAlive = new ArrayList<>();
			for (int i = 0; i < this.enemyPokemon.size(); i++) {
				if (this.enemyPokemon.get(i).getStat(STAT.HP) > 0) {
					enemyLoss = false;
					stillAlive.add(Integer.valueOf(i));
				}
			}
			if (enemyLoss) {
				Win();
			} else {
				// random switch pokemon
				int choice = RandomNumUtils.generateRandom(0, stillAlive.size() - 1);
				this.enemyCurrentPokemon = this.enemyPokemon.get(stillAlive.get(choice).intValue());
			}
			break;
		default:
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
		for (int x = 0; x < this.game.getPlayer().getParty().size(); x++) {
			if (this.game.getPlayer().getParty().get(x).hasParticipated()) {
				s++;
			}
		}
		this.playerCurrentPokemon.gainExp(this.game, this.enemyCurrentPokemon.getExpGain(false, s));
	}

	/**
	 * Perform logic when the player wins the battle.
	 */
	public void Win() {
		giveEXP();
		DebugUtility.printMessage("Player won!");
		// reset logic
		this.game.getPlayer().canMove = false;

		MessageQueue.getInstance().add("Player won!");
		GameGraphicsData.getInstance().setScene(BattleMessageScene.instance);

		this.game.getPlayer().beatenTrainers.add(this.enemyName);

		this.enemyPokemon.get(0).setStatusEffect(STATUS.NORMAL);

		// reset the music
		// todo - verify playBackgroundMusic doesn't automatically pause/stop
		// when switching music
		AudioLibrary.playBackgroundMusic(this.game.getPlayer().getCurLoc().getName());
	}

	/**
	 * Perform logic when the player loses the battle.
	 */
	public void Lose() {
		this.enemyCurrentPokemon.setStatusEffect(STATUS.NORMAL);
		// todo - change to message boxes

		String[] lossMessages = { "Player Pokemon has fainted",
				this.game.getPlayer().getName() + " is all out of usable Pokemon!",
				this.game.getPlayer().getName() + " whited out." };

		for (String message : lossMessages) {
			DebugUtility.printMessage(message);
			MessageQueue.getInstance().add(message);
		}
		this.game.getPlayer().setDirection(DIR.SOUTH);
		Party playerParty = this.game.getPlayer().getParty();
		for (Battler member : playerParty) {
			member.fullHeal();
		}
		GameGraphicsData.getInstance().setScene(WorldScene.instance);
	}

	/**
	 * Do TURN logic - assuming move has already been chosen
	 * 
	 * todo - change to message boxes
	 * 
	 * @param turn
	 *            - the current TURN
	 * @param move
	 *            - the selected move
	 */
	public void takeTurn(TURN turn, int move) {
		DebugUtility.printHeader("Turn: " + turn);

		// get the attacker and defender based on this TURN
		Battler attacker = (turn == TURN.PLAYER) ? this.playerCurrentPokemon : this.enemyCurrentPokemon;
		Battler defender = (turn == TURN.PLAYER) ? this.enemyCurrentPokemon : this.playerCurrentPokemon;
		if (attacker.getStat(STAT.HP) > 0) {
			// get the chosen move
			MoveData chosen = attacker.getMove(move);

			DebugUtility.printMessage(attacker.getName() + " is attacking " + defender.getName());

			// try to thaw / wake the attacker if they are affected
			attacker.tryToThaw();

			DebugUtility.printMessage("Using: " + chosen.toString());

			MessageQueue.getInstance().add(attacker.getName() + " used " + chosen.name + "!");

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
					AudioLibrary.playClip(SOUND_EFFECT.DAMAGE);

					// decrement move counter, print result
					// todo convert to message
					DebugUtility.printMessage(turn + "'s turn is over");
					chosen.movePP--;
				} else {
					DebugUtility.printMessage(attacker.getName() + " is paralyzed. It can't move.");
				}
				break;
			case BRN: // fall through, BRN and PZN are the same
			case PZN:
				// todo - deal % damage for burn / psn
				AudioLibrary.playClip(SOUND_EFFECT.DAMAGE);
				DebugUtility.printMessage(attacker.getName() + " has been hurt by it's " + status);
				//$FALL-THROUGH$
			default:
				defender.takeDamage(attacker.doDamage(chosen));
				AudioLibrary.playClip(SOUND_EFFECT.DAMAGE);
			}

			if (defender.getStat(STAT.HP) <= 0) {
				changePokemon(turn.opposite());
			}
			DebugUtility.printHeader("End turn");
		}
	}
}