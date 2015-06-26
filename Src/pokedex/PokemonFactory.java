package pokedex;

import java.io.File;
import java.util.HashMap;

import location.Location;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Pokemon data directory, map name->data
//
// Can create pokemon from this data given a level
//
// ////////////////////////////////////////////////////////////////////////
public class PokemonFactory extends HashMap<String, PokemonData> {

	private static final long serialVersionUID = 6443726917122609755L;

	private static PokemonFactory m_instance = new PokemonFactory();

	private PokemonFactory() {
		File[] listOfFiles = new File("Data/Pokemon").listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				PokemonData pd = new PokemonData(listOfFiles[i].getPath());
				if (pd.isValidData())
					put(pd.evolution_stages.get(0), pd);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the one and only instance of this singleton for use
	//
	// ////////////////////////////////////////////////////////////////////////
	public static PokemonFactory getInstance() {
		return m_instance;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the data from the Pokemon map, and create a new pokemon at given lvl
	//
	// ////////////////////////////////////////////////////////////////////////
	public Pokemon createPokemon(String name, int level) {
		return new Pokemon(m_instance.get(name), level);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Given a location, generate a random pokemon from the
	// wild pokemon chart in that region
	//
	// ////////////////////////////////////////////////////////////////////////
	public Pokemon randomPokemon(Location location) {
		String name = location.getPokemon(RandomNumUtils.generateRandom(0, 100));
		int level = RandomNumUtils.randomLevel(location.getMaxLevel(name), location.getMinLevel(name));
		return new Pokemon(m_instance.get(name), level);
	}
}