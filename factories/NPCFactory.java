package factories;

import graphics.NPC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import utilities.EnumsAndConstants.DIR;
import data_structures.Coordinate;
import data_structures.NPCData;

public class NPCFactory {

	public NPCDataMap npcData = new NPCDataMap();

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
		String[] text3 = { "Hello there!", "Welcome to the world of Pokemon!", "My name is Professor Oak.",
				"People call me the Pokemon Prof.", "This world is inhabited by creatures", "that we call Pokemon.",
				"People and Pokemon live together", "by supporting each other.", "Some people play with Pokemon,",
				"some battle with them.", "But we don't know everything about Pokemon yet.",
				"There are still many mysteries to solve.", "That's why I study Pokemon every day.",
				"Now, what did you say your name was?", "That's right.  Well are you ready?",
				"Your own Pokemon story is about to unfold.", "You'll face fun times and tough challenges.",
				"A world of dreams and adventures awaits.", "Let's go!  I'll be seeing you later!", "" };
		NPCData nDat = new NPCData();
		nDat.name = "Professor Oak";
		nDat.location = new Coordinate(0, 0);

		for (String x : text3) {
			nDat.conversationText.add(x);
		}
		nDat.trainer = false;
		nDat.stationary = true;
		nDat.moneyWon = 0;
		// PROFESSOROAK = new NPC(nDat);
		// PROFESSOROAK.sprite = EnumsAndConstants.sprite_lib.PROFOAK_LARGE;

	}
}