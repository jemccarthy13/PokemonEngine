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
import utilities.Utils;

public class GameInitializer {

	static Main game;

	public static void startgame(boolean continued, Main theGame) {
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
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Charmander", 9);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(1000000);
			Utils.playBackgroundMusic(EnumsAndConstants.MUSIC.NEWBARKTOWN);
		} else {
			String name = "GOLD";
			game.gData.player = new Player(36, 4, name);
			game.gData.player.setID(Utils.createTrainerID());
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Rattatta", 5);
			game.gData.player.caughtPokemon(charmander);
			game.gData.player.setMoney(2000);
			game.introScreen.Start();
		}
		game.gData.start_coorX = (EnumsAndConstants.TILESIZE * (8 - game.gData.player.getCurrentX()));
		game.gData.start_coorY = (EnumsAndConstants.TILESIZE * (6 - game.gData.player.getCurrentY()));
		game.gData.atTitle = false;
		game.gData.atContinueScreen = false;
		game.movable = true;
		game.gData.timeStarted = System.currentTimeMillis();
		game.NPCTHREAD.start();
	}

	public static void loadMap(String loadedMap) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(loadedMap)));
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);
		game.gData.map_width = Integer.parseInt(tokens.nextToken());
		game.gData.map_height = Integer.parseInt(tokens.nextToken());
		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}
		for (int r = 0; r < game.gData.map_height; r++) {
			ArrayList<Tile> row = new ArrayList<Tile>();
			for (int c = 0; c < game.gData.map_width; c++) {
				row.add(EnumsAndConstants.TILE);
			}
			game.gData.tm.add(row);
		}
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
