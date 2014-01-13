package factories;

import graphics.NPC;
import utilities.EnumsAndConstants;

public class NPCFactory {
	public NPC PROFESSOROAK;
	public NPC NBT_CITIZEN1, NBT_CITIZEN2, NBT_CITIZEN3;
	public NPC CHERRYGROVE_CITIZEN1, CHERRYGROVE_CITIZEN2, CHERRYGROVE_CITIZEN3;
	public NPC CHERRYGROVE_GUIDE;
	public NPC VIOLET_CITIZEN1, VIOLET_CITIZEN2, VIOLET_CITIZEN3, VIOLET_CITIZEN4, VIOLET_CITIZEN5;
	public NPC VIOLET_INDOORS1, VIOLET_INDOORS2;
	public NPC VIOLET_SCHOOL_STUDENT1, VIOLET_SCHOOL_STUDENT2, VIOLET_SCHOOL_STUDENT3, VIOLET_SCHOOL_STUDENT4,
			VIOLET_SCHOOL_STUDENT5, VIOLET_SCHOOL_STUDENT6;
	public NPC VIOLET_MART_CUSTOMER, VIOLET_SHOP_KEEP, VIOLET_CENTER_VISITOR;
	public NPC VIOLETGYM_TRAINER1, VIOLETGYM_TRAINER2, FALKNER;
	public NPC NURSE_JOY;
	public NPC[] JOHTO_NPC = new NPC[50];
	public NPC[] NEWBARKTOWN_NPC = new NPC[1];

	public NPCFactory() {
		String[] text3 = { "Hello there!", "Welcome to the world of Pokemon!", "My name is Professor Oak.",
				"People call me the Pokemon Prof.", "This world is inhabited by creatures", "that we call Pokemon.",
				"People and Pokemon live together", "by supporting each other.", "Some people play with Pokemon,",
				"some battle with them.", "But we don't know everything about Pokemon yet.",
				"There are still many mysteries to solve.", "That's why I study Pokemon every day.",
				"Now, what did you say your name was?", "That's right.  Well are you ready?",
				"Your own Pokemon story is about to unfold.", "You'll face fun times and tough challenges.",
				"A world of dreams and adventures awaits.", "Let's go!  I'll be seeing you later!", "" };
		PROFESSOROAK = new NPC(0, 0, "Professor Oak", text3, EnumsAndConstants.SPRITENAMES.PROF_OAK_LARGE, null, false);

		String[] text = { "" };

		String[] text2 = { "We hope to see you again!" };
		NURSE_JOY = new NPC(42, 105, "Citizen", text2, EnumsAndConstants.SPRITENAMES.NURSE, null, false);

		text[0] = "We care about the traditional buildings around here.";
		VIOLET_CITIZEN1 = new NPC(42, 30, "Citizen", text, EnumsAndConstants.SPRITENAMES.BALDMAN, null, false);

		text[0] = "It is rumored that there are ghost pokemon in the Sprout Tower.";
		VIOLET_CITIZEN2 = new NPC(34, 34, "Citizen", text, EnumsAndConstants.SPRITENAMES.BILL, null, false);

		text[0] = "Hey, your a pokemon trainer!  IF you beat the gym leader, you'll be ready for the big time.";
		VIOLET_CITIZEN3 = new NPC(30, 23, "Citizen", text, EnumsAndConstants.SPRITENAMES.CAMPER, null, false);

		text[0] = "Falkner, from Violet City pokemon gym, is a fine trainer.";
		VIOLET_CITIZEN4 = new NPC(24, 26, "Citizen", text, EnumsAndConstants.SPRITENAMES.BOY, null, false);

		text[0] = "You can't have your pokemon out with you in all places.";
		VIOLET_CITIZEN5 = new NPC(11, 23, "Citizen", text, EnumsAndConstants.SPRITENAMES.FATMAN, null, false);

		text[0] = "There are many wild Pokemon in the tall grass.";
		VIOLET_INDOORS1 = new NPC(8, 10, "Citizen", text, EnumsAndConstants.SPRITENAMES.BALDMAN, null, false);

		text[0] = "Do you want to trade Pokemon?";
		VIOLET_INDOORS2 = new NPC(3, 107, "Citizen", text, EnumsAndConstants.SPRITENAMES.BOY, null, false);

		text[0] = "I want to learn how to become a Pokemon Master.";
		VIOLET_SCHOOL_STUDENT1 = new NPC(23, 90, "Citizen", text, EnumsAndConstants.SPRITENAMES.YOUNGSTER, null, false);

		text[0] = "What type of Pokemon is Pikachu?";
		VIOLET_SCHOOL_STUDENT2 = new NPC(25, 90, "Citizen", text, EnumsAndConstants.SPRITENAMES.YOUNGSTER, null, false);

		text[0] = "Are you a Pokemon trainer?";
		VIOLET_SCHOOL_STUDENT3 = new NPC(26, 90, "Citizen", text, EnumsAndConstants.SPRITENAMES.BEAUTY, null, false);

		text[0] = "The Sprout Tower is a shrine to Bellsprout.";
		VIOLET_SCHOOL_STUDENT4 = new NPC(23, 88, "Citizen", text, EnumsAndConstants.SPRITENAMES.YOUNGSTER, null, false);

		text[0] = "Whadaya want from me!";
		VIOLET_SCHOOL_STUDENT5 = new NPC(25, 86, "Citizen", text, EnumsAndConstants.SPRITENAMES.CAMPER, null, false);

		text[0] = "You're never too old to learn about Pokemon.";
		VIOLET_SCHOOL_STUDENT6 = new NPC(24, 84, "Citizen", text, EnumsAndConstants.SPRITENAMES.BALDMAN, null, false);

		text[0] = "I wonder if they carry Pokeballs.";
		VIOLET_MART_CUSTOMER = new NPC(24, 106, "Citizen", text, EnumsAndConstants.SPRITENAMES.YOUNGSTER, null, false);

		text[0] = "Can I help you with something?";
		VIOLET_SHOP_KEEP = new NPC(20, 107, "Citizen", text, EnumsAndConstants.SPRITENAMES.SHOPKEEP, null, false);

		text[0] = "You can heal your Pokemon by talking to Nurse Joy.";
		VIOLET_CENTER_VISITOR = new NPC(40, 107, "Citizen", text, EnumsAndConstants.SPRITENAMES.FATMAN, null, false);

		text[0] = "I want to be like Falkner.";
		VIOLETGYM_TRAINER1 = new NPC(5, 89, "Citizen", text, EnumsAndConstants.SPRITENAMES.BIRDKEEPER, null, true);

		text[0] = "Falkner is the best.";
		VIOLETGYM_TRAINER2 = new NPC(10, 85, "Citizen", text, EnumsAndConstants.SPRITENAMES.BIRDKEEPER, null, true);

		text[0] = "My precious bird Pokemon are unstopable.";
		FALKNER = new NPC(7, 81, "Citizen", text, EnumsAndConstants.SPRITENAMES.FALKNER, null, true);

		text[0] = "I can guide you around cherrygrove!";
		CHERRYGROVE_GUIDE = new NPC(88, 102, "Citizen", text, EnumsAndConstants.SPRITENAMES.GUIDE, null, false);

		text[0] = "I'm hungry.";
		CHERRYGROVE_CITIZEN1 = new NPC(84, 105, "Citizen", text, EnumsAndConstants.SPRITENAMES.FATMAN, null, false);

		text[0] = "Cherrygrove City is beautiful.";
		CHERRYGROVE_CITIZEN2 = new NPC(83, 108, "Citizen", text, EnumsAndConstants.SPRITENAMES.LASS, null, false);

		text[0] = "Nothing interesting happens here.";
		CHERRYGROVE_CITIZEN3 = new NPC(78, 104, "Citizen", text, EnumsAndConstants.SPRITENAMES.BOY, null, false);

		String[] text4 = { "I'm hungry." };
		NBT_CITIZEN1 = new NPC(26, 8, "NBT_Citizen1", text4, EnumsAndConstants.SPRITENAMES.BOY, null, true);
		NBT_CITIZEN1.caughtPokemon(EnumsAndConstants.pokemon_generator.createPokemon("Rattatta", 3));

		text[0] = "Professor Elm knows a lot about pokemon.";
		NBT_CITIZEN2 = new NPC(10, 10, "Citizen", text, EnumsAndConstants.SPRITENAMES.BEAUTY, null, false);

		text[0] = "Nothing interesting happens here.";
		NBT_CITIZEN3 = new NPC(11, 11, "Citizen", text, EnumsAndConstants.SPRITENAMES.FATMAN, null, false);

		JOHTO_NPC[0] = VIOLET_CITIZEN1;
		JOHTO_NPC[1] = VIOLET_CITIZEN2;
		JOHTO_NPC[2] = VIOLET_CITIZEN3;
		JOHTO_NPC[3] = VIOLET_CITIZEN4;
		JOHTO_NPC[4] = VIOLET_CITIZEN5;
		JOHTO_NPC[5] = VIOLET_INDOORS1;
		JOHTO_NPC[6] = VIOLET_INDOORS2;
		JOHTO_NPC[7] = VIOLET_SCHOOL_STUDENT1;
		JOHTO_NPC[8] = VIOLET_SCHOOL_STUDENT2;
		JOHTO_NPC[9] = VIOLET_SCHOOL_STUDENT3;
		JOHTO_NPC[10] = VIOLET_SCHOOL_STUDENT4;
		JOHTO_NPC[11] = VIOLET_SCHOOL_STUDENT5;
		JOHTO_NPC[12] = VIOLET_SCHOOL_STUDENT6;
		JOHTO_NPC[13] = VIOLET_MART_CUSTOMER;
		JOHTO_NPC[14] = VIOLET_SHOP_KEEP;
		JOHTO_NPC[15] = VIOLET_CENTER_VISITOR;
		JOHTO_NPC[16] = NURSE_JOY;
		JOHTO_NPC[17] = VIOLETGYM_TRAINER1;
		JOHTO_NPC[18] = VIOLETGYM_TRAINER2;
		JOHTO_NPC[19] = FALKNER;
		JOHTO_NPC[20] = CHERRYGROVE_GUIDE;
		JOHTO_NPC[21] = CHERRYGROVE_CITIZEN1;
		JOHTO_NPC[22] = CHERRYGROVE_CITIZEN2;
		JOHTO_NPC[23] = CHERRYGROVE_CITIZEN3;
		JOHTO_NPC[24] = NBT_CITIZEN1;
		JOHTO_NPC[25] = NBT_CITIZEN2;
		JOHTO_NPC[26] = NBT_CITIZEN3;

		NEWBARKTOWN_NPC[0] = NBT_CITIZEN1;
	}
}