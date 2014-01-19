package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import pokedex.Pokemon;
import pokedex.PokemonList;
import trainers.NPC;
import trainers.Player;
import utilities.EnumsAndConstants;
import utilities.GameData;
import utilities.Utils;
import driver.Main;

public class Painter extends JPanel {

	private static final long serialVersionUID = 1L;

	public void paintComponent(Graphics g, Main game) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2.setTransform(at);
		if (game.gData.atTitle) {
			paintTitle(g);
		} else if (game.gData.atContinueScreen) {
			paintContinueScreen(g, game.gData);
		} else if (game.gData.inIntro) {
			paintIntroScreen(g, game.introScreen);
		} else if (game.gData.inNameScreen) {
			paintNameInputScreen(g, game);
		} else if (game.menuScreen.inPokeDex) {
			paintPokedexScreen(g);
		} else if (game.menuScreen.inPokemon) {
			paintPartyScreen(g, game.gData.player);
		} else if (game.menuScreen.inBag) {
			paintBagScreen(g);
		} else if (game.menuScreen.inPokeGear) {
			paintPokegearScreen(g, game.menuScreen);
		} else if (game.menuScreen.inTrainerCard) {
			paintTrainerCard(g, game.gData);
		} else if (game.menuScreen.inOption) {
			paintOptionScreen(g, game.menuScreen);
		} else if (game.gData.inBattle) {
			paintBattle(g, game);
		} else if (!game.gData.inBattle) {
			paintWorld(g, game.gData, g2, at);
		}
		if (game.menuScreen.inMain) {
			paintPauseMenu(g, game.menuScreen);
		}
		if (game.menuScreen.inConversation) {
			paintConversation(g, game.menuScreen);
		}
		if (game.menuScreen.inSave) {
			paintSaveMenu(g, game);
		}
	}

	private Image getNameSprite(String tbn) {
		return EnumsAndConstants.sprite_lib.getSprites(tbn).get(0);
	}

	private void paintNameInputScreen(Graphics g, Main game) {
		g.drawImage(EnumsAndConstants.sprite_lib.NAMESCREEN, 0, 0, null);

		if (game.nameScreen.rowSelection < 5) {
			g.drawImage(EnumsAndConstants.sprite_lib.ARROW, (int) (40 + EnumsAndConstants.TILESIZE * 2
					* game.nameScreen.colSelection), 100 + EnumsAndConstants.TILESIZE * game.nameScreen.rowSelection,
					null);
		}
		if (game.nameScreen.rowSelection == 5) {
			g.drawImage(EnumsAndConstants.sprite_lib.ARROW, (int) (100 + EnumsAndConstants.TILESIZE * 6
					* game.nameScreen.colSelection), 100 + EnumsAndConstants.TILESIZE * game.nameScreen.rowSelection,
					null);
		}

		String name = game.nameScreen.getNameSelected();

		for (int x = 0; x < EnumsAndConstants.MAX_NAME_SIZE; x++) {
			g.drawImage(EnumsAndConstants.sprite_lib.FONT_UNDERSCORE, 150 + EnumsAndConstants.TILESIZE * x, 40, null);
		}
		for (int x = 0; x < name.toCharArray().length; x++) {
			paintString(g, name, 150, 40);
		}
		if (name.length() < EnumsAndConstants.MAX_NAME_SIZE) {
			g.drawImage(EnumsAndConstants.sprite_lib.FONT_CURSOR, 150 + EnumsAndConstants.TILESIZE * name.length(), 40,
					null);
		}
		g.drawImage(getNameSprite(game.nameScreen.toBeNamed), 80, 30, null);
	}

	public void paintString(Graphics g, String string, int startX, int startY) {
		for (int x = 0; x < string.toCharArray().length; x++) {
			g.drawImage(EnumsAndConstants.sprite_lib.getFontChar(string.toCharArray()[x]), startX
					+ EnumsAndConstants.TILESIZE * x, startY, null);
		}
	}

	private void paintBattle(Graphics g, Main game) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.drawImage(game.encounter.BG, 0, 0, null);

		Pokemon playerPokemon = game.encounter.playerPokemon;
		Pokemon enemyPokemon = game.encounter.enemyPokemon.get(0);

		g.drawImage(playerPokemon.getBackSprite(), 120 - (playerPokemon.getBackSprite().getHeight(game)) / 2,
				228 - playerPokemon.getBackSprite().getHeight(game), null);
		g.drawImage(enemyPokemon.getFrontSprite(), 310, 25, null);

		if (game.encounter.inMain) {
			g.drawImage(game.encounter.battleMainBG, 0, 0, null);
			g.drawString("Wild " + enemyPokemon.getName() + " Appeared!", 30, 260);
			g.drawString("FIGHT", 290, 260);
			g.drawString("PKMN", 400, 260);
			g.drawString("ITEM", 290, 290);
			g.drawString("RUN", 400, 290);
			if ((game.encounter.currentSelectionMainX == 0) && (game.encounter.currentSelectionMainY == 0)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 274, 240, null);
			} else if ((game.encounter.currentSelectionMainX == 0) && (game.encounter.currentSelectionMainY == 1)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 274, 270, null);
			} else if ((game.encounter.currentSelectionMainX == 1) && (game.encounter.currentSelectionMainY == 0)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 384, 240, null);
			} else if ((game.encounter.currentSelectionMainX == 1) && (game.encounter.currentSelectionMainY == 1)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 384, 270, null);
			}
		}
		if (game.encounter.inFight) {

			g.drawImage(game.encounter.battleFightBG, 0, 0, null);
			g.drawString("Select a Move", 30, 260);
			g.drawString(playerPokemon.getMove(0).getName(), 200, 260);
			if (playerPokemon.getNumMoves() > 1) {
				g.drawString(playerPokemon.getMove(1).getName(), 345, 260);
			}
			if (playerPokemon.getNumMoves() > 2) {
				g.drawString(playerPokemon.getMove(2).getName(), 200, 290);
			}
			if (playerPokemon.getNumMoves() > 3) {
				g.drawString(playerPokemon.getMove(3).getName(), 345, 290);
			}

			if ((game.encounter.currentSelectionFightX == 0) && (game.encounter.currentSelectionFightY == 0)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 184, 240, null);
			} else if ((game.encounter.currentSelectionFightX == 0) && (game.encounter.currentSelectionFightY == 1)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 184, 270, null);
			} else if ((game.encounter.currentSelectionFightX == 1) && (game.encounter.currentSelectionFightY == 0)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 329, 240, null);
			} else if ((game.encounter.currentSelectionFightX == 1) && (game.encounter.currentSelectionFightY == 1)) {
				g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 329, 270, null);
			}
		}

		if (playerPokemon.statusEffect == 1) {
			g.drawImage(game.encounter.statusPAR, 415, 140, null);
		} else if (playerPokemon.statusEffect == 2) {
			g.drawImage(game.encounter.statusBRN, 415, 140, null);
		} else if (playerPokemon.statusEffect == 3) {
			g.drawImage(game.encounter.statusPSN, 415, 140, null);
		} else if (playerPokemon.statusEffect == 4) {
			g.drawImage(game.encounter.statusSLP, 415, 140, null);
		} else if (playerPokemon.statusEffect == 5) {
			g.drawImage(game.encounter.statusFRZ, 415, 140, null);
		}
		if (enemyPokemon.statusEffect == 1) {
			g.drawImage(game.encounter.statusPAR, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 2) {
			g.drawImage(game.encounter.statusBRN, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 3) {
			g.drawImage(game.encounter.statusPSN, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 4) {
			g.drawImage(game.encounter.statusSLP, 18, 60, null);
		} else if (enemyPokemon.statusEffect == 5) {
			g.drawImage(game.encounter.statusFRZ, 18, 60, null);
		}
		g.drawString(playerPokemon.getName(), 300, 175);
		g.drawString(((Integer) playerPokemon.getLevel()).toString(), 435, 175);
		g.drawString(((Integer) playerPokemon.getStat(EnumsAndConstants.STATS.HP)).toString(), 361, 207);
		g.drawString(((Integer) playerPokemon.getMaxStat(EnumsAndConstants.STATS.HP)).toString(), 403, 207);
		g.drawString(enemyPokemon.getName(), 20, 25);
		g.drawString(((Integer) enemyPokemon.getLevel()).toString(), 145, 25);
		g.drawString(((Integer) enemyPokemon.getStat(EnumsAndConstants.STATS.HP)).toString(), 70, 45);
		g.drawString(((Integer) enemyPokemon.getMaxStat(EnumsAndConstants.STATS.HP)).toString(), 112, 45);
	}

	private void paintIntroScreen(Graphics g, IntroScene intro) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.drawImage(EnumsAndConstants.sprite_lib.BEGINNING, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.getSprites("Professor Oak").get(0), 150, 20, null);
		if (EnumsAndConstants.npc_lib.getNPC("Professor Oak").getText().size() > intro.stage) {
			Utils.messageBox(g, EnumsAndConstants.npc_lib.getNPC("Professor Oak").getText().get(intro.stage - 1),
					EnumsAndConstants.npc_lib.getNPC("Professor Oak").getText().get(intro.stage));
		}
	}

	private void paintOptionScreen(Graphics g, MenuScene menu) {
		g.drawImage(EnumsAndConstants.sprite_lib.OPTION, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 22, 85 + 32 * menu.currentSelectionOption, null);
	}

	private void paintSaveMenu(Graphics g, Main game) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.SAVE, 0, 0, null);
		g.drawString(game.gData.player.getName(), 100, 68);
		g.drawString(((Integer) game.gData.player.getBadges()).toString(), 100, 101);
		g.drawString("1", 110, 134);
		g.drawString(Utils.formatTime(game.gData.gameTimeStruct), 76, 166);
		if (game.menuScreen.currentSelectionSave == 0) {
			g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 394, 148, null);
		} else if (game.menuScreen.currentSelectionSave == 1) {
			g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 394, 180, null);
		}
	}

	public String getPadding(String toBePadded) {
		int numSpaces = 12 - toBePadded.length();
		String retStr = "";
		for (int x = 0; x < numSpaces; x++) {
			retStr += " ";
		}
		return retStr;
	}

	private void paintTrainerCard(Graphics g, GameData gameData) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.TRAINERCARD, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.TRAINER_FOR_CARD, 320, 100, null);

		g.drawString("ID:  " + gameData.player.getID(), 295, 54);
		g.drawString("Name:" + getPadding("Name:") + gameData.player.getName(), 64, 93);
		g.drawString("Money:" + getPadding("Money:") + "$" + gameData.player.getMoney(), 64, 150);
		g.drawString("Pokedex:" + getPadding("Pokedex:") + gameData.player.getPokemonOwned(), 64, 183);
		g.drawString("Time:  " + getPadding("Time:") + Utils.formatTime(gameData.gameTimeStruct), 64, 213);
		// @TODO add badges printing
	}

	private void paintPokegearScreen(Graphics g, MenuScene menu) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.POKEGEAR, 0, 0, null);
		if (menu.currentSelectionPokeGear == 0) {
			g.drawImage(EnumsAndConstants.sprite_lib.POKEGEARMAP, 0, 0, null);
		} else if (menu.currentSelectionPokeGear == 1) {
			g.drawImage(EnumsAndConstants.sprite_lib.POKEGEARRADIO, 0, 0, null);
		} else if (menu.currentSelectionPokeGear == 2) {
			g.drawImage(EnumsAndConstants.sprite_lib.POKEGEARPHONE, 0, 0, null);
		} else if (menu.currentSelectionPokeGear == 3) {
			g.drawImage(EnumsAndConstants.sprite_lib.POKEGEAREXIT, 0, 0, null);
		}
	}

	private void paintBagScreen(Graphics g) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.BAGSCREEN, 0, 0, null);
	}

	private void paintPartyScreen(Graphics g, Player p) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.POKESEL, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYFIRST, 40, 20, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 20, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 70, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 120, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 170, null);
		g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 220, null);
		if (p.getPokemon().size() == 2) {
			g.drawImage(EnumsAndConstants.sprite_lib.PARTYBOX, 190, 20, null);
		}
		PokemonList playerPokemon = p.getPokemon();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon(), 75, 40, null);
			g.drawString((playerPokemon.get(0)).getName(), 65, 130);
		}
	}

	private void paintConversation(Graphics g, MenuScene menu) {
		Utils.messageBox(g, menu.conversation.getText(menu.stage));
	}

	private void paintPokedexScreen(Graphics g) {
		g.drawImage(EnumsAndConstants.sprite_lib.POKEDEX, 0, 0, null);
	}

	private void paintPauseMenu(Graphics g, MenuScene menu) {
		g.drawImage(EnumsAndConstants.sprite_lib.MAIN_MENU, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 335, 20 + 32 * menu.currentSelectionMain, null);
	}

	private void paintWorld(Graphics g, GameData gameData, Graphics2D g2, AffineTransform at) {
		g.setFont(EnumsAndConstants.POKEFONT);
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
						g.drawImage((Image) EnumsAndConstants.tileset.get(tilePicNum), x_coor, y_coor, null);
					}
					x_coor += EnumsAndConstants.TILESIZE;
					tile_number++;
				}
				x_coor = gameData.start_coorX;
				y_coor += EnumsAndConstants.TILESIZE;
			}
		}

		for (int i = 0; i < EnumsAndConstants.npc_lib.npcs.size(); i++) {
			NPC curNPC = EnumsAndConstants.npc_lib.npcs.get(i);
			g.drawImage(curNPC.getSprite(), curNPC.getCurrentX() * 32 + gameData.start_coorX, curNPC.getCurrentY() * 32
					+ gameData.start_coorY - 10, null);

			gameData.tm.set(curNPC.nData.position, EnumsAndConstants.OBSTACLE);
		}
		g2.translate(-gameData.offsetX, -gameData.offsetY);

		g2.setTransform(at);
		g.drawImage(gameData.player.getSprite(), 224, 118, null);
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.WHITE);
		g.drawString(gameData.player.getCurrentX() + "," + gameData.player.getCurrentY(), 10, 25);
	}

	private void paintContinueScreen(Graphics g, GameData gameData) {
		g.drawImage(EnumsAndConstants.sprite_lib.CONTINUESCREEN, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.ARROW, 13, 20 + 32 * gameData.menuSelection, null);
	}

	private void paintTitle(Graphics g) {
		g.drawImage(EnumsAndConstants.sprite_lib.TITLESCREEN, 0, 0, null);
		g.drawImage(EnumsAndConstants.sprite_lib.START_SYMBOL, 0, 260, null);
	}
}
