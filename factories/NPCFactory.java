package factories;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import trainers.NPC;
import trainers.TrainerData;
import utilities.EnumsAndConstants.DIR;

public class NPCFactory {

	public NPCDataMap npcData = new NPCDataMap();

	public ArrayList<NPC> npcs = new ArrayList<NPC>();

	// public NPC PROFESSOROAK;

	class NPCDataMap extends HashMap<String, TrainerData> {
		private static final long serialVersionUID = 1L;
		String spritePath = "graphics_lib/Characters/NPC";

		public NPCDataMap() {
			String path = "Data/NPCs";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					TrainerData pd = new TrainerData(listOfFiles[i].getPath());
					put(pd.name, pd);
				}
			}
		}
	}

	public NPC getNPC(String name) {
		return new NPC(npcData.get(name));
	}

	public NPCFactory() {
		for (String key : npcData.keySet()) {
			NPC newNPC = new NPC(npcData.get(key));
			newNPC.setDirection(DIR.SOUTH);
			npcs.add(newNPC);
		}
	}
}
