package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonList;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import driver.Configuration;
import driver.GameController;
import driver.GameData;
import driver.GamePanel;

// ////////////////////////////////////////////////////////////////////////////
//
// Painter class paints all grahpics
//
// TODO - add badges printing
// TODO - only paint the visible portions of the map
//
// ////////////////////////////////////////////////////////////////////////////
public class Painter extends JPanel {

	private static final long serialVersionUID = 2538625975768781653L;

	private static String ARROW = "Arrow";
	private static String partyFirstMember = "PartyFirst";
	private static String partyMember = "PartyBar";
	private static String partyBackground = "PartyBG";

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintComponent - main painting logic from which submethods are called
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintComponent(Graphics g, GamePanel game) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2.setTransform(at);
		if (game.gData.atTitle) {
			paintTitle(g, game.game);
		} else if (game.gData.atContinueScreen) {
			paintContinueScreen(g, game.gData);
		} else if (game.gData.inIntro) {
			paintIntroScreen(g, game);
		} else if (game.gData.inNameScreen) {
			paintNameInputScreen(g, game);
		} else if (game.menuScreen.MENU_inPokeDex) {
			paintPokedexScreen(g);
		} else if (game.menuScreen.MENU_inPokemon) {
			paintPartyScreen(g, game);
		} else if (game.menuScreen.MENU_inBag) {
			paintBagScreen(g);
		} else if (game.menuScreen.MENU_inPokeGear) {
			paintPokegearScreen(g, game.menuScreen);
		} else if (game.menuScreen.MENU_inTrainerCard) {
			paintTrainerCard(g, game);
		} else if (game.menuScreen.MENU_inOption) {
			paintOptionScreen(g, game);
		} else if (game.gData.inBattle) {
			paintBattle(g, game);
		} else if (!game.gData.inBattle) {
			paintWorld(g, game.game, g2, at);
		}

		if (game.menuScreen.MENU_inMain) {
			paintPauseMenu(g, game.menuScreen);
		} else if (game.menuScreen.MENU_inConversation) {
			paintConversation(g, game);
		} else if (game.menuScreen.MENU_inSave) {
			paintSaveMenu(g, game);
		} else if (game.gData.inMessage) {
			paintMessageBox(g, game);
		}

	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintMessageBox - utility to print a one line message
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintMessageBox(Graphics g, GamePanel game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(game.messageString, 30, 260);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintMessageBox - utility to print a two line message
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintMessageBox(Graphics g, String line1, String line2) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("MessageBox"), 0, 0, null);
		g.drawString(line1, 30, 260);
		g.drawString(line2, 30, 290);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the name input screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintNameInputScreen(Graphics g, GamePanel game) {
		g.drawImage(SpriteLibrary.getImage("Namescreen"), 0, 0, null);

		if (game.nameScreen.rowSelection < 5) {
			g.drawImage(SpriteLibrary.getImage(ARROW), (int) (40 + Tile.TILESIZE * 2 * game.nameScreen.colSelection),
					100 + Tile.TILESIZE * game.nameScreen.rowSelection, null);
		}
		if (game.nameScreen.rowSelection == 5) {
			g.drawImage(SpriteLibrary.getImage(ARROW), (int) (100 + Tile.TILESIZE * 6 * game.nameScreen.colSelection),
					100 + Tile.TILESIZE * game.nameScreen.rowSelection, null);
		}

		String name = game.nameScreen.getChosenName();

		for (int x = 0; x < Configuration.MAX_NAME_SIZE; x++) {
			g.drawImage(SpriteLibrary.getImage("_"), 150 + Tile.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			paintString(g, name, 150, 40);
		}
		if (name.length() < Configuration.MAX_NAME_SIZE) {
			g.drawImage(SpriteLibrary.getImage("CURSOR"), 150 + Tile.TILESIZE * name.length(), 40, null);
		}
		g.drawImage(SpriteLibrary.getSpriteForDir(game.nameScreen.toBeNamed, DIR.SOUTH).getImage(), 80, 30, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a given string to a given location of the screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintString(Graphics g, String string, int startX, int startY) {
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
	// TODO - analyze function arguments for potential decrease in passing large
	// data
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintBattle(Graphics g, GamePanel game) {
		g.drawImage(SpriteLibrary.getImage("BG"), 0, 0, null);

		Pokemon playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		Pokemon enemyPokemon = BattleEngine.getInstance().enemyPokemon.get(0);

		g.drawImage(playerPokemon.getBackSprite().getImage(),
				120 - (playerPokemon.getBackSprite().getImage().getHeight(game)) / 2, 228 - playerPokemon
						.getBackSprite().getImage().getHeight(game), null);
		g.drawImage(enemyPokemon.getFrontSprite().getImage(), 310, 25, null);

		if (BattleEngine.getInstance().inMain) {
			g.drawImage(SpriteLibrary.getImage("Battle"), 0, 0, null);
			g.drawString("Wild " + enemyPokemon.getName() + " Appeared!", 30, 260);
			g.drawString("FIGHT", 290, 260);
			g.drawString("PKMN", 400, 260);
			g.drawString("ITEM", 290, 290);
			g.drawString("RUN", 400, 290);
			if ((BattleEngine.getInstance().currentSelectionMainX == 0)
					&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 274, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 0)
					&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 274, 270, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 1)
					&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 384, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 1)
					&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 384, 270, null);
			}
		}
		if (BattleEngine.getInstance().inFight) {

			g.drawImage(SpriteLibrary.getImage("BattleFight"), 0, 0, null);
			g.drawString(playerPokemon.getMove(0).name, 200, 260);
			if (playerPokemon.getNumMoves() > 1) {
				g.drawString(playerPokemon.getMove(1).name, 345, 260);
			}
			if (playerPokemon.getNumMoves() > 2) {
				g.drawString(playerPokemon.getMove(2).name, 200, 290);
			}
			if (playerPokemon.getNumMoves() > 3) {
				g.drawString(playerPokemon.getMove(3).name, 345, 290);
			}

			if ((BattleEngine.getInstance().currentSelectionFightX == 0)
					&& (BattleEngine.getInstance().currentSelectionFightY == 0)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 184, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 0)
					&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 184, 270, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 1)
					&& (BattleEngine.getInstance().currentSelectionFightY == 0)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 329, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 1)
					&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
				g.drawImage(SpriteLibrary.getImage(ARROW), 329, 270, null);
			}
		}

		// player status effect information
		if (playerPokemon.statusEffect == 1) {
			g.drawImage(SpriteLibrary.getImage("StatusPAR"), 415, 140, null);
		} else if (playerPokemon.statusEffect == 2) {
			g.drawImage(SpriteLibrary.getImage("StatusBRN"), 415, 140, null);
		} else if (playerPokemon.statusEffect == 3) {
			g.drawImage(SpriteLibrary.getImage("StatusPSN"), 415, 140, null);
		} else if (playerPokemon.statusEffect == 4) {
			g.drawImage(SpriteLibrary.getImage("StatusSLP"), 415, 140, null);
		} else if (playerPokemon.statusEffect == 5) {
			g.drawImage(SpriteLibrary.getImage("StatusFRZ"), 415, 140, null);
		}

		// enemy status effect information
		if (enemyPokemon.statusEffect == 1) {
			g.drawImage(SpriteLibrary.getImage("StatusPAR"), 18, 60, null);
		} else if (enemyPokemon.statusEffect == 2) {
			g.drawImage(SpriteLibrary.getImage("StatusBRN"), 18, 60, null);
		} else if (enemyPokemon.statusEffect == 3) {
			g.drawImage(SpriteLibrary.getImage("StatusPSN"), 18, 60, null);
		} else if (enemyPokemon.statusEffect == 4) {
			g.drawImage(SpriteLibrary.getImage("StatusSLP"), 18, 60, null);
		} else if (enemyPokemon.statusEffect == 5) {
			g.drawImage(SpriteLibrary.getImage("StatusFRZ"), 18, 60, null);
		}
		// player pokemon information
		g.drawString(playerPokemon.getName(), 300, 175);
		g.drawString(String.valueOf(playerPokemon.getLevel()), 435, 175);
		g.drawString(String.valueOf(playerPokemon.getStat(STATS.HP)), 361, 207);
		g.drawString(String.valueOf(playerPokemon.getMaxStat(STATS.HP)), 403, 207);
		// enemy pokemon information
		g.drawString(enemyPokemon.getName(), 20, 25);
		g.drawString(String.valueOf(enemyPokemon.getLevel()), 145, 25);
		g.drawString(String.valueOf(enemyPokemon.getStat(STATS.HP)), 70, 45);
		g.drawString(String.valueOf(enemyPokemon.getMaxStat(STATS.HP)), 112, 45);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the intro screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintIntroScreen(Graphics g, GamePanel game) {
		g.drawImage(SpriteLibrary.getImage("Beginning"), 0, 0, null);
		g.drawImage(SpriteLibrary.getSpriteForDir("PROFESSOROAK_LARGE", DIR.SOUTH).getImage(), 150, 20, null);
		if (NPCLibrary.getInstance().get("Professor Oak").getText().size() > game.game.getIntroStage()) {
			paintMessageBox(g,
					NPCLibrary.getInstance().get("Professor Oak").getText().get(game.game.getIntroStage() - 1),
					NPCLibrary.getInstance().get("Professor Oak").getText().get(game.game.getIntroStage()));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the options screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintOptionScreen(Graphics g, GamePanel game) {
		String imageName = (game.gData.option_sound) ? "OptionBG_SoundOn" : "OptionBG_SoundOn";
		g.drawImage(SpriteLibrary.getImage(imageName), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 22, 85 + 32 * game.menuScreen.MENU_currentSelectionOption, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the save menu
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintSaveMenu(Graphics g, GamePanel game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("Save"), 0, 0, null);
		g.drawString(game.game.getPlayer().getName(), 100, 68);
		g.drawString(((Integer) game.game.getPlayer().getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(game.game.formatTime(), 76, 166);
		if (game.menuScreen.MENU_currentSelectionSave == 0) {
			g.drawImage(SpriteLibrary.getImage(ARROW), 394, 148, null);
		} else if (game.menuScreen.MENU_currentSelectionSave == 1) {
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
	// ////////////////////////////////////////////////////////////////////////
	private static void paintTrainerCard(Graphics g, GamePanel game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("TrainerCard"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Male"), 320, 100, null);

		Player player = game.game.getPlayer();
		g.drawString("ID:  " + player.getID(), 295, 54);
		g.drawString("Name:" + getPadding("Name:") + player.getName(), 64, 93);
		g.drawString("Money:" + getPadding("Money:") + "$" + player.getMoney(), 64, 150);
		g.drawString("Pokedex:" + getPadding("Pokedex:") + player.getNumPokemonOwned(), 64, 183);
		g.drawString("Time:  " + getPadding("Time:") + game.game.formatTime(), 64, 213);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokegear screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPokegearScreen(Graphics g, MenuScene menu) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("PokegearBG"), 0, 0, null);
		if (menu.MENU_currentSelectionPokeGear == 0) {
			g.drawImage(SpriteLibrary.getImage("PokegearMap"), 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 1) {
			g.drawImage(SpriteLibrary.getImage("PokegearRadio"), 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 2) {
			g.drawImage(SpriteLibrary.getImage("PokegearPhone"), 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 3) {
			g.drawImage(SpriteLibrary.getImage("PokegearExit"), 0, 0, null);
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
	private static void paintPartyScreen(Graphics g, GamePanel game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage(partyBackground), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(partyFirstMember), 40, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 70, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 120, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 170, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 220, null);
		if (game.game.getPlayer().getPokemon().size() == 2) {
			g.drawImage(SpriteLibrary.getImage(partyMember), 190, 20, null);
		}
		PokemonList playerPokemon = game.game.getPlayer().getPokemon();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon().getImage(), 75, 40, null);
			g.drawString((playerPokemon.get(0)).getName(), 65, 130);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a conversation on the screen as a series of message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintConversation(Graphics g, GamePanel game) {
		game.messageString = game.menuScreen.MENU_NPC.getText(game.menuScreen.MENU_stage);
		paintMessageBox(g, game);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokedex screen
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
	private static void paintPauseMenu(Graphics g, MenuScene menu) {
		g.drawImage(SpriteLibrary.getImage("Menu"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 335, 20 + 32 * menu.MENU_currentSelectionMain, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint the overworld -
	// include all NPCs, obstacles, map, interactive objects, and player
	//
	// TODO - update to paint only visible areas + 1 row + 1 column
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintWorld(Graphics g, GameController game, Graphics2D g2, AffineTransform at) {
		g.setColor(Color.BLACK);
		g2.setClip(new Rectangle(-16, -42, 704, 438));

		int offsetX = game.getOffsetX();
		int offsetY = game.getOffsetY();
		Player player = game.getPlayer();
		int map_height = game.getMapHeight();
		int map_width = game.getMapWidth();

		int startX = game.getStartX();
		int startY = game.getStartY();

		g2.translate(offsetX - 32, offsetY - 64);

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
			game.setMapTileAt(curNPC.tData.position, TileSet.OBSTACLE);
		}
		g2.translate(-offsetX, -offsetY);

		g2.setTransform(at);
		System.out.println(player);
		System.out.println(player.tData);
		System.out.println(player.tData.sprite);
		g.drawImage(player.tData.sprite.getImage(), 224, 118, null);
		g.setColor(Color.WHITE);
		g.drawString(player.getCurrentX() + "," + player.getCurrentY(), 10, 25);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the continue screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintContinueScreen(Graphics g, GameData gameData) {
		g.drawImage(SpriteLibrary.getImage("Continue"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(ARROW), 13, 20 + 32 * gameData.menuSelection, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the title screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintTitle(Graphics g, GameController game) {
		g.drawImage(SpriteLibrary.getImage("Title"), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage("Start"), 0, 260, null);
	}
}
