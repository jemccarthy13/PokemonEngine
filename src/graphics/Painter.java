package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import party.Battler;
import party.Battler.STAT;
import tiles.Tile;
import trainers.Player;
import utilities.BattleEngine;
import controller.GameController;

/**
 * Paints all grahpics
 */
public class Painter {

	private static String ARROW = "Arrow";

	static GameController game;
	static GamePanel gamePanel;

	HashMap<Integer, Scene> scenePainters = new HashMap<Integer, Scene>();

	/**
	 * Singleton instance
	 */
	private static Painter instance = new Painter();

	/**
	 * Singleton instance
	 * 
	 * @return the painter instance
	 */
	public static Painter getInstance() {
		return instance;
	}

	/**
	 * Add a scene to the painters
	 * 
	 * @param scene
	 *            - the scene that can be rendered
	 */
	public void register(Scene scene) {
		instance.scenePainters.put(scene.getId(), scene);
	}

	/**
	 * Main painting logic from which submethods are called
	 * 
	 * @param g
	 *            - the graphics to paint
	 * @param panel
	 *            - the game panel
	 */
	public static void paintComponent(Graphics g, GamePanel panel) {

		game = panel.gameController;
		gamePanel = panel;

		switch (game.getScene().getId()) {
		case 0:
			instance.scenePainters.get(0).render(g, game);
			break;
		case 1:
			instance.scenePainters.get(1).render(g, game);
			break;
		case 2:
			instance.scenePainters.get(2).render(g, game);
			break;
		case 3:
			instance.scenePainters.get(3).render(g, game);
			break;
		case 4:
			instance.scenePainters.get(4).render(g, game);
			break;
		case 5:
			instance.scenePainters.get(5).render(g, game);
			break;
		case 6:
			instance.scenePainters.get(6).render(g, game);
			break;
		case 7:
			instance.scenePainters.get(7).render(g, game);
			break;
		case 8:
			// paintPartyScreen(g);
			break;
		case 9:
			instance.scenePainters.get(69).render(g, game);
			paintPauseMenu(g);
			break;
		case 10:
			// paintPartyScreen(g);
			break;
		case 11:
			paintPokedexScreen(g);
			break;
		case 12:
			// instance.scenePainters.get(6).render(g, game);
			// paintBagScreen(g);
			break;
		case 13:
			paintPokegearScreen(g);
			break;
		case 14:
			paintPlayerCard(g);
			break;
		case 15:
			paintOptionScreen(g);
			break;
		case 16:
			instance.scenePainters.get(69).render(g, game);
			paintSaveMenu(g);
			break;
		case 17:
			instance.scenePainters.get(69).render(g, game);
			paintConversation(g, game);
		case 18:
			instance.scenePainters.get(69).render(g, game);
			break;
		default:
			instance.scenePainters.get(69).render(g, game);
			break;
		}

		if (game.getScene() != NameScene.instance) {
			String[] message = game.getCurrentMessage();
			if (message != null) {
				paintMessageBox(g, message);
			}
		}
	}

	/**
	 * Paint a given string to a given location of the screen
	 * 
	 * @param g
	 *            - the graphics to paint with
	 * @param string
	 *            - the string to paint
	 * @param startX
	 *            - the starting x location
	 * @param startY
	 *            - the starting y location
	 */
	static void paintString(Graphics g, String string, int startX, int startY) {
		for (int x = 0; x < string.toCharArray().length; x++) {
			switch (string.toCharArray()[x]) {
			case '?':
				g.drawImage(SpriteLibrary.getImage("QUESTION"), startX + Tile.TILESIZE * x, startY, null);
				break;
			case '.':
				g.drawImage(SpriteLibrary.getImage("PERIOD"), startX + Tile.TILESIZE * x, startY, null);
				break;
			case ' ':
				g.drawImage(SpriteLibrary.getImage("SPACE"), startX + Tile.TILESIZE * x, startY, null);
				break;
			default:
				g.drawImage(SpriteLibrary.getInstance().getFontChar(string.toCharArray()[x]).getImage(), startX
						+ Tile.TILESIZE * x, startY, null);
			}
		}
	}

	/**
	 * Paint Battler information on the battle screen
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	public static void paintBattlerInfo(Graphics g) {
		Battler playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		Battler enemyPokemon = BattleEngine.getInstance().enemyCurrentPokemon;

		// player pokemon information
		int playerHealth = playerPokemon.getStat(STAT.HP);
		if (playerHealth > 0) {
			g.drawString(playerPokemon.getName(), 300, 175);
			g.drawString(String.valueOf(playerPokemon.getLevel()), 435, 175);
			g.drawString(String.valueOf(playerHealth), 361, 207);
			g.drawString(String.valueOf(playerPokemon.getMaxStat(STAT.HP)), 403, 207);
		}
		// enemy pokemon information
		int enemyHealth = enemyPokemon.getStat(STAT.HP);
		if (enemyHealth > 0) {
			g.drawString(enemyPokemon.getName(), 20, 25);
			g.drawString(String.valueOf(enemyPokemon.getLevel()), 145, 25);
			g.drawString(String.valueOf(enemyHealth), 70, 45);
			g.drawString(String.valueOf(enemyPokemon.getMaxStat(STAT.HP)), 112, 45);
		}
	}

	/**
	 * Paint components of the intro screen
	 * 
	 * TODO paint options based on current options
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintOptionScreen(Graphics g) {
		String imageName = game.isSoundOn() ? "OptionBG_SoundOn" : "OptionBG_SoundOff";
		g.drawImage(SpriteLibrary.getImage(imageName), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 22, 85 + 32 * game.getCurrentSelection(), null);
	}

	/**
	 * Paint components of the save menu
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintSaveMenu(Graphics g) {
		g.setColor(Color.BLACK);

		Player player = game.getPlayer();

		g.drawImage(SpriteLibrary.getImage("Save"), 0, 0, null);
		g.drawString(player.getName(), 100, 68);
		g.drawString(((Integer) player.getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(game.formatTime(), 76, 166);
		if (game.getCurrentSelection() == 0) {
			g.drawImage(SpriteLibrary.getImage(ARROW), 394, 148, null);
		} else if (game.getCurrentSelection() == 1) {
			g.drawImage(SpriteLibrary.getImage(ARROW), 394, 180, null);
		}
	}

	/**
	 * Pad a given string with appropriate spacing
	 * 
	 * @param toBePadded
	 *            - the string to be padded
	 * @return string + 12-len(string) spaces
	 */
	public static String getPadding(String toBePadded) {
		int numSpaces = 12 - toBePadded.length();
		String retStr = "";
		for (int x = 0; x < numSpaces; x++) {
			retStr += " ";
		}
		return retStr;
	}

	/**
	 * Paint components of the player card
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintPlayerCard(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("TrainerCard"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Male"), 320, 100, null);

		Player player = game.getPlayer();
		g.drawString("ID:  " + player.getID(), 295, 54);
		g.drawString("Name:" + getPadding("Name:") + player.getName(), 64, 93);
		g.drawString("Money:" + getPadding("Money:") + "$" + player.getMoney(), 64, 150);
		g.drawString("Pokedex:" + getPadding("Pokedex:") + player.getNumPokemonOwned(), 64, 183);
		g.drawString("Time:  " + getPadding("Time:") + game.formatTime(), 64, 213);
	}

	/**
	 * Paint components of the pokegear screen
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintPokegearScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("PokegearBG"), 0, 0, null);
		switch (game.getCurrentSelection()) {
		case 0:
			g.drawImage(SpriteLibrary.getImage("PokegearMap"), 0, 0, null);
			break;
		case 1:
			g.drawImage(SpriteLibrary.getImage("PokegearRadio"), 0, 0, null);
			break;
		case 2:
			g.drawImage(SpriteLibrary.getImage("PokegearPhone"), 0, 0, null);
			break;
		case 3:
			g.drawImage(SpriteLibrary.getImage("PokegearExit"), 0, 0, null);
			break;
		}
	}

	/**
	 * Paint components of the pokedex screen
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintPokedexScreen(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("PokedexBG"), 0, 0, null);
	}

	/**
	 * Paint components of the pause menu
	 * 
	 * @param g
	 *            - the graphics to paint
	 */
	private static void paintPauseMenu(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 335, 20 + 32 * game.getCurrentSelection(), null);
	}

	/**
	 * Paint the current conversation
	 * 
	 * @param g
	 *            - the graphics to paint with
	 * @param game
	 *            - the game controller of the game
	 */
	private static void paintConversation(Graphics g, GameController game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(game.getCurrentMessage()[0], 30, 260);
		g.drawString(game.getCurrentMessage()[1], 30, 290);
	}

	/**
	 * Paint a two line message
	 * 
	 * @param g
	 *            - the graphics to paint with
	 * @param line1
	 *            - the first message string
	 * @param line2
	 *            - the second message string
	 */
	static void paintMessageBox(Graphics g, String line1, String line2) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(line1, 30, 260);
		g.drawString(line2, 30, 290);
	}

	/**
	 * Paint a two line message
	 * 
	 * @param g
	 *            - the graphics to paint
	 * @param lines
	 *            - the two strings to paint
	 */
	private static void paintMessageBox(Graphics g, String[] lines) {
		paintMessageBox(g, lines[0], lines[1]);
	}
}
