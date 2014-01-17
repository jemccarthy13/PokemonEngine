package factories;

import graphics.NPC;

import java.io.File;
import java.util.HashMap;

import data_structures.NPCData;

public class NPCFactory {

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

	public NPC[] getAll() {
		return (NPC[]) NPCData.entrySet().toArray();
	}

	public NPCDataMap NPCData = new NPCDataMap();

	public NPC getNPC(String name) {
		return new NPC(NPCData.get(name));
	}

	public NPCFactory() {

	}
}