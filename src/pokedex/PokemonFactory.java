package pokedex;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Private constructor allows resource loading to happen once and only once
	//
	// ////////////////////////////////////////////////////////////////////////
	private PokemonFactory() {
		String filename = "resources/data/Pokedex.json";
		populateMap(filename);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Reads the JSON formatted file and parses out the pokemon data
	//
	// ////////////////////////////////////////////////////////////////////////
	public void populateMap(String filename) {
		JSONParser parser = new JSONParser();
		try {

			// this is the JSON object for the entire file
			JSONObject all_pokemon = (JSONObject) parser.parse(new FileReader(filename));

			// this is the JSON object that is labeled "pokedex": an array of
			// all pokemon data
			JSONArray pokemon_data_all = (JSONArray) all_pokemon.get("pokemon");

			// iterate through the array of pokemon data
			Iterator<?> pokedex = pokemon_data_all.iterator();
			while (pokedex.hasNext()) {
				PokemonData pd = new PokemonData();

				// this is the JSON object for a pokemon
				JSONObject pokemon_data = (JSONObject) pokedex.next();

				// get the pokemon's name
				pd.name = (String) pokemon_data.get("pName");

				// get the pokemon's pokedex number
				pd.pokedexNumber = (String) pokemon_data.get("pokedex_num");

				// get the pokemon's type
				pd.type = (String) pokemon_data.get("type");

				// get the pokemon's base experience
				pd.baseExp = ((Long) pokemon_data.get("base_exp")).intValue();

				// get the pokemon's evolution stage names
				pd.evolution_stages = new ArrayList<String>();
				pd.evolution_levels = new ArrayList<Integer>();
				JSONArray json_evolutions = (JSONArray) pokemon_data.get("evolution_stages");
				Iterator<?> evolution_it = json_evolutions.iterator();
				while (evolution_it.hasNext()) {
					JSONObject stage = (JSONObject) evolution_it.next();
					pd.evolution_stages.add((String) stage.get("name"));
					pd.evolution_levels.add(((Long) (stage.get("level"))).intValue());
				}

				// get the pokemon's moves & levels they learn those moves
				pd.moves = new ArrayList<String>();
				pd.levelsLearned = new ArrayList<Integer>();

				JSONArray json_moves = (JSONArray) pokemon_data.get("moves");
				Iterator<?> moves_iterator = json_moves.iterator();
				while (moves_iterator.hasNext()) {
					JSONObject move = (JSONObject) moves_iterator.next();
					pd.moves.add((String) move.get("name"));
					pd.levelsLearned.add(((Long) (move.get("level"))).intValue());
				}

				if (pd.isValidData()) {
					put(pd.name, pd);
				} else if (pd.name != null) {
					System.err.println("Unable to load data for pokemon: " + pd.name);
				} else {
					System.err.println("Unknown error in populating pokemon map.");
				}
			}
		} catch (Exception e) {
			System.err.println("Unable to load Pokedex data.");
			e.printStackTrace();
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
		return createPokemon(name, level);
	}
}