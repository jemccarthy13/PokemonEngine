package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import utilities.EnumsAndConstants;
import utilities.Utils;
import data_structures.Location;
import data_structures.Pokemon;

public class GameInitializer {

	static Main game;

	public static void startgame(boolean continued, Main theGame) {
		game = theGame;
		game.currentMapNPC = EnumsAndConstants.npc_lib.getAll();
		String loadedMap = "/mapmaker/Maps/Johto.map";

		EnumsAndConstants.initializeJukeBox();

		try {
			loadMap(loadedMap);
		} catch (IOException e) {
			System.out.println(e.toString());
		}
		if (continued) {
			String name = "Gold";
			game.gold.setName(name);
			game.gold.createTrainerID();
			game.gold.setCurrentX(34);
			game.gold.setCurrentY(15);
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Charmander", 9);
			game.gold.caughtPokemon(charmander);
			game.gold.setMoney(1000000);
			Utils.playBackgroundMusic(EnumsAndConstants.MUSIC.NEWBARKTOWN);
			// game.gold.setCurLoc(new
			// Location(EnumsAndConstants.loc_lib.getLocation("New Bark Town").name));
		} else {
			String name = "Gold";
			game.gold.setName(name);
			game.gold.createTrainerID();
			game.gold.setCurrentX(36);
			game.gold.setCurrentY(4);
			Pokemon charmander = EnumsAndConstants.pokemon_generator.createPokemon("Rattatta", 5);
			game.gold.caughtPokemon(charmander);
			game.gold.setMoney(2000);
			game.gold.setCurLoc(new Location(EnumsAndConstants.loc_lib.getLocation("New Bark Town").name));
			game.introScreen.Start();
		}
		game.start_coorX = (EnumsAndConstants.TILESIZE * (8 - game.gold.getCurrentX()));
		game.start_coorY = (EnumsAndConstants.TILESIZE * (6 - game.gold.getCurrentY()));
		game.atTitle = false;
		game.atContinueScreen = false;
		game.movable = true;
		game.timeStarted = System.currentTimeMillis();
	}

	public static void loadMap(String loadedMap) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream(loadedMap)));
		String line = reader.readLine();
		StringTokenizer tokens = new StringTokenizer(line);
		game.map_width = Integer.parseInt(tokens.nextToken());
		game.map_height = Integer.parseInt(tokens.nextToken());
		line = reader.readLine();
		tokens = new StringTokenizer(line);
		while (!line.equals(".")) {
			line = reader.readLine();
		}
		for (int layers = 0; layers < 3; layers++) {
			line = reader.readLine();
			int curRow = 0;
			int curCol = 0;
			tokens = new StringTokenizer(line);
			for (int y = 0; y < game.map_width * game.map_height; y++) {
				String code = tokens.nextToken();

				curCol = y % game.map_width;
				if ((curCol == 0) && (layers == 1 || layers == 2) && (y != 0)) {
					curRow++;
				}
				addObstaclesToMap(layers, curRow, curCol, code);
				game.currentMap[layers][y] = Integer.parseInt(code);
			}
		}
	}

	private static void addObstaclesToMap(int layers, int curRow, int curCol, String code) {
		if (layers == 1) {
			game.tileMap[curRow][curCol] = EnumsAndConstants.TILE;
			for (int x = 0; x < EnumsAndConstants.impassible.length; x++) {
				if (Integer.parseInt(code) == EnumsAndConstants.impassible[x]) {
					game.tileMap[curRow][curCol] = EnumsAndConstants.OBSTACLE;
				}
			}
		}
		if (layers == 2) {
			for (int x = 0; x < EnumsAndConstants.impassible.length; x++) {
				if (Integer.parseInt(code) > 0 && Integer.parseInt(code) == EnumsAndConstants.impassible[x]) {
					game.tileMap[curRow][curCol] = EnumsAndConstants.OBSTACLE;
				}
			}
		}
	}

}
