package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

import controller.GameController;
import controller.TeleportLibrary;
import model.Configuration;
import model.Coordinate;
import model.GameMapList;
import tiles.BattleTile;
import tiles.Tile;
import tiles.TileSet;
import utilities.DebugUtility;

/**
 * A singleton class that loads the map layers into the game
 */
public class GameMap {
	CountDownLatch latch;

	private static GameMap instance = new GameMap();

	public static GameMap getInstance() {
		return instance;
	}

	private int width = 0;
	private int height = 0;

	/**
	 * Representation of the map images
	 */
	private GameMapList<Integer> imageMap = new GameMapList<>();

	/**
	 * Representation of the map tiles (characteristics such as obstacle or not)
	 */
	private GameMapList<Tile> tileMap = new GameMapList<>();

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * In a threaded environment, the file needs to be protected so the file
	 * reading can be synchronized between threads.
	 */
	private class SynchronizedReader {
		private BufferedReader reader;

		SynchronizedReader(BufferedReader r) {
			this.reader = r;
		}

		public synchronized String readLine() throws IOException {
			return this.reader.readLine();
		}
	}

	/**
	 * To speed up the map loading, each layer is processed in a thread.
	 * 
	 */
	private class ProcessLayerThread extends Thread {

		SynchronizedReader reader;
		int layer;
		int layerWidth;
		int layerHeight;

		ProcessLayerThread(int w, int h, SynchronizedReader r, int lyr) {
			this.reader = r;
			this.layer = lyr;
			this.layerWidth = w;
			this.layerHeight = h;
		}

		@Override
		public void run() {
			String line = null;
			try {
				line = this.reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int curRow = 0;
			int curCol = 0;

			StringTokenizer tokens = new StringTokenizer(line);
			for (int y = 0; y < this.layerWidth * this.layerHeight; y++) {

				String code = tokens.nextToken();

				curCol = y % this.layerWidth;
				if ((curCol == 0) && (this.layer == 1 || this.layer == 2) && (y != 0)) {
					curRow++;
				}

				// add special tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				if (Arrays.binarySearch(TileSet.IMPASSIBLE_TILES, Integer.parseInt(code)) >= 0) {
					if (this.layer == 1 || (this.layer == 2 && Integer.parseInt(code) > 0)) {
						setMapTileAt(c, TileSet.OBSTACLE);
					}
				} else if (Arrays.binarySearch(TileSet.BATTLE_TILES, Integer.parseInt(code)) >= 0) {
					if (this.layer == 1 || (this.layer == 2 && Integer.parseInt(code) > 0)) {
						setMapTileAt(c, TileSet.BATTLE);
					}
				} else {
					if ((this.layer == 1 || (this.layer == 2 && Integer.parseInt(code) > 0))
							&& getMapTileAt(c) == null) {
						setMapTileAt(c, TileSet.NORMAL);
					}
				}
				addMapImageAt(this.layer, y, Integer.parseInt(code));
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
		this.width = Integer.parseInt(tokens.nextToken());
		this.height = Integer.parseInt(tokens.nextToken());

		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		DebugUtility.printMessage("Loading map...");

		// initialize the Image map and add obstacles to the Tile map
		ArrayList<Thread> threads = new ArrayList<>();
		for (int layers = 0; layers < 3; layers++) {
			Thread t = new ProcessLayerThread(this.width, this.height, reader, layers);
			t.start();
			threads.add(t);
		}

		for (Thread t : threads) {
			t.join();
		}

		DebugUtility.printMessage("Loaded map.");
		DebugUtility.printMessage(this.tileMap.get(new Coordinate(4, 2).toString()).toString());
		DebugUtility.printMessage("" + this.imageMap.size());
	}

	/**
	 * Retrieve the tile image number at a given (layer, y) position
	 * 
	 * @param layer
	 *            - the layer to look in
	 * @param y
	 *            - the index of the image
	 * @return int representing which tile of the tileset should be painted
	 */
	public synchronized int getMapImageAt(int layer, int y) {
		int retInt = 0;
		Integer i = this.imageMap.get(new Coordinate(layer, y).toString());
		if (i != null) {
			retInt = this.imageMap.get(new Coordinate(layer, y).toString()).intValue();
		}
		return retInt;
	}

	/**
	 * Set the tile image number at a given (layer, y) position
	 * 
	 * @param layer
	 *            - the layer to look in
	 * @param y
	 *            - the index of the image
	 * @param value
	 *            - the new image number value
	 */
	public synchronized void setMapImageAt(int layer, int y, int value) {
		this.imageMap.set(new Coordinate(layer, y), Integer.valueOf(value));
	}

	/**
	 * If the map image location doesn't exit, add it
	 * 
	 * @param x
	 *            - layer index
	 * @param y
	 *            - element index within the layer
	 * @param value
	 *            - the value to add
	 */
	public synchronized void addMapImageAt(int x, int y, int value) {
		this.imageMap.put(new Coordinate(x, y).toString(), Integer.valueOf(value));
	}

	/**
	 * Retrieve a tile given a coordinate
	 * 
	 * @param c
	 *            - the coordinate to look at
	 * @return a Tile of the map
	 */
	public synchronized Tile getMapTileAt(Coordinate c) {
		return this.tileMap.get(c.toString());
	}

	/**
	 * Set a tile at a given coordinate
	 * 
	 * @param position
	 *            - the coordinate to set
	 * @param tile
	 *            - the tile to set that index to
	 */
	public synchronized void setMapTileAt(Coordinate position, Tile tile) {
		this.tileMap.set(position, tile);
	}

	public boolean isObstacleAt(Coordinate loc) {
		// TODO water tiles
		return (TileSet.compareTiles(getMapTileAt(loc), TileSet.OBSTACLE));
	}

	public boolean isBattleAt(Coordinate loc) {
		// TODO water tiles
		return getMapTileAt(loc).getClass().equals(BattleTile.class);
	}

	public static boolean isTeleportAt(Coordinate loc) {
		return TeleportLibrary.getList().containsKey(loc);
	}

	/**
	 * Check whether or not a coordinate is within the height and width of the
	 * map
	 * 
	 * @param loc
	 *            - the coordinate to check
	 * @return - true iff coordinate X and Y is > 0 and < height/width
	 */
	public boolean isInBounds(Coordinate loc) {
		// loc is > 0 but less than the bounds of the map
		return loc.getY() > 0 && loc.getY() <= this.height && loc.getX() > 0 && loc.getX() <= this.width;
	}
}
