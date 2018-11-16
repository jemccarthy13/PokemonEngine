package location;

import java.io.Serializable;

///////////////////////////////////////////////////////////////////////////////
//
// Location -
//
// TODO - determine the need for this wrapper - can use LocationData directly
//
///////////////////////////////////////////////////////////////////////////////
/**
 * Wrapper for a location data to provide ccess data members
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 6649497502085927348L;
	LocationData lData;

	/**
	 * Constructs a location given the location data
	 * 
	 * @param lData
	 *            - the LocationData to provide access to
	 */
	public Location(LocationData lData) {
		this.lData = lData;
	}

	/**
	 * Gets the name of the current location
	 * 
	 * @return string name
	 */
	public String getName() {
		return this.lData.name;
	}

	/**
	 * Given a random number, pick the name of a weighted random battler
	 * 
	 * @param name_number
	 *            - the number chosen
	 * @return String name of chosen battler
	 */
	public String getPokemon(long name_number) {
		String retStr = "Error in getting Pokemon from location";
		int total = 0;
		for (int x = 0; x < this.lData.probabilities.size(); x++) {
			total += this.lData.probabilities.get(x).intValue();

			if (name_number <= total) {
				return this.lData.pokemon.get(x);
			}
		}
		return retStr;
	}

	/**
	 * Get the minimum level for generating the given Battler
	 * 
	 * @param name
	 *            - the battler to look up
	 * @return int minimum level at this location
	 */
	public int getMinLevel(String name) {
		for (int x = 0; x < this.lData.pokemon.size(); x++) {
			if (name.equals(this.lData.pokemon.get(x))) {
				return this.lData.minLevels.get(x).intValue();
			}
		}
		return -1;
	}

	/**
	 * Get the maximum level for generating the given Battler
	 * 
	 * @param name
	 *            - the battler to retrieve the max level of
	 * @return int max level at this location
	 */
	public int getMaxLevel(String name) {
		for (int x = 0; x < this.lData.pokemon.size(); x++) {
			if (name.equals(this.lData.pokemon.get(x))) {
				return this.lData.maxLevels.get(x).intValue();
			}
		}
		return -1;
	}
}