package party;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Holds all the data needed to construct a Pokemon: name, number, evolution
 * stages + levels, baseExp, moves + levels learned
 * 
 * TODO consolidate evol_stages & evol_levels to MAP TODO consolidate
 * movesLearned & levelsLearned to MAP
 */
public class BattlerData implements Serializable {

	private static final long serialVersionUID = 6172701662394420909L;

	/**
	 * Name of the party member
	 */
	public String name = null;
	/**
	 * Party member's battle type
	 */
	public String type = null;
	/**
	 * Party members Pokedex number
	 */
	public int pokedexNumber = -2;
	/**
	 * The names this party member reaches at further evolutions
	 */
	public ArrayList<String> evolution_stages = null;
	/**
	 * The levels at which this party member evolves
	 */
	public ArrayList<Integer> evolution_levels = null;
	/**
	 * The base EXP gained for beating one of these Party members
	 */
	public int baseExp = 0;
	/**
	 * The names of moves learned
	 */
	public ArrayList<String> movesLearned = null;
	/**
	 * The corresponding levels at which those moves are learned
	 */
	public ArrayList<Integer> levelsLearned = null;

	/**
	 * Get string representation of the party member's data
	 * 
	 * @return string representation
	 */
	@Override
	public String toString() {
		String retStr = "Name: " + this.name + "\n";
		retStr += "PokedexNumber: " + this.pokedexNumber + "\n";
		retStr += "Base exp: " + this.baseExp + "\n";
		retStr += "Stages: \n";
		for (int x = 0; x < this.evolution_stages.size(); x++) {
			retStr += "  -" + this.evolution_stages.get(x) + " : " + this.evolution_levels.get(x) + "\n";
		}
		retStr += "Moves:\n";
		for (int x = 0; x < this.movesLearned.size(); x++) {
			retStr += "  -" + this.movesLearned.get(x) + " : " + this.levelsLearned.get(x) + "\n";
		}
		return retStr;
	}

	/**
	 * Check if all fields were populated correctly
	 * 
	 * @return whether or not the data is valid
	 */
	public boolean isValidData() {
		return (this.type != null && this.pokedexNumber != -2 && this.evolution_stages != null
				&& this.evolution_levels != null && this.baseExp != 0 && this.movesLearned != null
				&& this.levelsLearned != null);
	}
}