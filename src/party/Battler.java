package party;

import graphics.SpriteLibrary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

import party.MoveData.MOVECATEGORY;
import utilities.DebugUtility;
import utilities.RandomNumUtils;
import controller.GameController;

/**
 * Generated from Battler data, calculates stats based on a level and holds
 * moves / sprite data
 */
public class Battler implements Serializable {
	private static final long serialVersionUID = 3959217221984077560L;

	/**
	 * The configuration of a Party member's stats
	 */
	public static enum STAT {
		/**
		 * Health available
		 */
		HP,
		/**
		 * How strong offensively
		 */
		ATTACK,
		/**
		 * How strong defensively
		 */
		DEFENSE,
		/**
		 * How strong special offense is
		 */
		SP_ATTACK,
		/**
		 * How strong special defense is
		 */
		SP_DEFENSE,
		/**
		 * How fast the party member is
		 * 
		 * TODO in battle, check for fastest party member to go first
		 */
		SPEED,
		/**
		 * How accurate the party member is
		 */
		ACCURACY
	}

	/**
	 * The current status of the party member
	 */
	public static enum STATUS {
		/**
		 * Poisoned
		 */
		PZN,
		/**
		 * Burned
		 */
		BRN,
		/**
		 * Frozen and can't move
		 */
		FRZ,
		/**
		 * Asleep and can't move
		 */
		SLP,
		/**
		 * Paralyzed and maybe can't move
		 */
		PAR,
		/**
		 * Nothing's wrong
		 */
		NORMAL
	}

	private int evolution_stage = 0;
	private int level;
	private boolean participated = false;
	private MoveList moves;
	private int curExp;
	private ImageIcon party_icon, back_sprite, front_sprite;
	private STATUS statusEffect = STATUS.NORMAL;

	private BattlerData pData;

	private HashMap<STAT, Integer> stats = new HashMap<STAT, Integer>();
	private HashMap<STAT, Integer> maxStats = new HashMap<STAT, Integer>();

	/**
	 * Given PartyMemberData and a level, fill in the blanks // (stats,
	 * evolution stage, moves, sprites)
	 * 
	 * @param pData
	 *            - party member data object
	 * @param lev
	 *            - the level of the party member
	 */
	public Battler(BattlerData pData, int lev) {
		this.pData = pData;
		this.level = lev;
		this.curExp = (this.level * this.level * this.level);

		for (Integer x : pData.evolution_levels) {
			if (level > x && x != 0) {
				evolution_stage++;
			}
		}

		// create base max stats and initialize current stats to the maximums
		for (STAT s : STAT.values()) {
			maxStats.put(s, RandomNumUtils.randomBaseStat(level));
			stats.put(s, maxStats.get(s));
		}

		moves = new MoveList(pData.name);

		int idx = -1;
		for (int x = 0; x < pData.movesLearned.size(); x++) {
			if (level > pData.levelsLearned.get(x)) {
				idx++;
			}
		}
		for (int y = 0; y < 4; y++) {
			if (idx - y <= pData.movesLearned.size() && idx - y >= 0) {
				moves.add(MoveLibrary.getInstance().get(pData.movesLearned.get(idx - y)), false);
			}
		}

		updateSprites();
	}

	/**
	 * Get the right sprites for an updated pokedex number
	 */
	public void updateSprites() {
		String number = formatPokedexNumber();
		this.party_icon = SpriteLibrary.createImage(SpriteLibrary.getLibPath() + "Icons/icon" + number + ".png");
		this.back_sprite = SpriteLibrary.createImage(SpriteLibrary.getLibPath() + "Battlers/" + number + "b.png");
		this.front_sprite = SpriteLibrary.createImage(SpriteLibrary.getLibPath() + "Battlers/" + number + ".png");
	}

	/**
	 * Calculate the exp gained from defeating a battler in the current battle
	 * 
	 * @param trainerOwned
	 *            - is the battler owned by an opponent
	 * @param numParticipants
	 *            - the number of party members that participated in this battle
	 * @return the experience gained by this party member
	 */
	public int getExpGain(boolean trainerOwned, int numParticipants) {
		double a = trainerOwned ? 1.5 : 1;
		int b = pData.baseExp;
		return (int) (a * b * level) / (7 * numParticipants);
	}

	/**
	 * Give the pokemon the calculated exp if it's over the next level amount,
	 * level up
	 * 
	 * @param game
	 * 
	 * @param expGain
	 *            - the amount of experience to add
	 */
	public void gainExp(GameController game, int expGain) {
		this.curExp += expGain;
		DebugUtility.printMessage("Gained " + expGain + " exp");

		game.addMessage(getName() + " gained " + expGain + " exp!");
		while (this.curExp >= (this.level + 1) * (this.level + 1) * (this.level + 1)) {
			levelUp(game);
		}
	}

	/**
	 * Try to thaw this party member - 20% chance of returning to normal status
	 */
	public void tryToThaw() {
		Random rr = new Random();
		if (rr.nextInt(5) <= 1) {
			if (getStatusEffect() == STATUS.SLP) {
				// TODO - change to message boxes
				DebugUtility.printMessage(getName() + " has woken up.");
			}
			if (getStatusEffect() == STATUS.FRZ) {
				// TODO - change to message boxes
				DebugUtility.printMessage(getName() + " has broken free from the ice.");
			}
			setStatusEffect(STATUS.NORMAL);
		} else {
			if (getStatusEffect() == STATUS.SLP) {
				DebugUtility.printMessage(getName() + " is still asleep.");
			}
			if (getStatusEffect() == STATUS.FRZ) {
				DebugUtility.printMessage(getName() + " is frozen solid.");
			}
		}
	}

	/**
	 * Level up, increase stats, check for evolution, check for moves learned
	 * 
	 * @param game
	 */
	public void levelUp(GameController game) {
		if (this.level < 100) {
			this.level += 1;
			// TODO - convert to use message box
			game.addMessage(getName() + " grew to level " + level + "!");
			DebugUtility.printMessage(getName() + " grew to level " + level + "!");
		}
		for (STAT key : stats.keySet()) {
			if (key != STAT.ACCURACY) {
				int incr = RandomNumUtils.randomStatIncr();
				int new_value = stats.get(key) + incr;

				if (new_value > 240) {
					new_value = 240;
				}

				stats.put(key, new_value);
			}
		}
		for (STAT key : maxStats.keySet()) {
			if (key != STAT.ACCURACY) {
				int incr = RandomNumUtils.randomStatIncr();
				int new_value = maxStats.get(key) + incr;

				if (new_value > 240) {
					new_value = 240;
				}

				maxStats.put(key, new_value);
			}
		}
		if ((this.evolution_stage < 2) && (this.level == pData.evolution_levels.get(evolution_stage + 1))) {
			this.evolution_stage += 1;
			// TODO Convert to use message box
			DebugUtility.printMessage("Congratulations!  Your " + pData.evolution_stages.get(evolution_stage - 1)
					+ " has evolved into a " + pData.evolution_stages.get(evolution_stage) + "!");
			updateSprites();
		}
		for (int x = 0; x < pData.movesLearned.size(); x++) {
			if (level == pData.levelsLearned.get(x)) {
				this.moves.add(MoveLibrary.getInstance().get(pData.movesLearned.get(x)));
				// TODO convert to use message box
				DebugUtility.printMessage(getName() + " learned " + pData.movesLearned.get(x));
			}
		}
	}

	/**
	 * Deals a given amount of damage to this Pokemon
	 * 
	 * @param damage
	 *            - the amount of damage to deal
	 */
	public void doDamage(int damage) {
		setStat(STAT.HP, getStat(STAT.HP) - damage);
		if (getStat(STAT.HP) < 0) {
			setStat(STAT.HP, 0);
		}
	}

	/**
	 * Deals damage based on the given move
	 * 
	 * TODO implement "STAT" damage
	 * 
	 * @param move
	 *            - the move data of the opponent's move
	 */
	public void doDamage(MoveData move) {
		int attackStat = 0;
		int defStat = 0;
		if (move.category == MOVECATEGORY.PHYSICAL) {
			attackStat = (getStat(STAT.ATTACK));
			defStat = getStat(STAT.DEFENSE);
		}
		if (move.category == MOVECATEGORY.SPECIAL) {
			attackStat = (getStat(STAT.SP_ATTACK));
			defStat = getStat(STAT.SP_DEFENSE);
		}
		int damage = 0;
		if (move.category != MOVECATEGORY.STAT) {
			damage = (int) (((2 * (getLevel() / 5 + 2) * move.power * attackStat / 50 / defStat + 2)
					* RandomNumUtils.generateRandom(85, 100) / 100.0));
		}
		move.movePP--;

		DebugUtility.printMessage("Dealing " + damage + " damage to " + getName());
		doDamage(damage);
	}

	/**
	 * Restore the given amount of health
	 * 
	 * @param amount
	 *            - the amount to heal
	 */
	public void heal(int amount) {
		int hp = stats.get(STAT.HP) + amount;
		if (hp > maxStats.get(STAT.HP)) {
			fullHeal();
		} else {
			// restore the given amount
			stats.put(STAT.HP, hp);
		}
	}

	/**
	 * Restore all health
	 */
	public void fullHeal() {
		stats.put(STAT.HP, maxStats.get(STAT.HP));
	}

	/**
	 * Get the current value of a stat
	 * 
	 * @param stat
	 *            - the STAT to get the value of
	 * @return int value of the STAT
	 */
	public int getStat(STAT stat) {
		return stats.get(stat);
	}

	/**
	 * Get the maximum value of a stat
	 * 
	 * @param stat
	 *            - the STAT to get the value of
	 * @return int max value of stat
	 */
	public int getMaxStat(STAT stat) {
		return maxStats.get(stat);
	}

	/**
	 * Set a stat to the given value
	 * 
	 * @param stat
	 *            - the stat to set
	 * @param value
	 *            - the value to set it to
	 */
	private void setStat(STAT stat, int value) {
		stats.put(stat, value);
	}

	/**
	 * Return the name of this PartyMember
	 * 
	 * TODO custom names
	 * 
	 * @return the string name of Party member
	 */
	public String getName() {
		return pData.evolution_stages.get(evolution_stage);
	}

	/**
	 * The current level of this party member
	 * 
	 * @return int level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Get the chosen move
	 * 
	 * @param choice
	 *            - index of the move
	 * @return MoveData representing chosen move
	 */
	public MoveData getMove(int choice) {
		return moves.get(choice);
	}

	/**
	 * Retrieve the number of moves this party member knows
	 * 
	 * @return int number of moves
	 */
	public int getNumMoves() {
		return moves.size();
	}

	/**
	 * Has this Party member participated in the current battle?
	 * 
	 * @return whether or not party member participated
	 */
	public boolean hasParticipated() {
		return this.participated;
	}

	/**
	 * Set whether or not the party member participated in the current battle
	 */
	public void setParticipated() {
		this.participated = true;
	}

	/**
	 * Get the image used as the party icon for this party member
	 * 
	 * @return ImageIcon party icon
	 */
	public ImageIcon getIcon() {
		return this.party_icon;
	}

	/**
	 * Retrieve the image used as the back battle sprite
	 * 
	 * @return ImageIcon back sprite
	 */
	public ImageIcon getBackSprite() {
		return this.back_sprite;
	}

	/**
	 * Retrieve the image used as the front battle sprite
	 * 
	 * @return ImageIcon front sprite
	 */
	public ImageIcon getFrontSprite() {
		return this.front_sprite;
	}

	/**
	 * String representation of the PartyMember
	 * 
	 * @return String representation
	 */
	public String toString() {
		String retStr = pData.toString();
		retStr += "Level: " + this.level + "\n";
		retStr += "Cur Exp: " + this.curExp + "\n";
		int expNext = (int) Math.pow(this.level + 1, 3);
		retStr += "Next level: " + expNext + "\n";
		retStr += "Remainder: " + (expNext - curExp) + " \n";
		return retStr;
	}

	/**
	 * takes the base number, adds the current evolution stage, and then formats
	 * it to 3 digits
	 * 
	 * @param evolutionStage
	 *            - the current evolution stage
	 * @return String representation of the pokedex number
	 */
	public String formatPokedexNumber() {
		return String.format("%03d", Integer.parseInt(this.pData.pokedexNumber) + evolution_stage);
	}

	/**
	 * Retrieve this party member's current battle status
	 * 
	 * @return STATUS current status
	 */
	public STATUS getStatusEffect() {
		return statusEffect;
	}

	/**
	 * Set this party member's current battle status
	 * 
	 * @param statusEffect
	 *            the effect to apply to the party member
	 */
	public void setStatusEffect(STATUS statusEffect) {
		this.statusEffect = statusEffect;
	}
}