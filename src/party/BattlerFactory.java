package party;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import location.Location;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * Look through the Party Member data file and map name->data. Factory pattern
 * can create Party Members from this data given a level
 */
public class BattlerFactory extends HashMap<String, BattlerData> {

	private static final long serialVersionUID = 6443726917122609755L;
	private static BattlerFactory m_instance = new BattlerFactory();

	/**
	 * Private constructor allows resource loading to happen once and only once
	 */
	private BattlerFactory() {
		String filename = "resources/data/PartyInfo.json";
		populateMap(filename);
	}

	/**
	 * Reads the JSON formatted file and parses out the pokemon data
	 * 
	 * @param filename
	 *            - the JSON file where the data is located
	 */
	public void populateMap(String filename) {
		JSONParser parser = new JSONParser();
		try (FileReader reader = new FileReader(filename)) {
			JSONObject all_pokemon = (JSONObject) parser.parse(reader);
			reader.close();

			// this is the JSON object that is labeled "pokedex": an array of
			// all pokemon data
			JSONArray pokemon_data_all = (JSONArray) all_pokemon.get("pokemon");

			// iterate through the array of pokemon data
			Iterator<?> pokedex = pokemon_data_all.iterator();
			while (pokedex.hasNext()) {
				BattlerData pd = new BattlerData();

				// this is the JSON object for a pokemon
				JSONObject pokemon_data = (JSONObject) pokedex.next();

				// get the pokemon's name
				pd.name = (String) pokemon_data.get("pName");

				// get the pokemon's pokedex number
				pd.pokedexNumber = Integer.parseInt((String) pokemon_data.get("pokedex_num"));

				// get the pokemon's type
				pd.type = (String) pokemon_data.get("type");

				// get the pokemon's base experience
				pd.baseExp = Integer.parseInt(((String) pokemon_data.get("base_exp")));

				// get the pokemon's evolution stage names
				pd.evolution_stages = new ArrayList<>();
				pd.evolution_levels = new ArrayList<>();
				JSONArray json_evolutions = (JSONArray) pokemon_data.get("evolution_stages");
				Iterator<?> evolution_it = json_evolutions.iterator();
				while (evolution_it.hasNext()) {
					JSONObject stage = (JSONObject) evolution_it.next();
					pd.evolution_stages.add((String) stage.get("name"));
					pd.evolution_levels.add(Integer.valueOf(((String) stage.get("level")).toString()));
				}

				// get the pokemon's moves & levels they learn those moves
				pd.movesLearned = new ArrayList<>();
				pd.levelsLearned = new ArrayList<>();

				JSONArray json_moves = (JSONArray) pokemon_data.get("moves");
				Iterator<?> moves_iterator = json_moves.iterator();
				while (moves_iterator.hasNext()) {
					JSONObject move = (JSONObject) moves_iterator.next();
					pd.movesLearned.add((String) move.get("name"));
					pd.levelsLearned.add(Integer.valueOf((String) move.get("level")));
				}

				if (pd.isValidData()) {
					put(pd.name, pd);
				} else if (pd.name != null) {
					DebugUtility.printError("Unable to load data for pokemon: " + pd.name);
				} else {
					DebugUtility.error("Unknown error in populating pokemon map.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			DebugUtility.error("Unable to load Pokedex data.");
		}
	}

	/**
	 * Return the one and only instance of this singleton for use
	 * 
	 * @return this factory instance
	 */
	public static BattlerFactory getInstance() {
		return m_instance;
	}

	/**
	 * Get the data from the Pokemon map, and create a new pokemon at given lvl
	 * 
	 * @param name
	 *            - the name of the party member to find data for
	 * @param level
	 *            - the level of the party member to generate
	 * @return a new Party Member from the retrieved data
	 */
	public static Battler createPokemon(String name, int level) {
		return new Battler(m_instance.get(name), level);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	//
	//
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Given a location, generate a random party member from the wild battler
	 * chart of that region
	 * 
	 * @param location
	 *            - where the party member should be generated from
	 * @return a new party member
	 */
	public static Battler randomPokemon(Location location) {
		String name = location.getPokemon(RandomNumUtils.generateRandom(0, 100));
		int level = RandomNumUtils.randomLevel(location.getMaxLevel(name), location.getMinLevel(name));
		return createPokemon(name, level);
	}
}