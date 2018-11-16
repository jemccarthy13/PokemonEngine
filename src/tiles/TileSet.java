package tiles;

import java.awt.Image;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import graphics.SpriteLibrary;

/**
 * A typedef to ArrayList<Image> (array of tile images) that is loaded once to
 * be a library of tile images
 * 
 * @author John
 * 
 */
public class TileSet extends ArrayList<Image> {

	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = -3660653996002154385L;

	/**
	 * Single instance of an obstacle tile, used to block player movement
	 */
	public static final ObstacleTile OBSTACLE = new ObstacleTile();
	/**
	 * Single instance of a normal tile, used to fill in the map
	 */
	public static final NormalTile NORMAL = new NormalTile();
	/**
	 * Single instance of a battle tile, used to cause wild encounters
	 */
	public static final BattleTile BATTLE = new BattleTile();

	/**
	 * An array of all of the tile numbers that should be OBSTACLEs
	 * 
	 * @TODO - make part of tilset descriptor (JSON)
	 */
	public static final int[] IMPASSIBLE_TILES = { 0, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 18, 20, 21, 22, 23, 24, 25,
			26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 57, 58, 59, 60, 61, 62, 63,
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
			96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117,
			118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138,
			139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154 };

	/**
	 * An array of all of the tile numbers that should be BATTLE_TILEs
	 * 
	 * @TODO - make part of tilset descriptor (JSON)
	 */
	public static final int[] BATTLE_TILES = { 10, 17, 41 };

	/**
	 * Single instance of the TileSet
	 */
	private static TileSet m_tiles = new TileSet();

	/**
	 * Compare two see if two tiles are of the same class
	 * 
	 * @TODO make sure this is used for collision / battle checks
	 * @param one
	 *            - the first tile
	 * @param two
	 *            - the second tile
	 * @return whether or not the two tiles are of the same class (similar to a
	 *         instanceof b)
	 */
	public static boolean compareTiles(Tile one, Tile two) {
		return one.getClass() == two.getClass();
	}

	/**
	 * Load the tileset as a library of tile images
	 */
	private TileSet() {
		try (Scanner s = new Scanner(new InputStreamReader(TileSet.class.getResourceAsStream("/Tiles.tileset")))) {
			add(null);
			while (s.hasNext()) {
				String line = s.nextLine();
				if (line.split(",").length > 1) {
					String name = line.split(",")[1].trim().replace("Tiles", "").replace("\\", "").replace(".png", "");
					Image im = SpriteLibrary.getImage(name);
					add(im);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the current TileSet instance
	 * 
	 * @return - the single instance of a tile set
	 */
	public static TileSet getInstance() {
		return m_tiles;
	}
}