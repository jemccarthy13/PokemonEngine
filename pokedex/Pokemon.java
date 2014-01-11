package pokedex;

import java.awt.Image;
import java.io.Serializable;

import utilities.EnumsAndConstants;
import utilities.Utils;
import data_structures.Move;
import factories.MoveFactory;

public class Pokemon implements Serializable {
	private static final long serialVersionUID = 1L;
	int evolution_stage;
	int level;
	String name;
	String type;
	String pokedexNumber;
	boolean participated = false;
	Move[] moves = new Move[4];
	public Integer[] stats = new Integer[7];
	Integer[] max_stats = new Integer[7];
	Integer accuracy = Integer.valueOf(100);
	String[] evolution_stages = new String[3];
	Integer[] evolution_levels = new Integer[3];
	int baseExp;
	private int exp;
	protected Image party_icon;
	public int statusEffect;
	protected Image back_sprite;
	protected Image front_sprite;
	protected String basePokedexNumber;

	public int getLevel() {
		return this.level;
	}

	public void setName() {
		this.name = this.evolution_stages[0];
		if ((this.evolution_levels[1] != null) && (this.level > this.evolution_levels[1].intValue())) {
			this.name = this.evolution_stages[1];
		}
		if ((this.evolution_levels[2] != null) && (this.level > this.evolution_levels[2].intValue())) {
			this.name = this.evolution_stages[2];
		}
	}

	public void setStage() {
		this.evolution_stage = 0;
		if ((this.evolution_levels[1] != null) && (this.level > this.evolution_levels[1].intValue())) {
			this.evolution_stage = 1;
		}
		if ((this.evolution_levels[2] != null) && (this.level > this.evolution_levels[2].intValue())) {
			this.evolution_stage = 2;
		}
	}

	public void reset() {
		this.accuracy = Integer.valueOf(100);
		this.participated = false;
	}

	public Move[] getMoves() {
		return this.moves;
	}

	public int getNumMoves() {
		int count = 0;
		for (int x = 0; x < this.moves.length; x++) {
			if (this.moves[x] != MoveFactory.PLACEHOLDER) {
				count++;
			}
		}
		return count;
	}

	public int getExpGain(boolean wild, int s) {
		double a = wild ? 1.0D : 1.5D;
		if ((this.evolution_stage == 2) && (this.evolution_stages[2] != null)) {
			baseExp *= 2;
		}
		if ((this.evolution_stage == 3) || ((this.evolution_stage == 2) && (this.evolution_stages[2] == null))) {
			baseExp *= 3;
		}
		return (int) (a * this.baseExp * this.level / (7 * s));
	}

	public void printMoves() {
		System.out.println("1. " + this.moves[0] + "\n2. " + this.moves[1] + "\n3. " + this.moves[2] + "\n4. "
				+ this.moves[3]);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return this.name;
	}

	public void levelUp() {
		if (this.level < 100) {
			this.level += 1;
			System.out.println(this.name + " grew to level " + this.level + "!");
		}
		for (int x = 0; x < 6; x++) {
			int incr = Utils.randomStatIncr();
			if (this.stats[x].intValue() + incr == 240) {
				incr = 0;
			}
			System.out.println(utilities.EnumsAndConstants.STATNAMES[x] + " +" + incr);
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
		if ((this.evolution_stage < 2) && (this.level == this.evolution_levels[(this.evolution_stage + 1)].intValue())) {
			this.evolution_stage += 1;
			this.name = this.evolution_stages[this.evolution_stage];
			System.out.println("Congratulations!  Your " + this.evolution_stages[(this.evolution_stage - 1)]
					+ " has evolved into a " + this.name + "!");
		}
	}

	public int getStat(EnumsAndConstants.STATS stat) {
		return this.stats[stat.ordinal()].intValue();
	}

	public int getMaxStat(EnumsAndConstants.STATS hp) {
		return this.max_stats[hp.ordinal()].intValue();
	}

	public Pokemon(int lev) {
		this.level = lev;
		this.stats[EnumsAndConstants.STATS.HP.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.ATTACK.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.DEFENSE.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.SP_ATTACK.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.SP_DEFENSE.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.SPEED.ordinal()] = Integer.valueOf(Utils.randomBaseStat(this.level));
		this.stats[EnumsAndConstants.STATS.ACCURACY.ordinal()] = Integer.valueOf(100);
		this.exp = (this.level * this.level * this.level);
		for (int y = 0; y < 4; y++) {
			this.moves[y] = MoveFactory.PLACEHOLDER;
		}
		for (int x = 0; x < this.stats.length; x++) {
			if (this.stats[x].intValue() < 5) {
				this.stats[x] = Integer.valueOf(5);
			}
			this.max_stats[x] = this.stats[x];
		}
	}

	public String toString(boolean own) {
		String retStr = "";
		if (!own) {
			retStr = retStr + this.name + " (Lvl. " + this.level + ")";
		} else {
			retStr = retStr + this.name;
		}
		return retStr;
	}

	public void doDamage(int damage, boolean playSound) {
		setStat(EnumsAndConstants.STATS.HP, getStat(EnumsAndConstants.STATS.HP) - damage);
		if (getStat(EnumsAndConstants.STATS.HP) < 0) {
			setStat(EnumsAndConstants.STATS.HP, 0);
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

	public boolean hasParticipated() {
		return this.participated;
	}

	public void gainExp(int expGain) {
		this.exp += expGain;
		if (this.exp >= (this.level + 1) * (this.level + 1) * (this.level + 1)) {
			levelUp();
		}
	}

	public void setParticipated() {
		this.participated = true;
	}

	public void heal(int amount) {
		if (amount == -1) {
			this.stats[EnumsAndConstants.STATS.HP.ordinal()] = this.max_stats[EnumsAndConstants.STATS.HP.ordinal()];
		} else {
			int tmp40_37 = EnumsAndConstants.STATS.HP.ordinal();
			Integer[] tmp40_31 = this.stats;
			tmp40_31[tmp40_37] = Integer.valueOf(tmp40_31[tmp40_37].intValue() + amount);
			if (this.stats[EnumsAndConstants.STATS.HP.ordinal()].intValue() > this.max_stats[EnumsAndConstants.STATS.HP
					.ordinal()].intValue()) {
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

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: pokedex.Pokemon
 * 
 * JD-Core Version: 0.7.0.1
 */