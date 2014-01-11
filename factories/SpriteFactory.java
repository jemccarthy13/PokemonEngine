package factories;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;

public class SpriteFactory {
	public Toolkit tk;
	public String libPath = "/graphics_lib/";
	public String charPath = libPath + "Characters/Battle/";
	public String playerPath = libPath + "Characters/Player/";
	public String npcPath = libPath + "Characters/NPC/";
	public String titlePath = libPath + "Titles/";
	public String picPath = libPath + "Pictures/";
	public String fontPath = libPath + "Font/";

	public Image CONTINUESCREEN, START_SYMBOL, TITLESCREEN, ARROW, MAIN_MENU, MESSAGE_BOX, PROFOAK_LARGE;

	public Image PLAYER_DOWN, PLAYER_DOWN1, PLAYER_DOWN2;
	public Image PLAYER_UP, PLAYER_UP1, PLAYER_UP2;
	public Image PLAYER_LEFT, PLAYER_LEFT1, PLAYER_LEFT2;
	public Image PLAYER_RIGHT, PLAYER_RIGHT1, PLAYER_RIGHT2;
	public Image[] PLAYERSET = new Image[12];

	public Image POKEDEX, POKESEL, BAGSCREEN, POKEGEAR, TRAINERCARD, SAVE, OPTION, TRAINER_FOR_CARD;

	public Image PARTYFIRST, PARTYBOX, PARTYCANCEL, PARTYCANCELSEL;

	public Image POKEGEARMAP, POKEGEARRADIO, POKEGEARPHONE, POKEGEAREXIT;

	public Image BALDMAN;
	public Image BEAUTY;
	public Image BILL;
	public Image BIRDKEEPER_LEFT;
	public Image BIRDKEEPER_RIGHT;
	public Image BUGCATCHER;
	public Image CAMPER_DOWN;
	public Image CAMPER_LEFT;
	public Image CAMPER_RIGHT;
	public Image CAMPER_UP;
	public Image FALKNER;
	public Image FATMAN;
	public Image FISHER;
	public Image GUIDEGENT;
	public Image LASS;
	public Image MOM;
	public Image MR_POKEMON;
	public Image NURSE;
	public Image PICKNICKER;
	public Image PROFOAK;
	public Image SHOPKEEP;
	public Image YOUNGSTER;
	public Image BOY_DOWN, BOY_DOWN1, BOY_DOWN2;
	public Image BOY_RIGHT, BOY_RIGHT1, BOY_RIGHT2;
	public Image BOY_LEFT, BOY_LEFT1, BOY_LEFT2;
	public Image BOY_UP, BOY_UP1, BOY_UP2;
	public Image BEGINNING, POKEBALL, NAMESCREEN;
	public Image FONT_A, FONT_B, FONT_C, FONT_D, FONT_E, FONT_F, FONT_G, FONT_H, FONT_I, FONT_J, FONT_K, FONT_L,
			FONT_M, FONT_N, FONT_O, FONT_P, FONT_Q, FONT_R, FONT_S, FONT_T, FONT_U, FONT_V, FONT_W, FONT_X, FONT_Y,
			FONT_Z, FONT_EXCLAMATION, FONT_QUESTION, FONT_PERIOD, FONT_UNDERSCORE, FONT_SPACE, FONT_CURSOR;

	HashMap<Character, Image> fontMap = new HashMap<Character, Image>();

	public Image createImage(String path) {
		tk = Toolkit.getDefaultToolkit();
		Class<SpriteFactory> o = SpriteFactory.class;

		return tk.createImage(o.getResource(path));
	}

	public Image getFontChar(char c) {
		return fontMap.get(c);
	}

	public SpriteFactory() {
		POKEBALL = createImage(picPath + "Pokeball.png");

		CONTINUESCREEN = createImage(picPath + "Continue.png");
		START_SYMBOL = createImage(titlePath + "Start.png");
		TITLESCREEN = createImage(titlePath + "Title.png");

		BEGINNING = createImage(picPath + "Beginning.png");
		NAMESCREEN = createImage(picPath + "Namescreen.png");

		FONT_A = createImage(fontPath + "A.png");
		FONT_B = createImage(fontPath + "B.png");
		FONT_C = createImage(fontPath + "C.png");
		FONT_D = createImage(fontPath + "D.png");
		FONT_E = createImage(fontPath + "E.png");
		FONT_F = createImage(fontPath + "F.png");
		FONT_G = createImage(fontPath + "G.png");
		FONT_H = createImage(fontPath + "H.png");
		FONT_I = createImage(fontPath + "I.png");
		FONT_J = createImage(fontPath + "J.png");
		FONT_K = createImage(fontPath + "K.png");
		FONT_L = createImage(fontPath + "L.png");
		FONT_M = createImage(fontPath + "M.png");
		FONT_N = createImage(fontPath + "N.png");
		FONT_O = createImage(fontPath + "O.png");
		FONT_P = createImage(fontPath + "P.png");
		FONT_Q = createImage(fontPath + "Q.png");
		FONT_R = createImage(fontPath + "R.png");
		FONT_S = createImage(fontPath + "S.png");
		FONT_T = createImage(fontPath + "T.png");
		FONT_U = createImage(fontPath + "U.png");
		FONT_V = createImage(fontPath + "V.png");
		FONT_W = createImage(fontPath + "W.png");
		FONT_X = createImage(fontPath + "X.png");
		FONT_Y = createImage(fontPath + "Y.png");
		FONT_Z = createImage(fontPath + "Z.png");
		FONT_QUESTION = createImage(fontPath + "QUESTION.png");
		FONT_EXCLAMATION = createImage(fontPath + "!.png");
		FONT_UNDERSCORE = createImage(fontPath + "_.png");
		FONT_SPACE = createImage(fontPath + "SPACE.png");
		FONT_PERIOD = createImage(fontPath + "PERIOD.png");
		FONT_CURSOR = createImage(fontPath + "CURSOR.png");

		fontMap.put('A', FONT_A);
		fontMap.put('B', FONT_B);
		fontMap.put('C', FONT_C);
		fontMap.put('D', FONT_D);
		fontMap.put('E', FONT_E);
		fontMap.put('F', FONT_F);
		fontMap.put('G', FONT_G);
		fontMap.put('H', FONT_H);
		fontMap.put('I', FONT_I);
		fontMap.put('J', FONT_J);
		fontMap.put('K', FONT_K);
		fontMap.put('L', FONT_L);
		fontMap.put('M', FONT_M);
		fontMap.put('N', FONT_N);
		fontMap.put('O', FONT_O);
		fontMap.put('P', FONT_P);
		fontMap.put('Q', FONT_Q);
		fontMap.put('R', FONT_R);
		fontMap.put('S', FONT_S);
		fontMap.put('T', FONT_T);
		fontMap.put('U', FONT_U);
		fontMap.put('V', FONT_V);
		fontMap.put('W', FONT_W);
		fontMap.put('X', FONT_X);
		fontMap.put('Y', FONT_Y);
		fontMap.put('Z', FONT_Z);
		fontMap.put('?', FONT_QUESTION);
		fontMap.put('!', FONT_EXCLAMATION);
		fontMap.put(' ', FONT_SPACE);
		fontMap.put('.', FONT_PERIOD);

		PLAYER_DOWN = createImage(playerPath + "Down.png");
		PLAYER_DOWN1 = createImage(playerPath + "Down1.png");
		PLAYER_DOWN2 = createImage(playerPath + "Down2.png");

		PLAYER_UP = createImage(playerPath + "Up.png");
		PLAYER_UP1 = createImage(playerPath + "Up1.png");
		PLAYER_UP2 = createImage(playerPath + "Up2.png");

		PLAYER_LEFT = createImage(playerPath + "Left.png");
		PLAYER_LEFT1 = createImage(playerPath + "Left1.png");
		PLAYER_LEFT2 = createImage(playerPath + "Left2.png");

		PLAYER_RIGHT = createImage(playerPath + "Right.png");
		PLAYER_RIGHT1 = createImage(playerPath + "Right1.png");
		PLAYER_RIGHT2 = createImage(playerPath + "Right2.png");

		PLAYERSET[0] = PLAYER_UP;
		PLAYERSET[1] = PLAYER_UP1;
		PLAYERSET[2] = PLAYER_UP2;
		PLAYERSET[3] = PLAYER_DOWN;
		PLAYERSET[4] = PLAYER_DOWN1;
		PLAYERSET[5] = PLAYER_DOWN2;
		PLAYERSET[6] = PLAYER_RIGHT;
		PLAYERSET[7] = PLAYER_RIGHT1;
		PLAYERSET[8] = PLAYER_RIGHT2;
		PLAYERSET[9] = PLAYER_LEFT;
		PLAYERSET[10] = PLAYER_LEFT1;
		PLAYERSET[11] = PLAYER_LEFT2;

		MESSAGE_BOX = createImage(picPath + "Message_Text.png");
		ARROW = createImage(picPath + "Arrow.png");
		MAIN_MENU = createImage(picPath + "Menu.png");
		POKEDEX = createImage(picPath + "pokedexbg.png");
		POKESEL = createImage(picPath + "pokeselbg.png");
		BAGSCREEN = createImage(picPath + "BagScreen.png");
		TRAINERCARD = createImage(picPath + "TrainerCard.png");
		TRAINER_FOR_CARD = createImage(picPath + "Male.png");
		SAVE = createImage(picPath + "Save.png");
		OPTION = createImage(picPath + "OptionBG.png");
		PARTYFIRST = createImage(picPath + "Box.png");
		PARTYBOX = createImage(picPath + "SelectionBar.png");
		PARTYCANCEL = createImage(picPath + "pokeselcancel.png");
		PARTYCANCELSEL = createImage(picPath + "pokeselcancelsel.png");
		POKEGEAR = createImage(picPath + "PokegearBG.png");
		POKEGEARMAP = createImage(picPath + "PokegearMap.png");
		POKEGEARRADIO = createImage(picPath + "PokegearRadio.png");
		POKEGEARPHONE = createImage(picPath + "PokegearPhone.png");
		POKEGEAREXIT = createImage(picPath + "PokegearExit.png");

		BALDMAN = createImage(npcPath + "Baldman.png");
		BEAUTY = createImage(npcPath + "Beauty.png");
		BILL = createImage(npcPath + "Bill.png");
		BIRDKEEPER_LEFT = createImage(npcPath + "BirdKeeperL.png");
		BIRDKEEPER_RIGHT = createImage(npcPath + "BirdKeeperR.png");

		BOY_DOWN = createImage(npcPath + "Boy/Down.png");
		BOY_DOWN1 = createImage(npcPath + "Boy/Down1.png");
		BOY_DOWN2 = createImage(npcPath + "Boy/Down2.png");
		BOY_RIGHT = createImage(npcPath + "Boy/Right.png");
		BOY_LEFT = createImage(npcPath + "Boy/Left.png");
		BOY_LEFT1 = createImage(npcPath + "Boy/Left1.png");
		BOY_LEFT2 = createImage(npcPath + "Boy/Left2.png");
		BOY_UP = createImage(npcPath + "Boy/Up.png");
		BOY_UP1 = createImage(npcPath + "Boy/Up1.png");
		BOY_UP2 = createImage(npcPath + "Boy/Up2.png");

		CAMPER_LEFT = createImage(npcPath + "CamperD.png");
		CAMPER_DOWN = createImage(npcPath + "CamperL.png");
		BUGCATCHER = createImage(npcPath + "BugCatcher.png");
		FALKNER = createImage(npcPath + "Falkner.png");
		FATMAN = createImage(npcPath + "FatMan.png");
		FISHER = createImage(npcPath + "Fisher.png");
		GUIDEGENT = createImage(npcPath + "GuideGent.png");
		MOM = createImage(npcPath + "Mom.png");
		LASS = createImage(npcPath + "Lass.png");
		MR_POKEMON = createImage(npcPath + "MrPokemon.png");
		NURSE = createImage(npcPath + "Nurse.png");
		PICKNICKER = createImage(npcPath + "Picknicker.png");
		PROFOAK = createImage(npcPath + "ProfOak.png");
		PROFOAK_LARGE = createImage(charPath + "trainer9001.png");
		SHOPKEEP = createImage(npcPath + "ShopKeep.png");
		YOUNGSTER = createImage(npcPath + "Youngster.png");
	}
}