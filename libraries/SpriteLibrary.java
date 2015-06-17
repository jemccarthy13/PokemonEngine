package libraries;

import graphics.BattleScene;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

// ////////////////////////////////////////////////////////////////////////
//
// Create all images used in game for reference.  Defaults + going through
// the characters directory to map TrainerType->Image[]
//
// ////////////////////////////////////////////////////////////////////////
public class SpriteLibrary {
	public Toolkit tk;
	public String libPath = "graphics_lib/";
	public String charPath = libPath + "Characters/Battle/";
	public String spritePath = libPath + "Characters/Sprites/";
	public String titlePath = libPath + "Titles/";
	public String picPath = libPath + "Pictures/";
	public String fontPath = libPath + "Font/";

	public Image CONTINUESCREEN, START_SYMBOL, TITLESCREEN, ARROW, MAIN_MENU, MESSAGE_BOX;

	public Image POKEDEX, POKESEL, BAGSCREEN, POKEGEAR, TRAINERCARD, SAVE, OPTION_SOUND_ON, OPTION_SOUND_OFF,
			TRAINER_FOR_CARD;

	public Image PARTYFIRST, PARTYBOX, PARTYCANCEL, PARTYCANCELSEL;

	public Image POKEGEAR_MAP, POKEGEAR_RADIO, POKEGEAR_PHONE, POKEGEAR_EXIT;

	public Image BATTLE_BG, BATTLE_FIGHTBG, BG;
	public Image STATUS_PSN, STATUS_FRZ, STATUS_BRN, STATUS_SLP, STATUS_PAR;

	public Image BEGINNING, ICON, NAMESCREEN;
	public Image FONT_UNDERSCORE, FONT_CURSOR;

	HashMap<Character, Image> fontMap = new HashMap<Character, Image>();
	HashMap<String, ArrayList<Image>> spriteMap = new HashMap<String, ArrayList<Image>>();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Loads an image from a given path
	//
	// ////////////////////////////////////////////////////////////////////////
	public Image createImage(String path) {
		tk = Toolkit.getDefaultToolkit();

		return tk.createImage(SpriteLibrary.class.getResource("/" + path));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Returns the Font character Image corresponding to the given char
	//
	// ////////////////////////////////////////////////////////////////////////
	public Image getFontChar(char c) {
		return fontMap.get(c);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the set of 12 directional spries that correspond to the given name
	//
	// ////////////////////////////////////////////////////////////////////////
	public ArrayList<Image> getSprites(String name) {
		return spriteMap.get(name);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor intializes all sprites from the graphics_lib folder
	//
	// ////////////////////////////////////////////////////////////////////////
	public SpriteLibrary() {

		File file = new File(spritePath);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});

		for (String x : directories) {
			File f = new File(spritePath + x);
			String[] files = f.list();
			ArrayList<Image> sprites = new ArrayList<Image>();
			for (String y : files) {
				sprites.add(createImage(spritePath + x + "/" + y));
			}
			spriteMap.put(x, sprites);
		}

		ICON = createImage(titlePath + "Icon.png");
		TITLESCREEN = createImage(titlePath + "Title.png");
		START_SYMBOL = createImage(titlePath + "Start.png");
		CONTINUESCREEN = createImage(picPath + "Continue.png");

		BEGINNING = createImage(picPath + "Beginning.png");
		NAMESCREEN = createImage(picPath + "Namescreen.png");

		FONT_UNDERSCORE = createImage(fontPath + "_.png");
		FONT_CURSOR = createImage(fontPath + "CURSOR.png");
		fontMap.put('A', createImage(fontPath + "A.png"));
		fontMap.put('B', createImage(fontPath + "B.png"));
		fontMap.put('C', createImage(fontPath + "C.png"));
		fontMap.put('D', createImage(fontPath + "D.png"));
		fontMap.put('E', createImage(fontPath + "E.png"));
		fontMap.put('F', createImage(fontPath + "F.png"));
		fontMap.put('G', createImage(fontPath + "G.png"));
		fontMap.put('H', createImage(fontPath + "H.png"));
		fontMap.put('I', createImage(fontPath + "I.png"));
		fontMap.put('J', createImage(fontPath + "J.png"));
		fontMap.put('K', createImage(fontPath + "K.png"));
		fontMap.put('L', createImage(fontPath + "L.png"));
		fontMap.put('M', createImage(fontPath + "M.png"));
		fontMap.put('N', createImage(fontPath + "N.png"));
		fontMap.put('O', createImage(fontPath + "O.png"));
		fontMap.put('P', createImage(fontPath + "P.png"));
		fontMap.put('Q', createImage(fontPath + "Q.png"));
		fontMap.put('R', createImage(fontPath + "R.png"));
		fontMap.put('S', createImage(fontPath + "S.png"));
		fontMap.put('T', createImage(fontPath + "T.png"));
		fontMap.put('U', createImage(fontPath + "U.png"));
		fontMap.put('V', createImage(fontPath + "V.png"));
		fontMap.put('W', createImage(fontPath + "W.png"));
		fontMap.put('X', createImage(fontPath + "X.png"));
		fontMap.put('Y', createImage(fontPath + "Y.png"));
		fontMap.put('Z', createImage(fontPath + "Z.png"));
		fontMap.put('?', createImage(fontPath + "QUESTION.png"));
		fontMap.put('!', createImage(fontPath + "!.png"));
		fontMap.put(' ', createImage(fontPath + "SPACE.png"));
		fontMap.put('.', createImage(fontPath + "PERIOD.png"));

		MESSAGE_BOX = createImage(picPath + "Message_Text.png");
		ARROW = createImage(picPath + "Arrow.png");
		MAIN_MENU = createImage(picPath + "Menu.png");

		// MENU graphics
		POKEDEX = createImage(picPath + "pokedexbg.png");
		POKESEL = createImage(picPath + "pokeselbg.png");
		BAGSCREEN = createImage(picPath + "BagScreen.png");
		TRAINERCARD = createImage(picPath + "TrainerCard.png");
		TRAINER_FOR_CARD = createImage(picPath + "Male.png");
		SAVE = createImage(picPath + "Save.png");
		OPTION_SOUND_ON = createImage(picPath + "OptionBG_SoundOn.png");
		OPTION_SOUND_OFF = createImage(picPath + "OptionBG_SoundOff.png");

		// PARTY graphics
		PARTYFIRST = createImage(picPath + "Box.png");
		PARTYBOX = createImage(picPath + "SelectionBar.png");
		PARTYCANCEL = createImage(picPath + "pokeselcancel.png");
		PARTYCANCELSEL = createImage(picPath + "pokeselcancelsel.png");

		// POKEGEAR graphics
		POKEGEAR = createImage(picPath + "PokegearBG.png");
		POKEGEAR_MAP = createImage(picPath + "PokegearMap.png");
		POKEGEAR_RADIO = createImage(picPath + "PokegearRadio.png");
		POKEGEAR_PHONE = createImage(picPath + "PokegearPhone.png");
		POKEGEAR_EXIT = createImage(picPath + "PokegearExit.png");

		// BATTLE graphics
		BG = Toolkit.getDefaultToolkit().getImage(BattleScene.class.getResource("/graphics_lib/Pictures/BG.png"));
		BATTLE_BG = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/Battle.png"));
		BATTLE_FIGHTBG = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/Battle2.png"));
		STATUS_PAR = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/StatusPAR.png"));
		STATUS_BRN = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/StatusBRN.png"));
		STATUS_PSN = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/StatusPSN.png"));
		STATUS_SLP = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/StatusSLP.png"));
		STATUS_FRZ = Toolkit.getDefaultToolkit().getImage(
				BattleScene.class.getResource("/graphics_lib/Pictures/StatusFRZ.png"));
	}
}