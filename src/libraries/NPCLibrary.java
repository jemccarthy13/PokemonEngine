package libraries;

import java.io.File;
import java.util.HashMap;

import trainers.NPC;
import trainers.TrainerData;
import utilities.EnumsAndConstants.DIR;

//////////////////////////////////////////////////////////////////////////
//
// Look through the NPC data files, creating each NPC and adding them to 
// the list of active NPCs
//
//////////////////////////////////////////////////////////////////////////
public class NPCLibrary extends HashMap<String, NPC> {

	private static final long serialVersionUID = 1L;
	private static NPCLibrary m_instance = new NPCLibrary();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Private constructor ensures only one instance
	//
	// ////////////////////////////////////////////////////////////////////////
	private NPCLibrary() {
		String path = "resources/data/NPCs";
		try {
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					TrainerData pd = new TrainerData(listOfFiles[i].getPath());

					// if the trainer data is valid, create a new NPC and map
					// name -> NPC
					if (pd.isValidData()) {
						NPC newNPC = new NPC(pd);
						newNPC.setDirection(DIR.SOUTH);
						put(pd.name, newNPC);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the single instance of this library
	//
	// ////////////////////////////////////////////////////////////////////////
	public static NPCLibrary getInstance() {
		return m_instance;
	}
}