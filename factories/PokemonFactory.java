package factories;

import java.util.HashMap;

import data_structures.Pokemon;
import data_structures.PokemonData;

public class PokemonFactory {

	class PokemonDataMap extends HashMap<String, PokemonData> {

		private static final long serialVersionUID = 1L;

		public PokemonDataMap() {
			this.put("Pidgey", new PokemonData("Data/Pokemon/PIDGEY.PDAT"));
			this.put("Sentret", new PokemonData("Data/Pokemon/SENTRET.PDAT"));
			this.put("Bulbasaur", new PokemonData("Data/Pokemon/BULBASAUR.PDAT"));
			this.put("Rattatta", new PokemonData("Data/Pokemon/RATTATTA.PDAT"));
			this.put("Charmander", new PokemonData("Data/Pokemon/CHARMANDER.PDAT"));
		}
	}

	PokemonDataMap pokemonData = new PokemonDataMap();

	public Pokemon createPokemon(String name, int level) {

		return new Pokemon(pokemonData.get(name), level);
	}
}