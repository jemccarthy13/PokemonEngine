package utilities;

import graphics.MessageBox;
import graphics.Painter;

import java.awt.Font;
import java.io.Serializable;

import libraries.TeleportLibrary;
import tiles.NormalTile;
import tiles.ObstacleTile;
import tiles.TileSet;

// ////////////////////////////////////////////////////////////////////////
//
// Holds enumerations and constants for use during gameplay
//
// ////////////////////////////////////////////////////////////////////////
public class EnumsAndConstants {

	public static enum STATS {
		HP, ATTACK, DEFENSE, SP_ATTACK, SP_DEFENSE, SPEED, ACCURACY;
	}

	public static enum DIR implements Serializable {
		SOUTH, WEST, EAST, NORTH;
	}

	public static final String GRAPHICS_BATTLEPATH = "Src/graphics_lib/Battlers/";
	public static final String GRAPHICS_ICONPATH = "Src/graphics_lib/Icons/icon";
	public static final TeleportLibrary TELEPORTS = new TeleportLibrary();
	public static final MessageBox msg_box = new MessageBox();
	public static final Font POKEFONT = new Font("pokesl1", 0, 18);
	public static final ObstacleTile OBSTACLE = new ObstacleTile();
	public static final NormalTile TILE = new NormalTile();

	public static final int TILESIZE = 32;
	public static final int MAX_NAME_SIZE = 7;

	// on a scale of 0-100, slow - fast
	public static final int PLAYER_SPEED = 80;
	public static final int PLAYER_SPEED_WALK = 80;
	public static final int PLAYER_SPEED_RUN = 90;
	public static final int PLAYER_SPEED_BIKE = 95;

	public static final int NPC_SIGHT_DISTANCE = 5;

	public static final String VERSION = "Metallic Silver";

	public static final TileSet tileset = new TileSet();
	public static final int[] impassible = { 0, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 18, 20, 21, 22, 23, 24, 25, 26,
			27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 57, 58, 59, 60, 61, 62, 63, 64,
			65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 96,
			97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
			119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
			140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154 };

	public static Painter PAINTER = new Painter();
}