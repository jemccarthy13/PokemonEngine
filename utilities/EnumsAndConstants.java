package utilities;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.Serializable;

import tiles.NormalTile;
import tiles.ObstacleTile;
import tiles.TileSet;
import factories.AudioFactory;
import factories.LocationFactory;
import factories.MoveFactory;
import factories.NPCFactory;
import factories.PokemonFactory;
import factories.SpriteFactory;
import factories.TeleportFactory;
import graphics.MessageBox;
import graphics.Painter;

public class EnumsAndConstants {

	public static enum STATS {
		HP, ATTACK, DEFENSE, SP_ATTACK, SP_DEFENSE, SPEED, ACCURACY;
	}

	public static enum DIR implements Serializable {
		SOUTH, WEST, EAST, NORTH;
	}

	public static enum MUSIC {
		NEWBARKTOWN, CHERRYGROVE, TITLE, CONTINUE, TRAINER_BATTLE, INTRO, TRAINER1, TRAINER2, TRAINER3, TRAINER4, TRAINER5, TRAINER6;
	}

	// TODO determine if this enum is necessary with new NPC data files
	// (once implemented)
	public enum SPRITENAMES {
		BOY, LASS, FATMAN, YOUNGSTER, BIRDKEEPER, BILL, PLAYER, NURSE, BALDMAN, CAMPER, BEAUTY, SHOPKEEP, FALKNER, GUIDE, PROF_OAK_LARGE;
	}

	public static final Toolkit tk = Toolkit.getDefaultToolkit();

	// TODO change "Factory" to "Map" where most appropriate
	public static final AudioFactory audio_lib = new AudioFactory();
	public static final LocationFactory loc_lib = new LocationFactory();
	public static final MoveFactory move_lib = new MoveFactory();
	public static final PokemonFactory pokemon_generator = new PokemonFactory();
	public static final SpriteFactory sprite_lib = new SpriteFactory();
	public static final NPCFactory npc_lib = new NPCFactory();
	public static final TeleportFactory TELEPORTS = new TeleportFactory();
	public static final MessageBox msg_box = new MessageBox();
	public static final Font POKEFONT = new Font("pokesl1", 0, 18);
	public static final ObstacleTile OBSTACLE = new ObstacleTile();
	public static final NormalTile TILE = new NormalTile();
	public static final JukeBox col = new JukeBox();

	public static final int TILESIZE = 32;
	public static final int MAX_NAME_SIZE = 7;

	// on a scale of 0-100, slow - fast
	public static final int PLAYERSPEED = 80;
	public static final int PLAYERSPEED_WALK = 80;
	public static final int PLAYERSPEED_RUN = 90;
	public static final int PLAYERSPEED_BIKE = 95;

	public static final String VERSION = "Metallic Silver";

	public static final TileSet tileset = new TileSet();
	public static final int[] impassible = { 0, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16, 18, 20, 21, 22, 23, 24, 25,
			26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 57, 58, 59, 60, 61, 62, 63,
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
			96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117,
			118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138,
			139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154 };

	public static Painter PAINTER = new Painter();

	public static final String SOUNDEFFECTPATH = "/audio_lib/SE/";
	public static final String COLLISION_SOUND = "COLLISION";
	public static final String SELECT_SOUND = "SELECT";
	public static final String MENU_SOUND = "MENU";
	public static final String DAMAGE_SOUND = "DAMAGE";
	public static MidiPlayer BACKGROUND_MUSIC = null;

	public static void initializeJukeBox() {
		col.loadClip(SOUNDEFFECTPATH + "Select.wav", SELECT_SOUND, 1);
		col.loadClip(SOUNDEFFECTPATH + "Collision.wav", COLLISION_SOUND, 1);
		col.loadClip(SOUNDEFFECTPATH + "Menu.wav", MENU_SOUND, 1);
		col.loadClip(SOUNDEFFECTPATH + "Damage.wav", DAMAGE_SOUND, 1);
	}

	public static void initializeMidiPlayer() {
		BACKGROUND_MUSIC = audio_lib.TITLE;
		BACKGROUND_MUSIC.start();
	}
}