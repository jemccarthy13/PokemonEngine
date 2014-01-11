package data_structures;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;

import utilities.EnumsAndConstants;

public class TileSet extends ArrayList<Image> implements Serializable {
	private static final long serialVersionUID = 1L;

	public TileSet() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(TileSet.class.getResourceAsStream("/mapmaker/Tiles.tileset")));
			for (int i = 0; i < EnumsAndConstants.MAX_TILES; i++) {
				String line = br.readLine();
				if (line.split(",").length > 1) {
					String name = line.split(",")[1].trim();
					add(EnumsAndConstants.tk.createImage(TileSet.class.getResource("/mapmaker/" + name)));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}