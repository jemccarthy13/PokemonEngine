package location;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import trainers.Actor.DIR;
import utilities.DebugUtility;

/**
 * Look through the Location data file and map Name->Data
 */
public class LocationLibrary extends HashMap<String, LocationData> {

	private static final long serialVersionUID = 9095738634104025461L;
	private static LocationLibrary m_instance = new LocationLibrary();

	/**
	 * Private constructor causes single instance behavior
	 */
	private LocationLibrary() {
		String filename = "resources/data/Locations.json";
		populateMap(filename);
	}

	/**
	 * Reads the JSON formatted file and parses out the location data
	 * 
	 * @param filename
	 *            - the path of the Locations data file
	 */
	public void populateMap(String filename) {
		JSONParser parser = new JSONParser();
		try {

			// this is the JSON object for the entire file
			FileReader reader = new FileReader(filename);
			JSONObject all_locations = (JSONObject) parser.parse(reader);
			reader.close();

			// this is the JSON object that is labeled "pokedex": an array of
			// all pokemon data
			JSONArray location_data_all = (JSONArray) all_locations.get("locations");

			// iterate through the array of pokemon data
			Iterator<?> location_it = location_data_all.iterator();
			while (location_it.hasNext()) {

				LocationData ld = new LocationData();
				// this is the JSON object for a location
				JSONObject loc_data = (JSONObject) location_it.next();

				// get the location's name
				ld.name = (String) loc_data.get("name");

				// get the pokemon's pokedex number
				ld.canFlyOutOf = (Boolean) loc_data.get("canFlyOutOf");

				// get the coordinate boundaries for this location
				ld.boundaries = new HashMap<DIR, Integer>();
				ld.boundaries.put(DIR.NORTH,
						((Long) ((JSONObject) loc_data.get("boundaries")).get("north")).intValue());
				ld.boundaries.put(DIR.SOUTH,
						((Long) ((JSONObject) loc_data.get("boundaries")).get("south")).intValue());
				ld.boundaries.put(DIR.EAST, ((Long) ((JSONObject) loc_data.get("boundaries")).get("east")).intValue());
				ld.boundaries.put(DIR.WEST, ((Long) ((JSONObject) loc_data.get("boundaries")).get("west")).intValue());

				ld.pokemon = new ArrayList<String>();
				ld.probabilities = new ArrayList<Integer>();
				ld.minLevels = new ArrayList<Integer>();
				ld.maxLevels = new ArrayList<Integer>();

				JSONArray available_pkmn = (JSONArray) loc_data.get("pokemon_available");
				Iterator<?> pkmn_it = available_pkmn.iterator();
				while (pkmn_it.hasNext()) {
					JSONObject stage = (JSONObject) pkmn_it.next();
					ld.pokemon.add((String) stage.get("name"));
					ld.probabilities.add(((Long) (stage.get("probability"))).intValue());
					ld.minLevels.add(((Long) (stage.get("min_level"))).intValue());
					ld.maxLevels.add(((Long) (stage.get("max_level"))).intValue());
				}

				if (ld.isValidData()) {
					put(ld.name, ld);
				} else if (ld.name != null) {
					DebugUtility.printError("Unable to load data for location: " + ld.name);
				} else {
					DebugUtility.error("Unknown error in populating location map.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The single instance of the LocationLibrary
	 * 
	 * @return LocationLibrary instance
	 */
	public static LocationLibrary getInstance() {
		return m_instance;
	}

	/**
	 * Retrieve a location from the location library by name
	 * 
	 * @param name
	 *            - the location to look up
	 * @return a Location by that name
	 */
	public static Location getLocation(String name) {
		return new Location(m_instance.get(name));
	}
}