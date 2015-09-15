package party;

import java.io.Serializable;
import java.util.ArrayList;

// ////////////////////////////////////////////////////////////////////////
//
// Holds all the data needed to construct a Pokemon:
// name, number, evolution stages + levels, baseExp, moves + levels learned
//
// ////////////////////////////////////////////////////////////////////////
public class PartyMemberData implements Serializable {

	private static final long serialVersionUID = 6172701662394420909L;

	public String name = null;
	public String type = null;
	public String pokedexNumber = null;
	public ArrayList<String> evolution_stages = null;
	public ArrayList<Integer> evolution_levels = null;
	public int baseExp = 0;
	public ArrayList<String> movesLearned = null;
	public ArrayList<Integer> levelsLearned = null;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get string representation of this pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - generates PokemonData from a given file + validates input
	//
	// ////////////////////////////////////////////////////////////////////////
	public PartyMemberData() {}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Check if all fields were populated correctly
	//
	// ////////////////////////////////////////////////////////////////////////
	public boolean isValidData() {
		return (this.type != null && this.pokedexNumber != null && this.evolution_stages != null
				&& this.evolution_levels != null && this.baseExp != 0 && this.movesLearned != null && this.levelsLearned != null);
	}
}