package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import driver.Game;
import driver.GameData;
import pokedex.Pokemon;
import pokedex.Pokemon.STATS;
import pokedex.PokemonList;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.NPCLibrary;
import trainers.Player;
import utilities.BattleEngine;
import utilities.Utils;

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

	// ////////////////////////////////////////////////////////////////////////
	//
	// paintComponent - main painting logic from which submethods are called
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintComponent(Graphics g, Game game) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2.setTransform(at);
		if (game.gData.atTitle) {
			paintTitle(g);
		} else if (game.gData.atContinueScreen) {
			paintContinueScreen(g, game.gData);
		} else if (game.gData.inIntro) {
			paintIntroScreen(g, game.gData);
		} else if (game.gData.inNameScreen) {
			paintNameInputScreen(g, game);
		} else if (game.menuScreen.MENU_inPokeDex) {
			paintPokedexScreen(g);
		} else if (game.menuScreen.MENU_inPokemon) {
			paintPartyScreen(g, game.gData.player);
		} else if (game.menuScreen.MENU_inBag) {
			paintBagScreen(g);
		} else if (game.menuScreen.MENU_inPokeGear) {
			paintPokegearScreen(g, game.menuScreen);
		} else if (game.menuScreen.MENU_inTrainerCard) {
			paintTrainerCard(g, game.gData);
		} else if (game.menuScreen.MENU_inOption) {
			paintOptionScreen(g, game);
		} else if (game.gData.inBattle) {
			paintBattle(g, game);
		} else if (!game.gData.inBattle) {
			paintWorld(g, game.gData, g2, at);
		}
		if (game.menuScreen.MENU_inMain) {
			paintPauseMenu(g, game.menuScreen);
		}
		if (game.menuScreen.MENU_inConversation) {
			paintConversation(g, game.menuScreen);
		}
		if (game.menuScreen.MENU_inSave) {
			paintSaveMenu(g, game);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the name input screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintNameInputScreen(Graphics g, Game game) {
		g.drawImage(SpriteLibrary.NAMESCREEN, 0, 0, null);

		if (game.nameScreen.rowSelection < 5) {
			g.drawImage(SpriteLibrary.ARROW, (int) (40 + Tile.TILESIZE * 2 * game.nameScreen.colSelection),
					100 + Tile.TILESIZE * game.nameScreen.rowSelection, null);
		}
		if (game.nameScreen.rowSelection == 5) {
			g.drawImage(SpriteLibrary.ARROW, (int) (100 + Tile.TILESIZE * 6 * game.nameScreen.colSelection),
					100 + Tile.TILESIZE * game.nameScreen.rowSelection, null);
		}

		String name = game.nameScreen.getChosenName();

		for (int x = 0; x < GameData.MAX_NAME_SIZE; x++) {
			g.drawImage(SpriteLibrary.FONT_UNDERSCORE, 150 + Tile.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			paintString(g, name, 150, 40);
		}
		if (name.length() < GameData.MAX_NAME_SIZE) {
			g.drawImage(SpriteLibrary.FONT_CURSOR, 150 + Tile.TILESIZE * name.length(), 40, null);
		}
		g.drawImage(SpriteLibrary.getInstance().getSprites(game.nameScreen.toBeNamed).get(0), 80, 30, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a given string to a given location of the screen
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void paintString(Graphics g, String string, int startX, int startY) {
		for (int x = 0; x < string.toCharArray().length; x++) {
			g.drawImage(SpriteLibrary.getInstance().getFontChar(string.toCharArray()[x]), startX + Tile.TILESIZE * x,
					startY, null);
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
	private static void paintBattle(Graphics g, Game game) {
		g.drawImage(SpriteLibrary.BG, 0, 0, null);

		Pokemon playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		Pokemon enemyPokemon = BattleEngine.getInstance().enemyPokemon.get(0);

		g.drawImage(playerPokemon.getBackSprite(), 120 - (playerPokemon.getBackSprite().getHeight(game)) / 2,
				228 - playerPokemon.getBackSprite().getHeight(game), null);
		g.drawImage(enemyPokemon.getFrontSprite(), 310, 25, null);

		if (BattleEngine.getInstance().inMain) {
			g.drawImage(SpriteLibrary.BATTLE_BG, 0, 0, null);
			g.drawString("Wild " + enemyPokemon.getName() + " Appeared!", 30, 260);
			g.drawString("FIGHT", 290, 260);
			g.drawString("PKMN", 400, 260);
			g.drawString("ITEM", 290, 290);
			g.drawString("RUN", 400, 290);
			if ((BattleEngine.getInstance().currentSelectionMainX == 0)
					&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
				g.drawImage(SpriteLibrary.ARROW, 274, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 0)
					&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
				g.drawImage(SpriteLibrary.ARROW, 274, 270, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 1)
					&& (BattleEngine.getInstance().currentSelectionMainY == 0)) {
				g.drawImage(SpriteLibrary.ARROW, 384, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionMainX == 1)
					&& (BattleEngine.getInstance().currentSelectionMainY == 1)) {
				g.drawImage(SpriteLibrary.ARROW, 384, 270, null);
			}
		}
		if (BattleEngine.getInstance().inFight) {

			g.drawImage(SpriteLibrary.BATTLE_FIGHTBG, 0, 0, null);
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
				g.drawImage(SpriteLibrary.ARROW, 184, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 0)
					&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
				g.drawImage(SpriteLibrary.ARROW, 184, 270, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 1)
					&& (BattleEngine.getInstance().currentSelectionFightY == 0)) {
				g.drawImage(SpriteLibrary.ARROW, 329, 240, null);
			} else if ((BattleEngine.getInstance().currentSelectionFightX == 1)
					&& (BattleEngine.getInstance().currentSelectionFightY == 1)) {
				g.drawImage(SpriteLibrary.ARROW, 329, 270, null);
			}
		}

		// player status effect information
		if (playerPokemon.statusEffect == 1) {
			g.drawImage(SpriteLibrary.STATUS_PAR, 415, 140, null);
		} else if (playerPokemon.statusEffect == 2) {
			g.drawImage(SpriteLibrary.STATUS_BRN, 415, 140, null);
		} else if (playerPokemon.statusEffect == 3) {
			g.drawImage(SpriteLibrary.STATUS_PSN, 415, 140, null);
		} else if (playerPokemon.statusEffect == 4) {
			g.drawImage(SpriteLibrary.STATUS_SLP, 415, 140, null);
		} else if (playerPokemon.statusEffect == 5) {
			g.drawImage(SpriteLibrary.STATUS_FRZ, 415, 140, null);
		}

		// enemy status effect information
		if (enemyPokemon.statusEffect == 1) {
			g.drawImage(SpriteLibrary.STATUS_PAR, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 2) {
			g.drawImage(SpriteLibrary.STATUS_BRN, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 3) {
			g.drawImage(SpriteLibrary.STATUS_PSN, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 4) {
			g.drawImage(SpriteLibrary.STATUS_SLP, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 5) {
			g.drawImage(SpriteLibrary.STATUS_FRZ, 18, 60, null);
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
	private static void paintIntroScreen(Graphics g, GameData gameData) {
		g.drawImage(SpriteLibrary.BEGINNING, 0, 0, null);
		g.drawImage(SpriteLibrary.getInstance().getSprites("PROFESSOROAK_LARGE").get(0), 150, 20, null);
		if (NPCLibrary.getInstance().get("Professor Oak").getText().size() > gameData.introStage) {
			Utils.messageBox(g, NPCLibrary.getInstance().get("Professor Oak").getText().get(gameData.introStage - 1),
					NPCLibrary.getInstance().get("Professor Oak").getText().get(gameData.introStage));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the options screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintOptionScreen(Graphics g, Game game) {
		Image optionBG = (game.gData.option_sound) ? SpriteLibrary.OPTION_SOUND_ON : SpriteLibrary.OPTION_SOUND_OFF;

		g.drawImage(optionBG, 0, 0, null);
		g.drawImage(SpriteLibrary.ARROW, 22, 85 + 32 * game.menuScreen.MENU_currentSelectionOption, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the save menu
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintSaveMenu(Graphics g, Game game) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.SAVE, 0, 0, null);
		g.drawString(game.gData.player.getName(), 100, 68);
		g.drawString(((Integer) game.gData.player.getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(game.gData.gameTimeStruct.formatTime(), 76, 166);
		if (game.menuScreen.MENU_currentSelectionSave == 0) {
			g.drawImage(SpriteLibrary.ARROW, 394, 148, null);
		} else if (game.menuScreen.MENU_currentSelectionSave == 1) {
			g.drawImage(SpriteLibrary.ARROW, 394, 180, null);
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
	private static void paintTrainerCard(Graphics g, GameData gameData) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.TRAINERCARD, 0, 0, null);
		g.drawImage(SpriteLibrary.TRAINER_FOR_CARD, 320, 100, null);

		g.drawString("ID:  " + gameData.player.getID(), 295, 54);
		g.drawString("Name:" + getPadding("Name:") + gameData.player.getName(), 64, 93);
		g.drawString("Money:" + getPadding("Money:") + "$" + gameData.player.getMoney(), 64, 150);
		g.drawString("Pokedex:" + getPadding("Pokedex:") + gameData.player.getPokemonOwned(), 64, 183);
		g.drawString("Time:  " + getPadding("Time:") + gameData.gameTimeStruct.formatTime(), 64, 213);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokegear screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPokegearScreen(Graphics g, MenuScene menu) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.POKEGEAR, 0, 0, null);
		if (menu.MENU_currentSelectionPokeGear == 0) {
			g.drawImage(SpriteLibrary.POKEGEAR_MAP, 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 1) {
			g.drawImage(SpriteLibrary.POKEGEAR_RADIO, 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 2) {
			g.drawImage(SpriteLibrary.POKEGEAR_PHONE, 0, 0, null);
		} else if (menu.MENU_currentSelectionPokeGear == 3) {
			g.drawImage(SpriteLibrary.POKEGEAR_EXIT, 0, 0, null);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the bag screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintBagScreen(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.BAGSCREEN, 0, 0, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the party screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPartyScreen(Graphics g, Player p) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.POKESEL, 0, 0, null);
		g.drawImage(SpriteLibrary.PARTYFIRST, 40, 20, null);
		g.drawImage(SpriteLibrary.PARTYBOX, 190, 20, null);
		g.drawImage(SpriteLibrary.PARTYBOX, 190, 70, null);
		g.drawImage(SpriteLibrary.PARTYBOX, 190, 120, null);
		g.drawImage(SpriteLibrary.PARTYBOX, 190, 170, null);
		g.drawImage(SpriteLibrary.PARTYBOX, 190, 220, null);
		if (p.getPokemon().size() == 2) {
			g.drawImage(SpriteLibrary.PARTYBOX, 190, 20, null);
		}
		PokemonList playerPokemon = p.getPokemon();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon(), 75, 40, null);
			g.drawString((playerPokemon.get(0)).getName(), 65, 130);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint a conversation on the screen as a series of message boxes
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintConversation(Graphics g, MenuScene menu) {
		Utils.messageBox(g, menu.MENU_NPC.getText(menu.MENU_stage));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pokedex screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPokedexScreen(Graphics g) {
		g.drawImage(SpriteLibrary.POKEDEX, 0, 0, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the pause menu
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintPauseMenu(Graphics g, MenuScene menu) {
		g.drawImage(SpriteLibrary.MAIN_MENU, 0, 0, null);
		g.drawImage(SpriteLibrary.ARROW, 335, 20 + 32 * menu.MENU_currentSelectionMain, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint the overworld -
	// include all NPCs, obstacles, map, interactive objects, and player
	//
	// TODO - update to paint only visible areas + 1 row + 1 column
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintWorld(Graphics g, GameData gameData, Graphics2D g2, AffineTransform at) {
		g.setColor(Color.BLACK);
		g2.setClip(new Rectangle(-16, -42, 704, 438));

		g2.translate(gameData.offsetX - 32, gameData.offsetY - 64);

		for (int layer = 1; layer < 3; layer++) {
			int x_coor = gameData.start_coorX;
			int y_coor = gameData.start_coorY;

			int tile_number = 0;
			for (int y = 1; y <= gameData.map_height; y++) {
				for (int x = 1; x <= gameData.map_width; x++) {
					int tilePicNum = gameData.currentMap[layer][tile_number];

					if (!(layer == 2 && tilePicNum == 0)) {
						g.drawImage((Image) TileSet.getInstance().get(tilePicNum), x_coor, y_coor, null);
					}
					x_coor += Tile.TILESIZE;
					tile_number++;
				}
				x_coor = gameData.start_coorX;
				y_coor += Tile.TILESIZE;
			}
		}

		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			g.drawImage(curNPC.getSprite(), curNPC.getCurrentX() * 32 + gameData.start_coorX,
					curNPC.getCurrentY() * 32 + gameData.start_coorY - 10, null);
			gameData.tm.set(curNPC.tData.position, TileSet.OBSTACLE);
		}
		g2.translate(-gameData.offsetX, -gameData.offsetY);

		g2.setTransform(at);
		g.drawImage(gameData.player.getSprite(), 224, 118, null);
		g.setColor(Color.WHITE);
		g.drawString(gameData.player.getCurrentX() + "," + gameData.player.getCurrentY(), 10, 25);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the continue screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintContinueScreen(Graphics g, GameData gameData) {
		g.drawImage(SpriteLibrary.CONTINUESCREEN, 0, 0, null);
		g.drawImage(SpriteLibrary.ARROW, 13, 20 + 32 * gameData.menuSelection, null);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Paint all components of the title screen
	//
	// ////////////////////////////////////////////////////////////////////////
	private static void paintTitle(Graphics g) {
		g.drawImage(SpriteLibrary.TITLESCREEN, 0, 0, null);
		g.drawImage(SpriteLibrary.START_SYMBOL, 0, 260, null);
	}
}
