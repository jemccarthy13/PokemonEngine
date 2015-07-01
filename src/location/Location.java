package location;

import java.io.Serializable;

///////////////////////////////////////////////////////////////////////////////
//
// Location - wrapper for a location data, access data members
//
// TODO - determine the need for this wrapper - can use LocationData directly
//
///////////////////////////////////////////////////////////////////////////////
public class Location implements Serializable {

	private static final long serialVersionUID = 6649497502085927348L;
	LocationData lData;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Constructor - constructs a location given the location data
	//
	// ////////////////////////////////////////////////////////////////////////
	public Location(LocationData lData) {
		this.lData = lData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getName - gets the name of the current location
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getName() {
		// return this.lData.name;
		return lData.name;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getPokemon - given a random number, pick the name of a weighted
	// random pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getPokemon(long name_number) {
		String retStr = "Error in getting Pokemon from location";
		int total = 0;
		for (int x = 0; x < this.lData.probabilities.size(); x++) {
			total += this.lData.probabilities.get(x);

			if (name_number <= total) {
				return this.lData.pokemon.get(x);
			}
		}
		return retStr;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getMinLevel - get the minimum level for generating the given Pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getMinLevel(String name) {
		for (int x = 0; x < this.lData.pokemon.size(); x++) {
			if (name.equals(this.lData.pokemon.get(x))) {
				return this.lData.minLevels.get(x).intValue();
			}
		}
		return -1;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getMaxLevel - get the maximum level for generating the given Pokemon
	//
	// ////////////////////////////////////////////////////////////////////////
	public int getMaxLevel(String name) {
		for (int x = 0; x < this.lData.pokemon.size(); x++) {
			if (name.equals(this.lData.pokemon.get(x))) {
				return this.lData.maxLevels.get(x).intValue();
			}
		}
		return -1;
	}
}