package data_structures;

import java.io.Serializable;

import utilities.EnumsAndConstants;

public class Move implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	int power;
	protected EnumsAndConstants.MOVETYPE type;

	public int getStrength() {
		return this.power;
	}

	public String getName() {
		return this.name;
	}

	public Move(String n, int p) {
		this.name = n;
		this.power = p;
	}

	public String toString() {
		return this.name;
	}

	public EnumsAndConstants.MOVETYPE getType() {
		return this.type;
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: data_structures.Move
 * 
 * JD-Core Version: 0.7.0.1
 */