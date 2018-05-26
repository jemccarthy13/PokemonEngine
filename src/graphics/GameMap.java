package graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

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

	private static GameMap instance = new GameMap();

	public static GameMap getInstance() {
		return instance;
	}

	private int width = 0;
	private int height = 0;

	/**
	 * Representation of the map images
	 */
	private GameMapList<Integer> imageMap = new GameMapList<Integer>();

	/**
	 * Representation of the map tiles (characteristics such as obstacle or not)
	 */
	private GameMapList<Tile> tileMap = new GameMapList<Tile>();

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
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
		int layer;
		int width;
		int height;

		ProcessLayerThread(int w, int h, SynchronizedReader r, int lyr) {
			reader = r;
			layer = lyr;
			width = w;
			height = h;
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

			StringTokenizer tokens = new StringTokenizer(line);
			for (int y = 0; y < width * height; y++) {

				String code = tokens.nextToken();

				curCol = y % width;
				if ((curCol == 0) && (layer == 1 || layer == 2) && (y != 0)) {
					curRow++;
				}

				// add special tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				if (Arrays.binarySearch(TileSet.IMPASSIBLE_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						setMapTileAt(c, TileSet.OBSTACLE);
					}
				} else if (Arrays.binarySearch(TileSet.BATTLE_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						setMapTileAt(c, TileSet.BATTLE);
					}
				} else {
					if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) && getMapTileAt(c) == null) {
						setMapTileAt(c, TileSet.NORMAL);
					}
				}
				addMapImageAt(layer, y, Integer.parseInt(code));
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

		DebugUtility.printMessage("Initializing map tiles to null...");
		initializeTileMapToNull();

		// initialize the Image map and add obstacles to the Tile map
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int layers = 0; layers < 3; layers++) {
			Thread t = new ProcessLayerThread(width, height, reader, layers);
			t.start();
			threads.add(t);
		}

		for (Thread t : threads) {
			t.join();
		}
		DebugUtility.printMessage("Loaded map.");
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
	public int getMapImageAt(int layer, int y) {
		Integer i = imageMap.get(new Coordinate(y, layer));
		if (i == null) {
			return 0;
		} else {
			return imageMap.get(new Coordinate(y, layer));
		}
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
	public void setMapImageAt(int layer, int y, int value) {
		imageMap.set(new Coordinate(y, layer), value);
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
	public void addMapImageAt(int x, int y, int value) {
		List<Integer> layer = null;
		// set up a new layer if it doesn't exist
		try {
			layer = imageMap.get(x);
		} catch (IndexOutOfBoundsException e) {
			imageMap.add(new ArrayList<Integer>());
		}
		if (layer != null) {
			if (layer.size() <= y) {
				for (int i = 0; i < y + 1; i++) {
					layer.add(0);
				}
			}
			layer.set(y, value);
		}
	}

	/**
	 * Retrieve a tile given a coordinate
	 * 
	 * @param c
	 *            - the coordinate to look at
	 * @return a Tile of the map
	 */
	public Tile getMapTileAt(Coordinate c) {
		return tileMap.get(c);
	}

	/**
	 * Set a tile at a given coordinate
	 * 
	 * @param position
	 *            - the coordinate to set
	 * @param tile
	 *            - the tile to set that index to
	 */
	public void setMapTileAt(Coordinate position, Tile tile) {
		tileMap.set(position, tile);
	}

	/**
	 * Set the tileMap to contain null tiles
	 */
	public void initializeTileMapToNull() {
		for (int r = 0; r < GameMap.getInstance().getHeight(); r++) {
			Tile[] numbers = new Tile[GameMap.getInstance().getWidth()];
			Arrays.fill(numbers, null);
			List<Tile> row = Arrays.asList(numbers);
			tileMap.add(row);
		}
	}

	public boolean isObstacleAt(Coordinate loc) {
		// TODO water tiles
		return (TileSet.compareTiles(getMapTileAt(loc), TileSet.OBSTACLE));
	}

	public boolean isBattleAt(Coordinate loc) {
		// TODO water tiles
		return getMapTileAt(loc).getClass().equals(BattleTile.class);
	}

	public boolean isTeleportAt(Coordinate loc) {
		return TeleportLibrary.getList().containsKey(loc);
	}

	/**
	 * Check whether or not a coordinate is within the height and width of the map
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
