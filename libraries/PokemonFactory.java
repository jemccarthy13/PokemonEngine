package libraries;

import java.io.File;
import java.util.HashMap;

import pokedex.Pokemon;
import pokedex.PokemonData;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Pokemon data directory, map name->data
//
// called "factory" because it can create pokemon from this data given
// a level
//
// ////////////////////////////////////////////////////////////////////////
public class PokemonFactory {

	class PokemonDataMap extends HashMap<String, PokemonData> {

		private static final long serialVersionUID = 1L;

		public PokemonDataMap() {
			String path = "Data/Pokemon";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					PokemonData pd = new PokemonData(listOfFiles[i].getPath());
					put(pd.evolution_stages.get(0), pd);
				}
			}
		}
	}

	PokemonDataMap pokemonData = new PokemonDataMap();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the data from the Pokemon map, and create a new pokemon at given lvl
	//
	// ////////////////////////////////////////////////////////////////////////
	public Pokemon createPokemon(String name, int level) {
		return new Pokemon(pokemonData.get(name), level);
	}
}