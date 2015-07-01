package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import audio.AudioLibrary;
import location.LocationLibrary;
import pokedex.Pokemon;
import pokedex.PokemonFactory;
import tiles.Tile;
import tiles.TileSet;
import trainers.Player;
import utilities.Coordinate;

//////////////////////////////////////////////////////////////////////////
//
// GameInitializer loads a .SAV file or starts a new game.  Starts all 
// necessary threads and utilities.
//
// ////////////////////////////////////////////////////////////////////////
public class GameInitializer {

	static Game game;

	// ////////////////////////////////////////////////////////////////////////
	//
	// startGame method - given whether or not the game is a continue, start
	// the game based off a save file, or start a new game.
	// Prepares all the necessary utilities, such as
	//
	// TODO - add load from .SAV feature
	// TODO - map to be used is scriptable in data file with other "EVENTS"
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void startGame(boolean continued, Game theGame) {
		game = theGame;
		String loadedMap = "NewBarkTown";

		// TODO verify juke box is initialized properly
		// EnumsAndConstants.initializeJukeBox();

		try {
			loadMap(loadedMap);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		if (continued) {
			String name = "GOLD";
			game.gData.player = new Player(6, 10, name);
			Pokemon charmander = PokemonFactory.createPokemon("Charmander", 90);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(1000000);
			game.gData.timeStarted = System.currentTimeMillis();
			game.gData.player.setCurLocation(LocationLibrary.getLocation("Route 29"));
			AudioLibrary.getInstance().playBackgroundMusic("NewBarkTown", game.gData.option_sound);
		} else {
			String name = "GOLD";
			game.gData.player = new Player(50, 20, name);
			Pokemon charmander = PokemonFactory.createPokemon("Rattatta", 5);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(2000);
			game.gData.inIntro = true;
			game.gData.timeStarted = System.currentTimeMillis();
			game.gData.player.setCurLocation(LocationLibrary.getLocation("Route 29"));
			AudioLibrary.getInstance().playBackgroundMusic("Intro", game.gData.option_sound);
		}
		game.gData.start_coorX = (Tile.TILESIZE * (8 - game.gData.player.getCurrentX()));
		game.gData.start_coorY = (Tile.TILESIZE * (6 - game.gData.player.getCurrentY()));
		game.gData.atTitle = false;
		game.gData.atContinueScreen = false;
		game.movable = true;
		game.NPCTHREAD.start();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadMap - given a path to a map file, populate the Image and Tile data
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void loadMap(String loadedMap) throws IOException {
		// intialize file reader
		System.err.println("Loading map...");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(GameInitializer.class.getResourceAsStream("/maps/" + loadedMap + ".map")));
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);

		// read the dimensions, and skip additional data until map data
		game.gData.map_width = Integer.parseInt(tokens.nextToken());
		game.gData.map_height = Integer.parseInt(tokens.nextToken());

		System.out.println(game.gData.map_height);
		System.out.println(game.gData.map_width);
		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		// intialize the Tile map
		for (int r = 0; r < game.gData.map_height; r++) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			for (int c = 0; c < game.gData.map_width; c++) {
				row.add(TileSet.NORMAL_TILE);
			}
			game.gData.tm.add(row);
		}

		// initialize the Image map and add obstacles to the Tile map
		for (int layers = 0; layers < 3; layers++) {
			line = reader.readLine();
			int curRow = 0;
			int curCol = 0;
			tokens = new StringTokenizer(line);
			for (int y = 0; y < game.gData.map_width * game.gData.map_height; y++) {
				String code = tokens.nextToken();

				curCol = y % game.gData.map_width;
				if ((curCol == 0) && (layers == 1 || layers == 2) && (y != 0)) {
					curRow++;
				}

				// add speical tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				for (int x = 0; x < TileSet.IMPASSIBLE_TILES.length; x++) {
					if (Integer.parseInt(code) == TileSet.IMPASSIBLE_TILES[x]) {
						if (layers == 1 || (layers == 2 && Integer.parseInt(code) > 0))
							game.gData.tm.set(c, TileSet.OBSTACLE);
					}
				}
				for (int x = 0; x < TileSet.WILD_tiLES.length; x++) {
					if (Integer.parseInt(code) == TileSet.WILD_tiLES[x]) {
						if (layers == 1 || (layers == 2 && Integer.parseInt(code) > 0))
							game.gData.tm.set(c, TileSet.WILD_TILE);
					}
				}
				game.gData.currentMap[layers][y] = Integer.parseInt(code);
			}
		}
		System.err.println("Loaded map.");
	}
}
