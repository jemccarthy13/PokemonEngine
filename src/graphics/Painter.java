package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import model.Coordinate;
import party.Battler;
import party.Battler.STAT;
import tiles.Tile;
import trainers.Actor.DIR;
import utilities.BattleEngine;
import controller.GameController;

/**
 * One central location where all painting objects can register to be
 * 'rendered'.
 * 
 * The "Scenes" of the game register with the Painter, and when an update is
 * requested the Painter will call the Scene's "render" method based on the
 * game's current scene state.
 */
public class Painter {

	/**
	 * The data structure that stores all the Scenes that know how to render
	 * game graphics
	 */
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
	public void register(BaseScene scene) {
		instance.scenePainters.put(scene.getClass().hashCode(), scene);
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

		GameController control = panel.gameController;

		// based on the current scene, render the appropriate images
		int sceneToRender = control.getScene().getClass().hashCode();
		instance.scenePainters.get(sceneToRender).render(g, control);

		// At any scene other than the name screen, message boxes
		// will interrupt any underlying functionality and take priority until
		// they are dismissed
		if (control.getScene() != NameScene.instance) {
			String[] message = control.getCurrentMessage();
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
	static void paintSmallString(Graphics g, String string, int startX, int startY) {
		string = string.toUpperCase();
		int offset = ((int) (Tile.TILESIZE / 2.7)) - 2;
		for (int x = 0; x < string.toCharArray().length; x++) {
			switch (string.toCharArray()[x]) {
			case '?':
				g.drawImage(SpriteLibrary.getImage("QUESTION"), startX + offset * x, startY, null);
				break;
			case '.':
				g.drawImage(SpriteLibrary.getImage("PERIOD"), startX + offset * x, startY, null);
				break;
			case ' ':
				g.drawImage(SpriteLibrary.getImage("SPACE_small"), startX + offset * x, startY, null);
				break;
			case ':':
				g.drawImage(SpriteLibrary.getImage("COLON_small"), startX + offset * x, startY, null);
				break;
			default:
				g.drawImage(SpriteLibrary.getInstance().getSmallFontChar(string.toCharArray()[x]).getImage(), startX
						+ offset * x, startY, null);
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
		string = string.toUpperCase();
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
			case ':':
				g.drawImage(SpriteLibrary.getImage("COLON"), startX + Tile.TILESIZE * x, startY, null);
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

	/**
	 * Draw an exclamation box above the NPCs head
	 * 
	 * @param g
	 *            - the graphics to paint on
	 * @param game
	 *            - the controller
	 * @param npcPosition
	 *            - the position of the NPC that saw the player
	 */
	public static void paintTrainerSighted(Graphics g, GameController game, Coordinate npcPosition) {

		int offsetX = game.getOffsetX();
		int offsetY = game.getOffsetY();

		Coordinate sightedBoxLocation = npcPosition.move(DIR.NORTH);
		g.translate(offsetX - Tile.TILESIZE, offsetY - 2 * Tile.TILESIZE);

		g.drawImage(SpriteLibrary.getImage("trainer-sighted"),
				Tile.TILESIZE * sightedBoxLocation.getX() + game.getStartX(), sightedBoxLocation.getY() * Tile.TILESIZE
						+ game.getStartY() - 10, null);
		g.translate(-offsetX, -offsetY);

	}
}
