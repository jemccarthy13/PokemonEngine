package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import model.Configuration;
import party.Party;
import party.PartyMember;
import party.PartyMember.STAT;
import party.PartyMember.STATUS;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import controller.GameController;

// ////////////////////////////////////////////////////////////////////////////
//
// Painter class paints all grahpics
//
// ////////////////////////////////////////////////////////////////////////////
public class Painter {

	private static String ARROW = "Arrow";
	private static String partyFirstMember = "PartyFirst";
	private static String partyMember = "PartyBar";
	private static String partyBackground = "PartyBG";

	static GameController game;
	static GamePanel gamePanel;

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintComponent - main painting logic from which submethods are called
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintComponent(Graphics g, GamePanel panel) {

		game = panel.game;
		gamePanel = panel;

		switch (game.getScreen()) {
		case TITLE:
			paintTitle(g);
			break;
		case CONTINUE:
			paintContinueScreen(g);
			break;
		case INTRO:
			paintIntroScreen(g);
			break;
		case NAME:
			paintNameInputScreen(g);
			break;
		case BATTLE:
			paintBattle(g);
			break;
		case BATTLE_FIGHT:
			paintBattle(g);
			paintBattleFight(g);
			break;
		case BATTLE_MESSAGE:
			paintBattle(g);
			paintMessageBox(g, game);
			break;
		case BATTLE_ITEM:
			paintBagScreen(g);
			break;
		case BATTLE_POKEMON:
			paintPartyScreen(g);
			break;
		case MENU:
			paintWorld(g);
			paintPauseMenu(g);
			break;
		case POKEMON:
			paintPartyScreen(g);
			break;
		case POKEDEX:
			paintPokedexScreen(g);
			break;
		case BAG:
			paintBagScreen(g);
			break;
		case POKEGEAR:
			paintPokegearScreen(g);
			break;
		case TRAINERCARD:
			paintTrainerCard(g);
			break;
		case OPTION:
			paintOptionScreen(g);
			break;
		case SAVE:
			paintWorld(g);
			paintSaveMenu(g);
			break;
		case CONVERSATION:
			paintWorld(g);
			paintConversation(g, game);
		case MESSAGE:
			paintWorld(g);
			paintMessageBox(g, game);
			break;
		default:
			paintWorld(g);
			break;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the name input screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintNameInputScreen(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Namescreen"), 0, 0, null);

		if (game.getNameRowSelection() < 5) {
			g.drawImage(SpriteLibrary.getImage(ARROW), (int) (40 + Tile.TILESIZE * 2 * game.getNameColSelection()), 100
					+ Tile.TILESIZE * game.getNameRowSelection(), null);
		}
		if (game.getNameRowSelection() == 5) {
			g.drawImage(SpriteLibrary.getImage(ARROW), (int) (100 + Tile.TILESIZE * 6 * game.getNameColSelection()),
					100 + Tile.TILESIZE * game.getNameRowSelection(), null);
		}

		String name = game.getChosenName();

		for (int x = 0; x < Configuration.MAX_NAME_SIZE; x++) {
			g.drawImage(SpriteLibrary.getImage("_"), 150 + Tile.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			paintString(g, name, 150, 40);
		}
		if (name.length() < Configuration.MAX_NAME_SIZE) {
			g.drawImage(SpriteLibrary.getImage("CURSOR"), 150 + Tile.TILESIZE * name.length(), 40, null);
		}
		g.drawImage(SpriteLibrary.getSpriteForDir(game.getToBeNamed(), DIR.SOUTH).getImage(), 80, 30, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a given string to a given location of the screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintString(Graphics g, String string, int startX, int startY) {
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of a battle scene
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintBattle(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("BG"), 0, 0, null);
		paintPokemonInfo(g);

		PartyMember playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		PartyMember enemyPokemon = BattleEngine.getInstance().enemyCurrentPokemon;

		if (playerPokemon.getStat(STAT.HP) > 0) {
			g.drawImage(playerPokemon.getBackSprite().getImage(),
					120 - (playerPokemon.getBackSprite().getImage().getHeight(gamePanel)) / 2, 228 - playerPokemon
							.getBackSprite().getImage().getHeight(gamePanel), null);
		}
		if (enemyPokemon.getStat(STAT.HP) > 0) {
			g.drawImage(enemyPokemon.getFrontSprite().getImage(), 310, 25, null);
		}

		g.drawImage(SpriteLibrary.getImage("Battle"), 0, 0, null);
		String battleMessage = game.getCurrentMessage(false);
		if (battleMessage != null) {
			g.drawString(battleMessage, 30, 260);
		}
		g.drawString("FIGHT", 290, 260);
		g.drawString("PKMN", 400, 260);
		g.drawString("ITEM", 290, 290);
		g.drawString("RUN", 400, 290);

		int[] arrowX = { 274, 384 };
		int[] arrowY = { 240, 270 };

		int selX = BattleEngine.getInstance().currentSelectionMainX;
		int selY = BattleEngine.getInstance().currentSelectionMainY;

		// draw the arrow based on current selection
		g.drawImage(SpriteLibrary.getImage(ARROW), arrowX[selX], arrowY[selY], null);

		STATUS playerPartyStatus = playerPokemon.getStatusEffect();
		if (playerPartyStatus != STATUS.NORMAL) {
			g.drawImage(SpriteLibrary.getImage("Status" + playerPartyStatus), 415, 140, null);
		}

		STATUS enemyPartyStatus = enemyPokemon.getStatusEffect();
		if (enemyPartyStatus != STATUS.NORMAL) {
			g.drawImage(SpriteLibrary.getImage("Status" + enemyPartyStatus), 18, 60, null);
		}

		paintPokemonInfo(g);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the name input screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintBattleFight(Graphics g) {
		PartyMember playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;

		g.drawImage(SpriteLibrary.getImage("BattleFight"), 0, 0, null);

		int[] x = { 200, 345, 200, 345 };
		int[] y = { 260, 260, 290, 290 };

		// draw the moves
		for (int i = 0; i < playerPokemon.getNumMoves(); i++) {
			g.drawString(playerPokemon.getMove(i).name, x[i], y[i]);
		}

		int[] arrowX = { 184, 329 };
		int[] arrowY = { 240, 270 };

		int selX = BattleEngine.getInstance().currentSelectionFightX;
		int selY = BattleEngine.getInstance().currentSelectionFightY;

		// draw the arrow based on current selection
		g.drawImage(SpriteLibrary.getImage(ARROW), arrowX[selX], arrowY[selY], null);

		paintPokemonInfo(g);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint friendly & enemy Pokemon information
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPokemonInfo(Graphics g) {
		PartyMember playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		PartyMember enemyPokemon = BattleEngine.getInstance().enemyCurrentPokemon;

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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the intro screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintIntroScreen(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Beginning"), 0, 0, null);
		g.drawImage(SpriteLibrary.getSpriteForDir("PROFESSOROAK_LARGE", DIR.SOUTH).getImage(), 150, 20, null);
		if (NPCLibrary.getInstance().get("Professor Oak").getText().size() > game.getIntroStage()) {
			paintMessageBox(g, NPCLibrary.getInstance().get("Professor Oak").getText().get(game.getIntroStage() - 1),
					NPCLibrary.getInstance().get("Professor Oak").getText().get(game.getIntroStage()));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the options screen
	//
	// TODO paint options based on current options - adjust images
	// option sound is only on/off
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintOptionScreen(Graphics g) {
		String imageName = game.isSoundOn() ? "OptionBG_SoundOn" : "OptionBG_SoundOff";
		g.drawImage(SpriteLibrary.getImage(imageName), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 22, 85 + 32 * game.getCurrentSelection(), null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the save menu
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pad a given string with appropriate spacing
	//
	// ////////////////////////////////////////////////////////////////////////
	public static String getPadding(String toBePadded) {
		int numSpaces = 12 - toBePadded.length();
		String retStr = "";
		for (int x = 0; x < numSpaces; x++) {
			retStr += " ";
		}
		return retStr;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the Trainer Card
	//
	// TODO paint badges
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintTrainerCard(Graphics g) {
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokegear screen
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the bag screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintBagScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the party screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPartyScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage(partyBackground), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(partyFirstMember), 40, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 70, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 120, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 170, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 220, null);
		if (game.getPlayer().getPokemon().size() == 2) {
			g.drawImage(SpriteLibrary.getImage(partyMember), 190, 20, null);
		}
		Party playerPokemon = game.getPlayer().getPokemon();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon().getImage(), 75, 40, null);
			g.drawString((playerPokemon.get(0)).getName(), 65, 130);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokedex screen
	//
	// TODO - finish POKEDEX screen paint logic
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPokedexScreen(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("PokedexBG"), 0, 0, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pause menu
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPauseMenu(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 335, 20 + 32 * game.getCurrentSelection(), null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint the overworld -
	// include all NPCs, obstacles, map, interactive objects, and player
	//
	// TODO - update to paint only visible areas + 1 row + 1 column
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintWorld(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2.setTransform(at);

		g.setColor(Color.BLACK);
		g.setClip(new Rectangle(-16, -42, 704, 438));

		int offsetX = game.getOffsetX();
		int offsetY = game.getOffsetY();
		Player player = game.getPlayer();
		int map_height = game.getMapHeight();
		int map_width = game.getMapWidth();

		int startX = game.getStartX();
		int startY = game.getStartY();

		g.translate(offsetX - 32, offsetY - 64);

		for (int layer = 1; layer < 3; layer++) {
			int x_coor = startX;
			int y_coor = startY;

			int tile_number = 0;
			for (int y = 1; y <= map_height; y++) {
				for (int x = 1; x <= map_width; x++) {
					int tilePic = game.getMapImageAt(layer, tile_number);

					if (!(layer == 2 && tilePic == 0)) {
						g.drawImage((Image) TileSet.getInstance().get(tilePic), x_coor, y_coor, null);
					}
					x_coor += Tile.TILESIZE;
					tile_number++;
				}
				x_coor = startX;
				y_coor += Tile.TILESIZE;
			}
		}

		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			g.drawImage(curNPC.tData.sprite.getImage(), curNPC.getCurrentX() * 32 + startX, curNPC.getCurrentY() * 32
					+ startY - 10, null);
			game.setMapTileAt(curNPC.getPosition(), TileSet.OBSTACLE);
		}

		// TODO - remove this line and the 2 setTranform lines to start
		// debugging transition to 20x20 painting
		g.translate(-offsetX, -offsetY);

		g2.setTransform(at);
		g.drawImage(player.tData.sprite.getImage(), 224, 118, null);
		g.setColor(Color.WHITE);
		g.drawString(player.getCurrentX() + "," + player.getCurrentY(), 10, 25);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the continue screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintContinueScreen(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Continue"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 13, 20 + 32 * game.getCurrentSelection(), null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the title screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintTitle(Graphics g) {
		g.drawImage(SpriteLibrary.getImage("Title"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Start"), 0, 260, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a conversation on the screen as a series of message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintConversation(Graphics g, GameController game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(game.getCurrentMessage(true), 30, 260);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintMessageBox - utility to print a one line message
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintMessageBox(Graphics g, GameController game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(game.getCurrentMessage(false), 30, 260);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintMessageBox - utility to print a two line message
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintMessageBox(Graphics g, String line1, String line2) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(line1, 30, 260);
		g.drawString(line2, 30, 290);
	}

}
