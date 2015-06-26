package tiles;

import java.awt.Image;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import utilities.EnumsAndConstants;

// ////////////////////////////////////////////////////////////////////////
//
// TileSet - equivalent to ArrayList<Image> (array of tile images) that 
// is loaded once through EnumsAndConstants as a library of tile images
//
// ////////////////////////////////////////////////////////////////////////
public class TileSet extends ArrayList<Image> implements Serializable {
	private static final long serialVersionUID = 1L;

	public TileSet() {
		try {
			Scanner s = new Scanner(new InputStreamReader(TileSet.class.getResourceAsStream("/mapmaker/Tiles.tileset")));
			add(null);
			while (s.hasNext()) {
				String line = s.nextLine();
				if (line.split(",").length > 1) {
					String name = line.split(",")[1].trim();
					add(EnumsAndConstants.tk.createImage(TileSet.class.getResource("/mapmaker/" + name)));
				}
			}
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}