package trainers;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import graphics.SpriteLibrary;
import model.Coordinate;
import party.BattlerFactory;
import party.Party;
import trainers.Actor.DIR;
import utilities.DebugUtility;

/**
 * Creating each NPC and adding them to the list of active NPCs
 */
public class NPCLibrary extends HashMap<String, Actor> {

	/**
	 * Serialization variable
	 */
	private static final long serialVersionUID = 3371121853217236345L;
	/**
	 * Single instance of this library
	 */
	private static NPCLibrary m_instance = new NPCLibrary();

	/**
	 * Private constructor ensures only one instance. Loads data from the
	 * NPCs.json file
	 */
	private NPCLibrary() {
		String filename = "resources/data/NPCs.json";
		populateMap(filename);
	}

	/**
	 * Return the single instance of this library
	 * 
	 * @return single library instance
	 */
	public static NPCLibrary getInstance() {
		return m_instance;
	}

	/**
	 * Reads the json formatted file and processes out the data
	 * 
	 * @param filename
	 *            - the file to use to populate the map
	 */
	public void populateMap(String filename) {
		JSONParser parser = new JSONParser();
		try (FileReader reader = new FileReader(filename)) {
			// this is the JSON main object for all NPCs
			JSONObject all_npcs = (JSONObject) parser.parse(reader);
			reader.close();
			// this is the JSON object that contains all NPC data as an array
			JSONArray npc_data_all = (JSONArray) all_npcs.get("NPCs");

			// iterate through the array of NPC data to create the NPCs
			Iterator<?> npcs_it = npc_data_all.iterator();
			while (npcs_it.hasNext()) {
				ActorData td = new ActorData();

				// this is the JSON object for a single NPC
				JSONObject npc_data = (JSONObject) npcs_it.next();

				// get the NPCs name
				td.name = (String) npc_data.get("name");

				// get the NPCs Location
				JSONObject pos_coords = (JSONObject) npc_data.get("location");
				int x = ((Long) pos_coords.get("x")).intValue();
				int y = ((Long) pos_coords.get("y")).intValue();
				td.position = new Coordinate(x, y);

				// get npc stationary true/false
				td.stationary = ((Boolean) npc_data.get("stationary")).booleanValue();

				// get npc sprite images directory name (sprite image name)
				td.sprite_name = (String) npc_data.get("image");
				td.sprite = SpriteLibrary.getSpriteForDir(td.sprite_name, DIR.SOUTH);

				// get the conversation text
				JSONArray conversation = (JSONArray) npc_data.get("speech_text");
				// convert to arraylist and push to trainerdata
				ArrayList<String> list = new ArrayList<>();
				Iterator<?> convo_it = conversation.iterator();
				while (convo_it.hasNext()) {
					list.add((String) convo_it.next());
				}
				td.conversationText = list;

				// get the trainer's pokemon
				JSONArray pkmn_json = (JSONArray) npc_data.get("pokemon");
				td.trainer = (pkmn_json == null) ? false : true;

				if (td.trainer && pkmn_json != null) {
					// convert to PokemonList and push to trainerdata
					Party pList = new Party();
					Iterator<?> pkmn_it = pkmn_json.iterator();
					while (pkmn_it.hasNext()) {
						JSONObject pokemon = (JSONObject) pkmn_it.next();
						String name = (String) pokemon.get("name");
						int level = ((Long) pokemon.get("level")).intValue();
						pList.add(BattlerFactory.createPokemon(name, level));
					}

					td.party = pList;
					td.money = ((Long) npc_data.get("money")).intValue();
				}

				put(td.name, new Actor(td));
			}
		} catch (Exception e) {
			e.printStackTrace();
			DebugUtility.error("Unable to load NPC data.\n" + e.getMessage());
		}
	}
}