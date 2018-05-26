package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import controller.GameController;
import model.Configuration;
import model.Coordinate;
import model.GameData;
import tiles.TileSet;
import utilities.DebugUtility;

/**
 * A singleton class that loads the map layers into the game
 */
public class MapLoader {

	private static MapLoader instance = new MapLoader();

	public static MapLoader getInstance() {
		return instance;
	}

	/**
	 * In a threaded environment, the file needs to be protected so the file reading
	 * can be synchronized between threads.
	 */
	private class SynchronizedReader {
		private BufferedReader reader;

		SynchronizedReader(BufferedReader r) {
			reader = r;
		}

		public synchronized String readLine() throws IOException {
			return reader.readLine();
		}
	}

	/**
	 * To speed up the map loading, each layer is processed in a thread.
	 * 
	 */
	private class ProcessLayerThread extends Thread {

		SynchronizedReader reader;
		GameController controller;
		GameData data;
		int layer;

		ProcessLayerThread(SynchronizedReader r, GameController control, int lyr) {
			reader = r;
			controller = control;
			data = controller.gData;
			layer = lyr;
		}

		@Override
		public void run() {
			String line = null;
			try {
				line = reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int curRow = 0;
			int curCol = 0;
			int mapwidth = GameData.getInstance().getMapWidth();
			int mapheight = GameData.getInstance().getMapHeight();

			StringTokenizer tokens = new StringTokenizer(line);
			for (int y = 0; y < mapwidth * mapheight; y++) {

				String code = tokens.nextToken();

				curCol = y % mapwidth;
				if ((curCol == 0) && (layer == 1 || layer == 2) && (y != 0)) {
					curRow++;
				}

				// add special tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				if (Arrays.binarySearch(TileSet.IMPASSIBLE_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						data.setMapTileAt(c, TileSet.OBSTACLE);
					}
				} else if (Arrays.binarySearch(TileSet.BATTLE_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						data.setMapTileAt(c, TileSet.BATTLE);
					}
				} else {
					if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) && data.getMapTileAt(c) == null) {
						data.setMapTileAt(c, TileSet.NORMAL);
					}
				}
				data.addMapImageAt(layer, y, Integer.parseInt(code));
			}
		}
	}

	/**
	 * A function to load the map from the map file in configuration.
	 * 
	 * @param controller
	 *            - the GameController to store GameData in.
	 * @throws IOException
	 *             - if IO Errors occur
	 * @throws InterruptedException
	 *             - if a Thread is interrupted
	 */
	public void loadMap(GameController controller) throws IOException, InterruptedException {
		DebugUtility.printHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(
				new InputStreamReader(GameController.class.getResourceAsStream(Configuration.MAP_TO_LOAD)));
		SynchronizedReader reader = new SynchronizedReader(bReader);
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);

		// read the dimensions, and skip additional data until map data
		GameData.getInstance().setMapWidth(Integer.parseInt(tokens.nextToken()));
		GameData.getInstance().setMapHeight(Integer.parseInt(tokens.nextToken()));

		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		DebugUtility.printMessage("Initializing map tiles to null...");
		GameData.getInstance().initializeTileMapToNull();

		// initialize the Image map and add obstacles to the Tile map
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int layers = 0; layers < 3; layers++) {
			Thread t = new ProcessLayerThread(reader, controller, layers);
			t.start();
			threads.add(t);
		}

		for (Thread t : threads) {
			t.join();
		}
		DebugUtility.printMessage("Loaded map.");
	}
}
