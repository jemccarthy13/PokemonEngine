package pokedex;

import java.awt.Image;
import java.io.Serializable;

import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.STATS;
import utilities.Utils;

// ////////////////////////////////////////////////////////////////////////
//
// Pokemon - generated from PokemonData, calculates stats based on a level
// and holds moves + sprite data
//
// ////////////////////////////////////////////////////////////////////////
public class Pokemon implements Serializable {
	private static final long serialVersionUID = 1L;
	int evolution_stage = 0;
	int level;
	boolean participated = false;
	Move[] moves = new Move[4];
	public Integer[] stats = new Integer[7];
	Integer[] max_stats = new Integer[7];
	Integer accuracy = Integer.valueOf(100);
	private int curExp;
	protected Image party_icon;
	public int statusEffect;
	protected Image back_sprite;
	protected Image front_sprite;

	PokemonData pData;

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
		this.stats[STATS.HP.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[STATS.ATTACK.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[STATS.DEFENSE.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[STATS.SP_ATTACK.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[STATS.SP_DEFENSE.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[STATS.SPEED.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
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
				moves[y] = EnumsAndConstants.move_lib.getMove(pData.moves.get(idx - y));
			}
		}

		for (int x = 0; x < this.stats.length; x++) {
			if (this.stats[x].intValue() < 5) {
				this.stats[x] = Integer.valueOf(5);
			}
			this.max_stats[x] = this.stats[x];
		}

		this.party_icon = EnumsAndConstants.tk.createImage(getClass().getResource(
				EnumsAndConstants.GRAPHICS_ICONPATH + formatPokedexNumber(0) + ".png"));
		this.back_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				EnumsAndConstants.GRAPHICS_BATTLEPATH + formatPokedexNumber(evolution_stage) + "b.png"));
		this.front_sprite = EnumsAndConstants.tk.createImage(getClass().getResource(
				EnumsAndConstants.GRAPHICS_BATTLEPATH + formatPokedexNumber(evolution_stage) + ".png"));
	}

	public String formatPokedexNumber(int evolutionStage) {
		return String.format("%03d", Integer.parseInt(this.pData.pokedexNumber) + evolutionStage);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// levelUp - if at enough EXP, level up (also checks for evolution)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void levelUp() {
		if (this.level < 100) {
			this.level += 1;
			System.out.println(pData.evolution_stages.get(evolution_stage) + " grew to level " + level + "!");
		}
		for (int x = 0; x < 6; x++) {
			int incr = Utils.randomStatIncr();
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

	public int getStat(EnumsAndConstants.STATS stat) {
		return this.stats[stat.ordinal()].intValue();
	}

	public int getMaxStat(EnumsAndConstants.STATS hp) {
		return this.max_stats[hp.ordinal()].intValue();
	}

	public String toString() {
		return pData.toString() + "\n" + level;
	}

	public void doDamage(int damage, boolean playSound) {
		setStat(STATS.HP, getStat(STATS.HP) - damage);
		if (getStat(STATS.HP) < 0) {
			setStat(STATS.HP, 0);
		}
		if (playSound) {
			Utils.playDamageSound();
		}
	}

	public void doDamageWithoutSound(int damage) {
		doDamage(damage, false);
	}

	public void doDamage(int damage) {
		doDamage(damage, true);
	}

	private void setStat(EnumsAndConstants.STATS hp, int i) {
		this.stats[hp.ordinal()] = Integer.valueOf(i);
	}

	public String getName() {
		return pData.evolution_stages.get(evolution_stage);
	}

	public int getLevel() {
		return level;
	}

	public boolean hasParticipated() {
		return this.participated;
	}

	public int getExpGain(boolean trainerOwned, int numParticipants) {
		double a = trainerOwned ? 1.5 : 1;
		int b = pData.baseExp;
		return (int) (a * b * level) / (7 * numParticipants);
	}

	public void gainExp(int expGain) {
		this.curExp += expGain;
		if (this.curExp >= (this.level + 1) * (this.level + 1) * (this.level + 1)) {
			levelUp();
		}
	}

	public int getNumMoves() {
		int count = 0;
		for (Move x : moves) {
			if (x != null) {
				count++;
			}
		}

		return count;
	}

	public Move getMove(int choice) {
		return moves[choice];
	}

	public void setParticipated() {
		this.participated = true;
	}

	public void heal(int amount) {
		if (amount == -1) {
			this.stats[STATS.HP.ordinal()] = this.max_stats[STATS.HP.ordinal()];
		} else {
			int tmp40_37 = STATS.HP.ordinal();
			Integer[] tmp40_31 = this.stats;
			tmp40_31[tmp40_37] = Integer.valueOf(tmp40_31[tmp40_37].intValue() + amount);
			if (this.stats[STATS.HP.ordinal()].intValue() > this.max_stats[STATS.HP.ordinal()].intValue()) {
				heal(-1);
			}
		}
	}

	public Image getIcon() {
		return this.party_icon;
	}

	public Image getBackSprite() {
		return this.back_sprite;
	}

	public Image getFrontSprite() {
		return this.front_sprite;
	}

}
