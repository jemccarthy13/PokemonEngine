package data_structures;

import java.io.Serializable;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	Integer[] maxLevel;
	Integer[] minLevel;
	String[] pokemon;
	double[] probabilities;
	TrainerList trainers;

	public String getName() {
		return this.name;
	}

	public String getPokemon(long name_number) {
		String retStr = null;
		if (name_number < this.probabilities[0]) {
			return this.pokemon[0];
		}
		for (int x = 0; x < this.probabilities.length; x++) {
			if ((name_number >= this.probabilities[x]) && (name_number <= this.probabilities[(x + 1)])) {
				return this.pokemon[(x + 1)];
			}
		}
		return retStr;
	}

	public int foundSize() {
		return this.pokemon.length;
	}

	public String getFound() {
		String retStr = "";
		for (int x = 0; x < this.pokemon.length; x++) {
			retStr = retStr + this.pokemon[x] + " ";
		}
		retStr = retStr.trim();
		return retStr;
	}

	Location(String location, int min, int max, String[] found) {
		this.name = location;
		this.maxLevel = new Integer[found.length];
		this.minLevel = new Integer[found.length];
		this.probabilities = new double[found.length];
		for (int x = 0; x < found.length; x++) {
			this.maxLevel[x] = Integer.valueOf(max);
			this.minLevel[x] = Integer.valueOf(min);
		}
		this.pokemon = found;
	}

	public Location(String n) {
		this.name = n;
	}

	public int getMinLevel(String name) {
		for (int x = 0; x < this.pokemon.length; x++) {
			if (name.equals(this.pokemon[x])) {
				return this.minLevel[x].intValue();
			}
		}
		return -1;
	}

	public int getMaxLevel(String name) {
		for (int x = 0; x < this.pokemon.length; x++) {
			if (name.equals(this.pokemon[x])) {
				return this.maxLevel[x].intValue();
			}
		}
		return -1;
	}

	public Object getTrainers() {
		return this.trainers;
	}
}