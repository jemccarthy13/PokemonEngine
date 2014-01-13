package factories;

import java.io.File;
import java.util.HashMap;

import data_structures.Pokemon;
import data_structures.PokemonData;

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

	public Pokemon createPokemon(String name, int level) {

		return new Pokemon(pokemonData.get(name), level);
	}
}