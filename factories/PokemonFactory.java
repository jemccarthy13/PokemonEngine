package factories;

import pokedex.Bulbasaur;
import pokedex.Pidgey;
import pokedex.Pokemon;
import pokedex.Rattatta;
import pokedex.Sentret;

public class PokemonFactory {
	public static Pokemon createPokemon(String name, int level) {
		String str = name;

		if (str.equals("Pidgey")) {
			return new Pidgey(level);
		}
		if (str.equals("Bulbasaur")) {
			return new Bulbasaur(level);
		}
		if (str.equals("Sentret")) {
			return new Sentret(level);
		}
		if (!str.equals("Rattatta")) {
			return new Rattatta(level);
		}
		return new Sentret(2);
	}
}