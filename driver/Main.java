package driver;

import graphics.BattleScene;
import graphics.IntroScene;
import graphics.MenuScene;
import graphics.NPC;
import graphics.NPCThread;
import graphics.NameScene;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import locations.Coordinate;
import pokedex.Pokemon;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.MUSIC;
import utilities.EnumsAndConstants.SPRITENAMES;
import utilities.Utils;
import data_structures.ObstacleTile;
import data_structures.Player;
import data_structures.Tile;
import data_structures.TimeStruct;

public class Main extends JPanel implements KeyListener, ActionListener {

	int test;

	private static final long serialVersionUID = 1L;

	private boolean noClip = false;
	private boolean noBattle = false;

	private Timer gameTimer;

	public int offsetX = 0;
	public int offsetY = 0;
	public int start_coorX, start_coorY;
	public int menuSelection = 0;
	public TimeStruct gameTimeStruct = new TimeStruct();
	private int movespritepixels = 0;

	public long timeStarted;
	public Player gold = new Player(0, 0, "Gold");

	boolean walking = false;
	boolean movable_up = true;
	boolean movable_down = true;
	boolean movable_left = true;
	boolean movable_right = true;
	private boolean rightFoot = false;
	boolean movable = true;
	public boolean atTitle = true;
	public boolean atContinueScreen = false;

	public int[][] currentMap = new int[3][16500];
	public Tile[][] tileMap = new Tile[200][200];
	public int map_width;

	public int map_height;

	public NPC[] currentMapNPC = EnumsAndConstants.npc_lib.NEWBARKTOWN_NPC;
	public static NPCThread NPCTHREAD;

	public boolean inBattle = false;
	public boolean playerWin = false;

	public BattleScene encounter;
	public MenuScene menuScreen;
	public IntroScene introScreen;
	public NameScene nameScreen;

	public boolean inMenu = false;
	public boolean inIntro = false;
	public boolean inNameScreen = false;

	String playerName;

	EventHandler eventHandler;

	public Main() {
		menuScreen = new MenuScene(this);
		nameScreen = new NameScene();
		introScreen = new IntroScene(this);

		eventHandler = new EventHandler(this);

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));
		addKeyListener(this);
		gameTimer = new Timer(20, this);
		gameTimer.start();
		NPCTHREAD = new NPCThread(gold);
		NPCTHREAD.start();
		playerName = gold.getName();
	}

	public void actionPerformed(ActionEvent e) {
		gameTimeStruct.updateTime(timeStarted);

		if (inBattle) {
			if (encounter.playerPokemon.getStat(EnumsAndConstants.STATS.HP) <= 0) {
				handleWhiteOut();
			}
			if (encounter.enemyPokemon.get(0).getStat(EnumsAndConstants.STATS.HP) <= 0) {
				handleBattleWon();
			}
			if (!encounter.playerTurn) {
				encounter.enemyTurn();
			}
		} else {
			handleMovement();
		}
		repaint();
		validate();
	}

	private void handleMovement() {
		int playerCurX = gold.getCurrentX();
		int playerCurY = gold.getCurrentY();
		EnumsAndConstants.DIR playerDir = gold.getDir();
		Pokemon playerPokemon1 = gold.getPokemon().size() > 0 ? (Pokemon) gold.getPokemon().get(0) : null;

		if (!noClip) { // if noClip default true - else check for collisions
			movable_up = ((playerCurY == 0)) ? false : true;
			movable_left = ((playerCurX == 0)) ? false : true;
			movable_right = ((playerCurX == map_width - 1)) ? false : true;
			movable_down = ((playerCurY == map_height - 1)) ? false : true;

			if ((playerCurY > 0) && (playerCurX >= 0)) {
				movable_up = (tileMap[(playerCurY - 1)][playerCurX] instanceof ObstacleTile) ? false : true;
			}
			if ((playerCurY >= 0) && (playerCurX > 0)) {
				movable_left = (tileMap[playerCurY][(playerCurX - 1)] instanceof ObstacleTile) ? false : true;
			}
			if ((playerCurY >= 0) && (playerCurX >= 0)) {
				movable_down = (tileMap[(playerCurY + 1)][playerCurX] instanceof ObstacleTile) ? false : true;
				movable_right = (tileMap[playerCurY][(playerCurX + 1)] instanceof ObstacleTile) ? false : true;
			}
		}

		if (walking) { // take care of walking animation graphics logic
			movespritepixels += 1;
			offsetY = ((playerDir == EnumsAndConstants.DIR.NORTH) && (movable_up)) ? offsetY + 2 : offsetY;
			offsetY = ((playerDir == EnumsAndConstants.DIR.SOUTH) && (movable_down)) ? offsetY - 2 : offsetY;
			offsetX = ((playerDir == EnumsAndConstants.DIR.WEST) && (movable_left)) ? offsetX + 2 : offsetX;
			offsetX = ((playerDir == EnumsAndConstants.DIR.EAST) && (movable_right)) ? offsetX - 2 : offsetX;
		}

		Coordinate curPos = new Coordinate(playerCurX, playerCurY);
		Map<Coordinate, Coordinate> dict = EnumsAndConstants.TELEPORTS.getListofTeleports();
		Set<Coordinate> k = dict.keySet();

		boolean teleported = false;
		for (Coordinate x : k) {
			if (x.equals(curPos)) {
				gold.setCurrentX(dict.get(x).getX());
				gold.setCurrentY(dict.get(x).getY());

				start_coorX = (gold.getCurrentX() - x.getX()) * -1 * EnumsAndConstants.TILESIZE;
				start_coorY = (gold.getCurrentY() - x.getY()) * -1 * EnumsAndConstants.TILESIZE;
				teleported = true;
				gold.setDir(EnumsAndConstants.DIR.NORTH);
			}
		}
		if (movespritepixels >= 16 && !teleported) {
			movespritepixels = 0;
			walking = false;
			if ((playerDir == EnumsAndConstants.DIR.NORTH) && (movable_up)) {
				gold.changeLoc(1, 1);
			}
			if ((playerDir == EnumsAndConstants.DIR.SOUTH) && (movable_down)) {
				gold.changeLoc(0, 1);
			}
			if ((playerDir == EnumsAndConstants.DIR.WEST) && (movable_left)) {
				gold.changeLoc(1, 0);
			}
			if ((playerDir == EnumsAndConstants.DIR.EAST) && (movable_right)) {
				gold.changeLoc(0, 0);
			}
			rightFoot = (!rightFoot);
			if ((playerPokemon1.statusEffect == 2) || (playerPokemon1.statusEffect == 3)) {
				playerPokemon1.doDamageWithoutSound(1);
			}
		}
		if (((playerDir == EnumsAndConstants.DIR.NORTH) && (movable_up))
				|| ((playerDir == EnumsAndConstants.DIR.SOUTH) && (movable_down))
				|| ((playerDir == EnumsAndConstants.DIR.WEST) && (movable_left))
				|| ((playerDir == EnumsAndConstants.DIR.EAST) && (movable_right))) {
			gold.changeSprite(movespritepixels, rightFoot);
		}
		for (int i = 0; i < currentMapNPC.length; i++) {
			NPC curNPC = currentMapNPC[i];
			boolean doBattle = !noBattle;
			if (curNPC.trainer) {
				if ((curNPC.getCurrentX() < map_height) && (curNPC.getCurrentY() < map_width) && (!inBattle)) {
					checkForTrainerEncounter(curNPC, doBattle);
				} else {
					movable = true;
				}
			}
			if (playerWin) {
				Utils.pauseBackgrondMusic();
				Utils.playBackgroundMusic(gold.getCurLoc());
				gold.beatenTrainers.add(encounter.enemy.getName());
				playerWin = false;
				movable = true;
			}
		}
	}

	private void checkForTrainerEncounter(NPC curNPC, boolean doBattle) {
		boolean NPC_SEES_PLAYER = playerInRange(curNPC);
		if (NPC_SEES_PLAYER) {
			for (int x = 0; x < gold.beatenTrainers.size(); x++) {
				if (curNPC.getName().equals(gold.beatenTrainers.get(x))) {
					doBattle = false;
				}
			}
			if (doBattle && !inBattle && !inMenu) {
				NPCTHREAD.stop = true;

				enemyTrainerAnimation(curNPC);
				doTrainerBattle(curNPC);
			}
		}
	}

	private boolean playerInRange(NPC curNPC) {
		int playerCurY = gold.getCurrentY();
		int playerCurX = gold.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		EnumsAndConstants.DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y) && (NPC_Y - playerCurY <= 5) && (NPC_DIR == EnumsAndConstants.DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= 5) && (NPC_DIR == EnumsAndConstants.DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= 5) && (NPC_DIR == EnumsAndConstants.DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= 5) && (NPC_DIR == EnumsAndConstants.DIR.EAST)))));
	}

	private void enemyTrainerAnimation(NPC curNPC) {
		Utils.pickTrainerMusic();
		int NPC_Y = curNPC.getCurrentY();
		int NPC_X = curNPC.getCurrentX();
		EnumsAndConstants.DIR NPC_DIR = curNPC.getDirection();

		tileMap[NPC_Y][NPC_X] = EnumsAndConstants.TILE;
		if (NPC_DIR == EnumsAndConstants.DIR.NORTH) {
			while (curNPC.getCurrentY() > gold.getCurrentY() + 1) {
				tileMap[curNPC.getCurrentY()][curNPC.getCurrentX()] = EnumsAndConstants.TILE;
				curNPC.moveUp();
				paintComponent(getGraphics());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException localInterruptedException) {}
			}
		}
		if (NPC_DIR == EnumsAndConstants.DIR.SOUTH) {
			while (curNPC.getCurrentY() < gold.getCurrentY() - 1) {
				tileMap[curNPC.getCurrentY()][curNPC.getCurrentX()] = EnumsAndConstants.TILE;
				curNPC.moveDown();
				paintComponent(getGraphics());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException localInterruptedException1) {}
			}
		}
		if (NPC_DIR == EnumsAndConstants.DIR.EAST) {
			while (curNPC.getCurrentX() < gold.getCurrentX() - 1) {
				tileMap[curNPC.getCurrentY()][curNPC.getCurrentX()] = EnumsAndConstants.TILE;
				curNPC.moveRight();
				paintComponent(getGraphics());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException localInterruptedException2) {}
			}
		}
		if (NPC_DIR == EnumsAndConstants.DIR.WEST) {
			while (curNPC.getCurrentX() > gold.getCurrentX() + 1) {
				tileMap[curNPC.getCurrentY()][curNPC.getCurrentX()] = EnumsAndConstants.TILE;
				curNPC.moveLeft();
				paintComponent(getGraphics());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException localInterruptedException3) {}
			}
		}
	}

	private void handleBattleWon() {
		encounter.playerWon = true;
		System.out.println("Wild Pokemon has fainted");
		encounter.Win();
		if (encounter.enemy.getName() != null) {
			gold.beatenTrainers.add(encounter.enemy.getName());
		}
	}

	private void handleWhiteOut() {
		System.out.println("Player Pokemon has fainted");
		System.out.println(playerName + " is all out of usable Pokemon!");
		System.out.println(playerName + " whited out.");
		encounter.whiteOut();
		gold.setSprite(EnumsAndConstants.sprite_lib.PLAYER_UP);
		gold.getPokemon().get(0).heal(-1);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		EnumsAndConstants.PAINTER.paintComponent(g, this);
		repaint();
		validate();
	}

	public void doTrainerBattle(NPC curNPC) {
		movable = false;
		Utils.playBackgroundMusic(MUSIC.TRAINER_BATTLE);
		encounter = new BattleScene(this, curNPC);
		inBattle = true;
		encounter.Start();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (atTitle) {
			if (keyCode == KeyEvent.VK_ENTER) {
				atTitle = false;
				Utils.playBackgroundMusic(MUSIC.CONTINUE);
				atContinueScreen = true;
			}
		} else if (inIntro) {
			if (keyCode == KeyEvent.VK_X) {
				nameScreen.removeChar();
			}
			if (keyCode == KeyEvent.VK_Z) {
				introScreen.stage += 2;
				if (introScreen.stage > EnumsAndConstants.npc_lib.PROFESSOROAK.getTextLength() - 1) {
					Utils.playBackgroundMusic(MUSIC.NEWBARKTOWN);
					inIntro = !inIntro;
				}
				if (introScreen.stage == 15) {
					inNameScreen = true;
					nameScreen.setToBeNamed(SPRITENAMES.PLAYER);
					inIntro = false;
				}
			}
		} else if (inNameScreen) {
			if (keyCode == KeyEvent.VK_X) {
				nameScreen.removeChar();
			}
			if ((keyCode == KeyEvent.VK_Z)) {
				if (nameScreen.rowSelection == 5) {
					if (nameScreen.colSelection == 1 && nameScreen.getNameSelected().length() > 0) {
						gold.setName(nameScreen.getNameSelected());
						nameScreen.reset();
						inNameScreen = false;
						inIntro = true;
					}
					if (nameScreen.colSelection == 0) {
						nameScreen.removeChar();
					}
				} else {
					nameScreen.addSelectedChar();
				}
			}
			if (keyCode == KeyEvent.VK_DOWN && nameScreen.rowSelection < 5) {
				nameScreen.rowSelection++;
				if (nameScreen.rowSelection == 5 && nameScreen.colSelection < 3) {
					nameScreen.colSelection = 0;
				}
				if (nameScreen.rowSelection == 5 && nameScreen.colSelection >= 3) {
					nameScreen.colSelection = 1;
				}
			} else if (keyCode == KeyEvent.VK_UP && nameScreen.rowSelection > 0) {
				nameScreen.rowSelection--;
			} else if (keyCode == KeyEvent.VK_LEFT && nameScreen.colSelection > 0) {
				nameScreen.colSelection--;
			} else if (keyCode == KeyEvent.VK_RIGHT && nameScreen.rowSelection == 5 && nameScreen.colSelection < 1) {
				nameScreen.colSelection++;
			} else if (keyCode == KeyEvent.VK_RIGHT && nameScreen.rowSelection < 5 && nameScreen.colSelection < 5) {
				nameScreen.colSelection++;
			}
		} else if ((atContinueScreen) && (!atTitle)) {
			if (keyCode == KeyEvent.VK_UP) {
				Utils.playSelectSound();
				if (menuSelection > 0) {
					menuSelection -= 1;
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				Utils.playSelectSound();
				if (menuSelection < 2) {
					menuSelection += 1;
				}
			}
			if (keyCode == KeyEvent.VK_Z) {
				Utils.playSelectSound();
				if (menuSelection == 0) {
					GameInitializer.startgame(true, this);
				} else if (menuSelection == 1) {
					GameInitializer.startgame(false, this);
				}
			}
		} else {
			if ((!inMenu) && (movable) && (!inBattle) && (!walking)) {
				eventHandler.handleWorldEvent(keyCode);
			}
			if (inMenu) {
				eventHandler.handleMenuEvent(keyCode);
			}
			if (inBattle) {
				eventHandler.handleBattleEvent(keyCode);
			}
		}
		repaint();
		validate();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

}