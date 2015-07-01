package pokedex;

import java.awt.Image;
import java.io.Serializable;

import graphics.SpriteLibrary;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// Pokemon - generated from PokemonData, calculates stats based on a level
// and holds moves + sprite data
//
// ////////////////////////////////////////////////////////////////////////
public class Pokemon implements Serializable {
	private static final long serialVersionUID = 3959217221984077560L;
	int evolution_stage = 0;
	int level;
	boolean participated = false;
	MoveData[] moves = new MoveData[4];
	public Integer[] stats = new Integer[7];
	Integer[] max_stats = new Integer[7];
	Integer accuracy = Integer.valueOf(100);
	private int curExp;
	protected Image party_icon;
	public int statusEffect;
	protected Image back_sprite;
	protected Image front_sprite;

	PokemonData pData;

	public static enum STATS {
		HP, ATTACK, DEFENSE, SP_ATTACK, SP_DEFENSE, SPEED, ACCURACY;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - given PokemonData and a level, fill in the blanks
	// (stats, evolution stage, moves, sprites)
	//
	// ////////////////////////////////////////////////////////////////////////
	public Pokemon(PokemonData pData, int lev) {
		this.pData = pData;
		this.level = lev;
		for (Integer x : pData.evolution_levels) {
			if (level > x && x != 0) {
				evolution_stage++;
			}
		}
		this.stats[STATS.HP.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.ATTACK.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.DEFENSE.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.SP_ATTACK.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.SP_DEFENSE.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.SPEED.ordinal()] = Integer.valueOf(RandomNumUtils.randomBaseStat(this.level));
		this.stats[STATS.ACCURACY.ordinal()] = Integer.valueOf(100);
		this.curExp = (this.level * this.level * this.level);

		int idx = -1;
		for (int x = 0; x < pData.moves.size(); x++) {

			if (level > pData.levelsLearned.get(x)) {
				idx++;
			}
		}
		for (int y = 0; y < 4; y++) {
			if (idx - y <= pData.moves.size() && idx - y >= 0) {
				moves[y] = MoveLibrary.getInstance().get(pData.moves.get(idx - y));
			}
		}

		for (int x = 0; x < this.stats.length; x++) {
			if (this.stats[x].intValue() < 5) {
				this.stats[x] = Integer.valueOf(5);
			}
			this.max_stats[x] = this.stats[x];
		}

		this.party_icon = SpriteLibrary
				.createImage(SpriteLibrary.libPath + "Icons/icon" + formatPokedexNumber(0) + ".png");
		this.back_sprite = SpriteLibrary
				.createImage(SpriteLibrary.libPath + "Battlers/" + formatPokedexNumber(evolution_stage) + "b.png");
		this.front_sprite = SpriteLibrary
				.createImage(SpriteLibrary.libPath + "Battlers/" + formatPokedexNumber(evolution_stage) + ".png");
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
		if (this.curExp >= (this.level + 1) * (this.level + 1) * (this.level + 1)) {
			levelUp();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// levelUp - if at enough EXP, level up (also checks for evolution)
	//
	// TODO - fix tmp variables strangeness
	//
	// ////////////////////////////////////////////////////////////////////////
	public void levelUp() {
		if (this.level < 100) {
			this.level += 1;
			System.out.println(getName() + " grew to level " + level + "!");
		}
		for (int x = 0; x < 6; x++) {
			int incr = RandomNumUtils.randomStatIncr();
			if (this.stats[x].intValue() + incr == 240) {
				incr = 0;
			}
			int tmp125_124 = x;
			Integer[] tmp125_121 = this.stats;
			tmp125_121[tmp125_124] = Integer.valueOf(tmp125_121[tmp125_124].intValue() + incr);
			int tmp141_140 = x;
			Integer[] tmp141_137 = this.max_stats;
			tmp141_137[tmp141_140] = Integer.valueOf(tmp141_137[tmp141_140].intValue() + incr);
			if (this.stats[x].intValue() > 240) {
				this.stats[x] = Integer.valueOf(240);
			}
		}
		if ((this.evolution_stage < 2) && (this.level == pData.evolution_levels.get(evolution_stage + 1))) {
			this.evolution_stage += 1;
			System.out.println("Congratulations!  Your " + pData.evolution_stages.get(evolution_stage - 1)
					+ " has evolved into a " + pData.evolution_stages.get(evolution_stage) + "!");
		}
		for (int x = 0; x < pData.moves.size(); x++) {
			if (level == pData.levelsLearned.get(x))
				System.out.println(getName() + " learned " + pData.moves.get(x));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// doDamage - deals a given amount of damage to this Pokemon
	// playSound flags whether or not to play damage sound (for poison vs
	// in battle)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void doDamage(int damage) {
		setStat(STATS.HP, getStat(STATS.HP) - damage);
		if (getStat(STATS.HP) < 0) {
			setStat(STATS.HP, 0);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// heal - restore the given amount of health
	//
	// ////////////////////////////////////////////////////////////////////////
	public void heal(int amount) {
		if (amount == -1) {
			this.stats[STATS.HP.ordinal()] = this.max_stats[STATS.HP.ordinal()];
		} else {
			int hp = STATS.HP.ordinal();
			stats[hp] = Integer.valueOf(stats[hp].intValue() + amount);
			if (this.stats[STATS.HP.ordinal()].intValue() > this.max_stats[STATS.HP.ordinal()].intValue()) {
				heal(-1);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getStat - given a STAT get the current value
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getStat(STATS stat) {
		return this.stats[stat.ordinal()].intValue();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getMaxStat - given a STAT return the highest value that stat could be
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getMaxStat(STATS hp) {
		return this.max_stats[hp.ordinal()].intValue();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// setStat - set the given stat to the given value
	//
	// ////////////////////////////////////////////////////////////////////////
	private void setStat(STATS hp, int i) {
		this.stats[hp.ordinal()] = Integer.valueOf(i);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getName - return the name of this pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getName() {
		return pData.evolution_stages.get(evolution_stage);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getLevel - return the level of this pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getLevel() {
		return level;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getMove - return the chosen move
	//
	// ////////////////////////////////////////////////////////////////////////
	public MoveData getMove(int choice) {
		return moves[choice];
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getNumMoves - return the number of moves the pokemon knows
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getNumMoves() {
		int count = 0;
		for (MoveData move : moves) {
			if (move != null) {
				count++;
			}
		}
		return count;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// hasParticipated - return whether or not this pokemon has participated
	// in the current battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean hasParticipated() {
		return this.participated;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// setParticipated - set whether or not the pokemon has participated in
	// the current battle
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setParticipated() {
		this.participated = true;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getIcon - return the image used as the party icon for this pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public Image getIcon() {
		return this.party_icon;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getBackSprite - return the image used as the back battle sprite
	//
	// ////////////////////////////////////////////////////////////////////////
	public Image getBackSprite() {
		return this.back_sprite;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getFrontSprite - return the image used as the front battle sprite
	//
	// ////////////////////////////////////////////////////////////////////////
	public Image getFrontSprite() {
		return this.front_sprite;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// toString - debug information related to Pokemon
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
}
