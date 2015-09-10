package driver;

import graphics.SpriteLibrary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Timer;

import location.LocationLibrary;
import pokedex.Pokemon;
import pokedex.PokemonFactory;
import tiles.Tile;
import tiles.TileSet;
import trainers.Player;
import utilities.Coordinate;
import utilities.DebugUtility;
import utilities.Utils;
import audio.AudioLibrary;

//////////////////////////////////////////////////////////////////////////
//
// theGameInitializer loads a .SAV file or starts a new theGame.  Starts all 
// necessary threads and utilities.
//
// ////////////////////////////////////////////////////////////////////////
public class GameInitializer {

	static GameInitializer m_instance = new GameInitializer();

	// ////////////////////////////////////////////////////////////////////////
	//
	// starttheGame method - given whether or not the theGame is a continue,
	// start
	// the theGame based off a save file, or start a new theGame.
	// Prepares all the necessary utilities, such as
	//
	// TODO - map to be used is scriptable in data file with other "EVENTS"
	//
	// ////////////////////////////////////////////////////////////////////////
	public static GameData startGame(boolean continued, Game theGame) {
		if (continued) {
			theGame.gData = Utils.loadGame();
			if (!theGame.gData.player.tData.isValidData()) {
				System.err.println(theGame.gData.player.tData.toString());
			}
		} else {
			String name = "GOLD";
			theGame.gData.player = new Player(4, 2, name);
			Pokemon charmander = PokemonFactory.createPokemon("Charmander", 7);
			theGame.gData.player.caughtPokemon(charmander);
			theGame.gData.player.setMoney(1000000);
			theGame.gData.player.setCurLocation(LocationLibrary.getLocation("Route 27"));
			AudioLibrary.getInstance().playBackgroundMusic("NewBarkTown", theGame.gData.option_sound);
			theGame.gData.start_coorX = (Tile.TILESIZE * (8 - theGame.gData.player.getCurrentX()));
			theGame.gData.start_coorY = (Tile.TILESIZE * (6 - theGame.gData.player.getCurrentY()));
			if (theGame.gData.SHOWINTRO) {
				theGame.gData.introStage = 1;
				theGame.gData.inIntro = true;
			}
		}

		// initialize the player sprite
		theGame.gData.player.tData.sprite_name = "PLAYER";
		theGame.gData.player.tData.sprite = SpriteLibrary.getInstance().getSprites("PLAYER")
				.get(theGame.gData.player.getDirection().ordinal() * 3);

		// get out of any menus
		theGame.gData.atTitle = false;
		theGame.gData.atContinueScreen = false;
		theGame.gData.inMenu = false;
		theGame.NPCTHREAD.start();

		// start clock for current session
		theGame.gData.gameTimeStruct.timeStarted = System.currentTimeMillis();
		theGame.gData.gameTimer = new Timer(100 - theGame.gData.currentSpeed, theGame);
		theGame.gData.gameTimer.start();

		System.out.println(theGame.gData.player.tData.toString());
		System.out.println("** Rendered session id: " + theGame.gData.id);
		return theGame.gData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadMap - given a path to a map file, populate the Image and Tile data
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void loadMap(GameData data, String loadedMap) throws Exception {

		DebugUtility.debugHeader("Loading Map");

		// intialize file reader
		BufferedReader bReader = new BufferedReader(new InputStreamReader(
				GameInitializer.class.getResourceAsStream("/maps/" + loadedMap + ".map")));
		SynchronizedReader reader = m_instance.new SynchronizedReader(bReader);
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);

		// read the dimensions, and skip additional data until map data
		data.map_width = Integer.parseInt(tokens.nextToken());
		data.map_height = Integer.parseInt(tokens.nextToken());

		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		DebugUtility.printMessage("Initializing map tiles to null...");
		// initialize the Tile map
		for (int r = 0; r < data.map_height; r++) {

			Tile[] numbers = new Tile[data.map_width];
			Arrays.fill(numbers, null);
			List<Tile> row = Arrays.asList(numbers);
			data.tm.add(row);
		}

		DebugUtility.printMessage(" - Map tiles: " + data.map_height * data.map_width + " tiles initialized.");

		// initialize the Image map and add obstacles to the Tile map
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for (int layers = 0; layers < 3; layers++) {
			Thread t = m_instance.new ProcessLayerThread(reader, data, layers);
			t.start();
			threads.add(t);
		}

		for (Thread t : threads) {
			t.join();
		}
		DebugUtility.printMessage("Loaded map.");
	}

	class ProcessLayerThread extends Thread {

		SynchronizedReader reader;
		GameData data;
		int layer;

		ProcessLayerThread(SynchronizedReader r, GameData gameData, int lyr) {
			reader = r;
			data = gameData;
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
			StringTokenizer tokens = new StringTokenizer(line);
			for (int y = 0; y < data.map_width * data.map_height; y++) {

				String code = tokens.nextToken();

				curCol = y % data.map_width;
				if ((curCol == 0) && (layer == 1 || layer == 2) && (y != 0)) {
					curRow++;
				}

				// add special tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				if (Arrays.binarySearch(TileSet.IMPASSIBLE_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						data.tm.set(c, TileSet.OBSTACLE);
					}
				} else if (Arrays.binarySearch(TileSet.WILD_TILES, Integer.parseInt(code)) >= 0) {
					if (layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) {
						data.tm.set(c, TileSet.WILD_TILE);
					}
				} else {
					if ((layer == 1 || (layer == 2 && Integer.parseInt(code) > 0)) && data.tm.getTileAt(c) == null) {
						data.tm.set(c, TileSet.NORMAL_TILE);
					}
				}
				data.currentMap[layer][y] = Integer.parseInt(code);
			}
		}
	}

	class SynchronizedReader {
		BufferedReader reader;

		SynchronizedReader(BufferedReader r) {
			reader = r;
		}

		public synchronized String readLine() throws IOException {
			return reader.readLine();
		}
	}
}
