package tiles;

import graphics.SpriteLibrary;

import java.awt.Image;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

// ////////////////////////////////////////////////////////////////////////
//
// TileSet - equivalent to ArrayList<Image> (array of tile images) that 
// is loaded once through EnumsAndConstants as a library of tile images
//
// ////////////////////////////////////////////////////////////////////////
public class TileSet extends ArrayList<Image> implements Serializable {

	private static final long serialVersionUID = -3660653996002154385L;

	public static final ObstacleTile OBSTACLE = new ObstacleTile();
	public static final NormalTile NORMAL_TILE = new NormalTile();
	public static final WildTile WILD_TILE = new WildTile();

	// TODO - impassible tiles as part of tilset descriptor (JSON)
	public static final int[] IMPASSIBLE_TILES = { 0, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 18, 20, 21, 22, 23, 24, 25,
			26, 27, 28, 30, 31, 32, 33, 34, 35, 36, 37, 38, 48, 49, 50, 51, 52, 53, 54, 55, 57, 58, 59, 60, 61, 62, 63,
			64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
			96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117,
			118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138,
			139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154 };

	public static final int[] WILD_tiLES = { 10, 17, 41 };

	private static TileSet m_tiles = new TileSet();

	// ////////////////////////////////////////////////////////////////////////
	//
	// TileSet - loaded once as a library of tile images
	//
	// ////////////////////////////////////////////////////////////////////////
	private TileSet() {
		try {
			Scanner s = new Scanner(new InputStreamReader(TileSet.class.getResourceAsStream("/Tiles.tileset")));
			add(null);
			while (s.hasNext()) {
				String line = s.nextLine();
				if (line.split(",").length > 1) {
					String name = line.split(",")[1].trim().replace("Tiles", "").replace("\\", "").replace(".png", "");
					Image im = SpriteLibrary.getImage(name);
					add(im);
				}
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the current tileset
	//
	// ////////////////////////////////////////////////////////////////////////
	public static TileSet getInstance() {
		return m_tiles;
	}
}