package utilities;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.Serializable;

import audio.JukeBox;
import audio.MidiPlayer;
import data_structures.NormalTile;
import data_structures.ObstacleTile;
import data_structures.TileSet;
import factories.AudioFactory;
import factories.ItemFactory;
import factories.LocationFactory;
import factories.MoveFactory;
import factories.NPCFactory;
import factories.PokemonFactory;
import factories.SpriteFactory;
import factories.TeleportFactory;
import graphics.MessageBox;
import graphics.Painter;

public class EnumsAndConstants {
	public static enum GAMEOVER implements Serializable {
		NOT_OVER, WIN, LOSS, ERROR;
	}

	public static enum STATS {
		HP, ATTACK, DEFENSE, SP_ATTACK, SP_DEFENSE, SPEED, ACCURACY;
	}

	public static enum DIR implements Serializable {
		NORTH, SOUTH, EAST, WEST;
	}

	public static enum MUSIC {
		NEWBARKTOWN, CHERRYGROVE, TITLE, CONTINUE, TRAINER_BATTLE, INTRO, TRAINER1, TRAINER2, TRAINER3, TRAINER4, TRAINER5, TRAINER6;
	}

	public static enum POKETYPE {
		NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON, DARK, STEEL;
	}

	public enum SPRITENAMES {
		BOY, LASS, FATMAN, YOUNGSTER, BIRDKEEPER, BILL, PLAYER, NURSE, BALDMAN, CAMPER, BEAUTY, SHOPKEEP, FALKNER, GUIDE, PROF_OAK_LARGE;
	}

	public static final String[] STATNAMES = { "HP", "ATTACK", "DEFENSE", "SP_ATTACK", "SP_DEFENSE", "SPEED" };

	public static enum MOVETYPE {
		PHYSICAL, SPECIAL, STAT;
	}

	public static enum LOCATIONS {
		MOMHOUSE, ELMLAB, NEWBARKTOWN, ROUTE29, CHERRYGROVE, ROUTE30, CHERRYGROVEMART, CHERRYGROVEPOKECENTER;
	}

	public static final AudioFactory audio_lib = new AudioFactory();
	public static final ItemFactory item_lib = new ItemFactory();
	public static final LocationFactory loc_lib = new LocationFactory();

	public static final Toolkit tk = Toolkit.getDefaultToolkit();
	public static final MoveFactory move_lib = new MoveFactory();
	public static final PokemonFactory pokemon_generator = new PokemonFactory();
	public static final SpriteFactory sprite_lib = new SpriteFactory();
	public static final NPCFactory npc_lib = new NPCFactory();

	public static final Font POKEFONT = new Font("pokesl1", 0, 18);
	public static final String filePath = "PokemonOrange.SAV";
	public static final ObstacleTile OBSTACLE = new ObstacleTile();
	public static final NormalTile TILE = new NormalTile();
	public static final JukeBox col = new JukeBox();
	public static final int MAX_DISTANCE = 5;
	public static final int INCR = 0;
	public static final int DECR = 1;
	public static final int X_LOC = 0;
	public static final int Y_LOC = 1;
	public static final int JFRAME_XDIM = 480;
	public static final int JFRAME_YDIM = 320;
	public static final int MAX_LEVEL = 100;
	public static final int MAX_NUM_MOVES = 4;
	public static final int NUM_STATS = 7;
	public static final int MAX_STAGES = 3;
	public static final int MAX_NAME_SIZE = 7;
	public static final int STAT_OFFSET = 5;
	public static final int HP_ALL = -1;
	public static final int STAT_INCR_START = 2;
	public static final int STAT_INCR_END = 3;
	public static final int NUM_TILE_IMAGES = 38;
	public static final int TILESIZE = 32;
	public static final int SCREENCENTERX = 224;
	public static final int SCREENCENTERY = 118;
	public static final int SECONDS_PER_HOUR = 3600;
	public static final int SECONDS_PER_MIN = 60;
	public static final int MSEC_CORRECTION = 1000;
	public static final int MAX_TILES = 91;
	public static final int MOMSHOUSEX = 15;
	public static final int MOMSHOUSEY = 25;
	public static final int ELMSLABX = 30;
	public static final int ELMSLABY = 30;

	public static TeleportFactory TELEPORTS = new TeleportFactory();

	public static Painter PAINTER = new Painter();

	public static final String SOUNDEFFECTPATH = "/audio_lib/SE/";
	public static final String COLLISION_SOUND = "COLLISION";
	public static final String SELECT_SOUND = "SELECT";
	public static final String MENU_SOUND = "MENU";
	public static final String DAMAGE_SOUND = "DAMAGE";
	public static MidiPlayer BACKGROUND_MUSIC = null;
	public static MessageBox msg_box = new MessageBox();
	public static final TileSet tileset = new TileSet();
	public static final String[] STAT_NAMES = { "HP", "Attack", "Defense", "Sp. Att.", "Sp. Def", "Speed" };
	public static final int[] impassible = { 0, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 20, 21, 22, 23, 24, 25,
			26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 57, 58, 59, 60, 61, 62, 63,
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };

	public static void initializeJukeBox() {
		col.loadClip("/audio_lib/SE/Select.wav", "SELECT", 1);
		col.loadClip("/audio_lib/SE/Collision.wav", "COLLISION", 1);
		col.loadClip("/audio_lib/SE/Menu.wav", "MENU", 1);
		col.loadClip("/audio_lib/SE/Damage.wav", "DAMAGE", 1);
	}

	public static void initializeMidiPlayer() {
		BACKGROUND_MUSIC = audio_lib.TITLE;
		BACKGROUND_MUSIC.start();
	}
}