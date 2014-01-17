package factories;

import graphics.NPC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import utilities.EnumsAndConstants.DIR;
import data_structures.NPCData;

public class NPCFactory {

	public NPCDataMap npcData = new NPCDataMap();

	public ArrayList<NPC> npcs = new ArrayList<NPC>();

	// public NPC PROFESSOROAK;

	class NPCDataMap extends HashMap<String, NPCData> {
		private static final long serialVersionUID = 1L;
		String spritePath = "graphics_lib/Characters/NPC";

		public NPCDataMap() {
			String path = "Data/NPCs";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					NPCData pd = new NPCData(listOfFiles[i].getPath());
					put(pd.name, pd);
				}
			}
		}
	}

	public ArrayList<NPC> getAll() {
		ArrayList<NPC> intermediate = new ArrayList<NPC>();
		for (String key : npcData.keySet()) {
			NPC newNPC = new NPC(npcData.get(key));
			newNPC.setDirection(DIR.SOUTH);
			intermediate.add(newNPC);
		}
		return intermediate;
	}

	public NPC getNPC(String name) {
		return new NPC(npcData.get(name));
	}

	public NPCFactory() {

		npcs = getAll();
	}
}
