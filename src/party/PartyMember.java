package party;

import graphics.SpriteLibrary;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import javax.swing.ImageIcon;

import party.MoveData.MOVECATEGORY;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// ParyMember - generated from PokemonData, calculates stats based on a level
// and holds moves + sprite data
//
// ////////////////////////////////////////////////////////////////////////
public class PartyMember implements Serializable {
	private static final long serialVersionUID = 3959217221984077560L;

	public static enum STAT {
		HP, ATTACK, DEFENSE, SP_ATTACK, SP_DEFENSE, SPEED, ACCURACY
	}

	public static enum STATUS {
		PZN, BRN, FRZ, SLP, PAR, NORMAL
	}

	private int evolution_stage = 0;
	private int level;
	private boolean participated = false;
	private MoveList moves;
	private int curExp;
	private ImageIcon party_icon, back_sprite, front_sprite;
	private STATUS statusEffect = STATUS.NORMAL;

	PartyMemberData pData;

	private HashMap<STAT, Integer> stats = new HashMap<STAT, Integer>();
	private HashMap<STAT, Integer> maxStats = new HashMap<STAT, Integer>();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - given PokemonData and a level, fill in the blanks
	// (stats, evolution stage, moves, sprites)
	//
	// ////////////////////////////////////////////////////////////////////////
	public PartyMember(PartyMemberData pData, int lev) {
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

		this.party_icon = SpriteLibrary.createImage(SpriteLibrary.libPath + "Icons/icon" + formatPokedexNumber(0)
				+ ".png");
		this.back_sprite = SpriteLibrary.createImage(SpriteLibrary.libPath + "Battlers/"
				+ formatPokedexNumber(evolution_stage) + "b.png");
		this.front_sprite = SpriteLibrary.createImage(SpriteLibrary.libPath + "Battlers/"
				+ formatPokedexNumber(evolution_stage) + ".png");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getExpGain - calculate the exp gained from defeating a pokemon in the
	// current battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getExpGain(boolean trainerOwned, int numParticipants) {
		double a = trainerOwned ? 1.5 : 1;
		int b = pData.baseExp;
		return (int) (a * b * level) / (7 * numParticipants);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// gainExp - give the pokemon the calculated exp
	// if it's over the next level amount, level up
	//
	// ////////////////////////////////////////////////////////////////////////
	public void gainExp(int expGain) {
		this.curExp += expGain;
		// TODO - convert to use message box
		DebugUtility.printMessage("Gained " + expGain + " exp");
		if (this.curExp >= (this.level + 1) * (this.level + 1) * (this.level + 1)) {
			levelUp();
		}
	}

	//
	// TODO change to use BATTLE_STATUS enum
	//
	public void tryToThaw() {
		Random rr = new Random();
		if (rr.nextInt(5) <= 1) {
			if (getStatusEffect() == STATUS.SLP) {
				// TODO - convert to use message box
				DebugUtility.printMessage(getName() + " has woken up.");
			}
			if (getStatusEffect() == STATUS.FRZ) {
				// TODO - convert to use message box
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Level up, increase stats, check for evolution, check for moves learned
	//
	// ////////////////////////////////////////////////////////////////////////
	public void levelUp() {
		if (this.level < 100) {
			this.level += 1;
			// TODO - convert to use message box
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
		if ((this.evolution_stage < 2) && (this.level == pData.evolution_levels.get(evolution_stage + 1))) {
			this.evolution_stage += 1;
			// TODO Convert to use message box
			DebugUtility.printMessage("Congratulations!  Your " + pData.evolution_stages.get(evolution_stage - 1)
					+ " has evolved into a " + pData.evolution_stages.get(evolution_stage) + "!");
		}
		for (int x = 0; x < pData.movesLearned.size(); x++) {
			if (level == pData.levelsLearned.get(x))
				// TODO convert to use message box
				DebugUtility.printMessage(getName() + " learned " + pData.movesLearned.get(x));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Deals a given amount of damage to this Pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public void doDamage(int damage) {
		setStat(STAT.HP, getStat(STAT.HP) - damage);
		if (getStat(STAT.HP) < 0) {
			setStat(STAT.HP, 0);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Deals damage based on the given move
	//
	// ////////////////////////////////////////////////////////////////////////
	public void doDamage(MoveData move) {
		int attackStat = 0;
		int defStat = 0;
		DebugUtility.printMessage(move.toString());
		if (move.category == MOVECATEGORY.PHYSICAL) {
			attackStat = (getStat(STAT.ATTACK));
			defStat = getStat(STAT.DEFENSE);
			DebugUtility.printMessage("Using physical attack...");
			DebugUtility.printMessage("Defense stat is: " + defStat);
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
		doDamage(damage);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Restore the given amount of health
	//
	// ////////////////////////////////////////////////////////////////////////
	public void heal(int amount) {
		int hp = stats.get(STAT.HP) + amount;
		if (hp > maxStats.get(STAT.HP)) {
			fullHeal();
		} else {
			// restore the given amount
			stats.put(STAT.HP, hp);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Restore all health
	//
	// ////////////////////////////////////////////////////////////////////////
	public void fullHeal() {
		stats.put(STAT.HP, maxStats.get(STAT.HP));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the current value of a stat
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getStat(STAT stat) {
		return stats.get(stat);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the maximum value of a stat
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getMaxStat(STAT stat) {
		return maxStats.get(stat);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Set a stat to the given value
	//
	// ////////////////////////////////////////////////////////////////////////
	private void setStat(STAT stat, int i) {
		stats.put(stat, i);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the name of this PartyMember
	//
	// TODO - custom names
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getName() {
		return pData.evolution_stages.get(evolution_stage);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the level of this PartyMember
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getLevel() {
		return level;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the chosen move
	//
	// ////////////////////////////////////////////////////////////////////////
	public MoveData getMove(int choice) {
		return moves.get(choice);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the number of moves this PartyMember knows
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getNumMoves() {
		return moves.size();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return whether or not this PartyMember has participated
	// in the current battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean hasParticipated() {
		return this.participated;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Set whether or not the PartyMember has participated in
	// the current battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setParticipated() {
		this.participated = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the image used as the party icon for this PartyMember
	//
	// ////////////////////////////////////////////////////////////////////////
	public ImageIcon getIcon() {
		return this.party_icon;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the image used as the back battle sprite
	//
	// ////////////////////////////////////////////////////////////////////////
	public ImageIcon getBackSprite() {
		return this.back_sprite;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the image used as the front battle sprite
	//
	// ////////////////////////////////////////////////////////////////////////
	public ImageIcon getFrontSprite() {
		return this.front_sprite;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Debug information related to PartyMember
	//
	// ////////////////////////////////////////////////////////////////////////
	public String toString() {
		return pData.toString() + "Level: " + this.level;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatPokedexNumber - takes the base number, adds the stage, and then
	// formats it to 3 digits
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatPokedexNumber(int evolutionStage) {
		return String.format("%03d", Integer.parseInt(this.pData.pokedexNumber) + evolutionStage);
	}

	public STATUS getStatusEffect() {
		return statusEffect;
	}

	public void setStatusEffect(STATUS statusEffect) {
		this.statusEffect = statusEffect;
	}
}