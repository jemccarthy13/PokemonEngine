package libraries;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import trainers.NPC;
import trainers.TrainerData;
import utilities.EnumsAndConstants.DIR;

//////////////////////////////////////////////////////////////////////////
//
//Look through the NPC data files, creating each NPC and adding them to 
// the list of active NPCs
//
//////////////////////////////////////////////////////////////////////////
public class NPCLibrary {

	public NPCDataMap npcData = new NPCDataMap();
	public ArrayList<NPC> npcs = new ArrayList<NPC>();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Maps NPC Name->Data
	//
	// ////////////////////////////////////////////////////////////////////////
	class NPCDataMap extends HashMap<String, TrainerData> {
		private static final long serialVersionUID = 1L;

		public NPCDataMap() {
			String path = "Src/Data/NPCs";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					TrainerData pd = new TrainerData(listOfFiles[i].getPath());
					if (pd.isValidData())
						put(pd.name, pd);
				}
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getNPC - retrieve a NPC by name from the map
	//
	// ////////////////////////////////////////////////////////////////////////
	public NPC getNPC(String name) {
		for (NPC curNPC : npcs) {
			if (curNPC.getName().equals(name)) {
				return curNPC;
			}
		}
		return null;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor initializes the NPC list
	//
	// ////////////////////////////////////////////////////////////////////////
	public NPCLibrary() {
		for (String key : npcData.keySet()) {
			NPC newNPC = new NPC(npcData.get(key));
			newNPC.setDirection(DIR.SOUTH);
			npcs.add(newNPC);
		}
	}
}
