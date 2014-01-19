package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import pokedex.Pokemon;
import tiles.Coordinate;
import tiles.Tile;
import trainers.Player;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.MUSIC;
import utilities.Utils;

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
		String loadedMap = "/mapmaker/Maps/Johto.map";

		EnumsAndConstants.initializeJukeBox();

		try {
			loadMap(loadedMap);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		if (continued) {
			String name = "GOLD";
			game.gData.player = new Player(34, 15, name);
			game.gData.player.setID(Utils.createTrainerID());
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Charmander", 90);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(1000000);
			game.gData.timeStarted = System.currentTimeMillis();
			Utils.playBackgroundMusic(EnumsAndConstants.MUSIC.NEWBARKTOWN);
		} else {
			String name = "GOLD";
			game.gData.player = new Player(36, 4, name);
			game.gData.player.setID(Utils.createTrainerID());
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Rattatta", 5);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(2000);
			game.gData.inIntro = true;
			game.gData.timeStarted = System.currentTimeMillis();
			Utils.playBackgroundMusic(MUSIC.INTRO);
		}
		game.gData.start_coorX = (EnumsAndConstants.TILESIZE * (8 - game.gData.player.getCurrentX()));
		game.gData.start_coorY = (EnumsAndConstants.TILESIZE * (6 - game.gData.player.getCurrentY()));
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
		// intiialize file reader
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				GameInitializer.class.getResourceAsStream(loadedMap)));
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);

		// read the dimensions, and skip additional data until map data
		game.gData.map_width = Integer.parseInt(tokens.nextToken());
		game.gData.map_height = Integer.parseInt(tokens.nextToken());
		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}

		// intialize the Tile map
		for (int r = 0; r < game.gData.map_height; r++) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			for (int c = 0; c < game.gData.map_width; c++) {
				row.add(EnumsAndConstants.TILE);
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
				addObstaclesToMap(layers, curRow, curCol, code);
				game.gData.currentMap[layers][y] = Integer.parseInt(code);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// addObstaclesToMap - based on the codes read, add appropriate obstacles
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void addObstaclesToMap(int layers, int curRow, int curCol, String code) {
		Coordinate c = new Coordinate(curCol, curRow);

		for (int x = 0; x < EnumsAndConstants.impassible.length; x++) {
			if (Integer.parseInt(code) == EnumsAndConstants.impassible[x]) {
				if (layers == 1 || (layers == 2 && Integer.parseInt(code) > 0))
					game.gData.tm.set(c, EnumsAndConstants.OBSTACLE);
			}
		}
	}
}
