package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.Timer;

import audio.AudioLibrary;
import graphics.SpriteLibrary;
import location.LocationLibrary;
import pokedex.Pokemon;
import pokedex.PokemonFactory;
import tiles.Tile;
import tiles.TileSet;
import trainers.Player;
import utilities.Coordinate;
import utilities.Utils;

//////////////////////////////////////////////////////////////////////////
//
// theGameInitializer loads a .SAV file or starts a new theGame.  Starts all 
// necessary threads and utilities.
//
// ////////////////////////////////////////////////////////////////////////
public class GameInitializer {

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
		// theGame = thetheGame;
		String loadedMap = "NewBarkTown";

		try {
			loadMap(theGame.gData, loadedMap);

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
				theGame.gData.player.setCurLocation(LocationLibrary.getLocation("Route 29"));
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
		} catch (IOException e) {
			System.err.println("Error initializing theGame.");
		}
		System.out.println("** Rendered session id: " + theGame.gData.id);
		return theGame.gData;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// loadMap - given a path to a map file, populate the Image and Tile data
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void loadMap(GameData data, String loadedMap) throws IOException {
		// intialize file reader
		System.err.println("Loading map...");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(GameInitializer.class.getResourceAsStream("/maps/" + loadedMap + ".map")));
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

		// intialize the Tile map
		for (int r = 0; r < data.map_height; r++) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			for (int c = 0; c < data.map_width; c++) {
				row.add(TileSet.NORMAL_TILE);
			}
			data.tm.add(row);
		}

		// initialize the Image map and add obstacles to the Tile map
		for (int layers = 0; layers < 3; layers++) {
			line = reader.readLine();
			int curRow = 0;
			int curCol = 0;
			tokens = new StringTokenizer(line);
			for (int y = 0; y < data.map_width * data.map_height; y++) {
				String code = tokens.nextToken();

				curCol = y % data.map_width;
				if ((curCol == 0) && (layers == 1 || layers == 2) && (y != 0)) {
					curRow++;
				}

				// add speical tiles to the map
				Coordinate c = new Coordinate(curCol, curRow);

				for (int x = 0; x < TileSet.IMPASSIBLE_TILES.length; x++) {
					if (Integer.parseInt(code) == TileSet.IMPASSIBLE_TILES[x]) {
						if (layers == 1 || (layers == 2 && Integer.parseInt(code) > 0))
							data.tm.set(c, TileSet.OBSTACLE);
					}
				}
				for (int x = 0; x < TileSet.WILD_tiLES.length; x++) {
					if (Integer.parseInt(code) == TileSet.WILD_tiLES[x]) {
						if (layers == 1 || (layers == 2 && Integer.parseInt(code) > 0))
							data.tm.set(c, TileSet.WILD_TILE);
					}
				}
				data.currentMap[layers][y] = Integer.parseInt(code);
			}
		}
		System.err.println("Loaded map.");
	}
}
