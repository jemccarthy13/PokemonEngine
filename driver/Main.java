package driver;

import graphics.BattleScene;
import graphics.IntroScene;
import graphics.MenuScene;
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

import pokedex.Pokemon;
import pokedex.PokemonList;
import tiles.Coordinate;
import trainers.NPC;
import utilities.EnumsAndConstants;
import utilities.EnumsAndConstants.DIR;
import utilities.EnumsAndConstants.MUSIC;
import utilities.GameData;
import utilities.Utils;

public class Main extends JPanel implements KeyListener, ActionListener {

	// ////////////////////////////////////////////////////////////////////////
	// ======================== VARIABLE DECLARATIONS =======================//
	// ================= Movement control variables =========================//
	boolean walking = false;
	private int movespritepixels = 0; // movement (animation) counter
	public boolean movable = true;
	boolean[] moveable_dir = new boolean[4];
	boolean rightFoot = false;
	// ====================== NPC Random movement controller ================//
	public static NPCThread NPCTHREAD = new NPCThread();
	// ===================== Graphics Logic Controllers =====================//
	public BattleScene encounter;
	public MenuScene menuScreen;
	public IntroScene introScreen;
	public NameScene nameScreen;
	public EventHandler eventHandler;
	// ======================== Game logic Data =============================//
	public GameData gData = new GameData();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor for Main panel. This is the panel that all
	// aspects of the game are painted to / from.
	//
	// Constructor initializes event handler, all the scenes that get
	// painted during gameplay, and the timer that handles sprite movement
	// speeds. Also sets up input controller (key listener).
	//
	// ////////////////////////////////////////////////////////////////////////
	public Main() {
		eventHandler = new EventHandler(this);
		menuScreen = new MenuScene(gData);
		introScreen = new IntroScene(gData);
		nameScreen = new NameScene();

		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(480, 320));
		addKeyListener(this);
		gData.gameTimer = new Timer(100 - EnumsAndConstants.PLAYERSPEED, this);
		gData.gameTimer.start();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Any time an action is performed in the frame, this method is called.
	// In Pokemon game, this method is constantly listening for actions
	//
	// ////////////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e) {

		gData.updateTime(); // update the time played

		if (gData.inBattle) {
			if (encounter.playerPokemon.getStat(EnumsAndConstants.STATS.HP) <= 0) {
				handleWhiteOut();
			}
			if (encounter.enemyPokemon.get(0).getStat(EnumsAndConstants.STATS.HP) <= 0) {
				handleBattleWon();
			}
			if (!encounter.playerTurn) {
				encounter.enemyTurn();
			}
		} else if (!gData.atTitle && !gData.inIntro && !gData.inNameScreen && !gData.inMenu && !gData.atContinueScreen) {
			handleMovement();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Handles player movement on keyboard input
	//
	// ////////////////////////////////////////////////////////////////////////
	private void handleMovement() {
		// get all comparison variables up front
		int playerCurX = gData.player.getCurrentX();
		int playerCurY = gData.player.getCurrentY();
		DIR playerDir = gData.player.getDir();
		PokemonList playerPokemon = gData.player.getPokemon();
		Coordinate playerPos = gData.player.pData.position;

		// check for collisions in each direction
		moveable_dir[DIR.NORTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.NORTH, gData) || gData.noClip;
		moveable_dir[DIR.WEST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.WEST, gData) || gData.noClip;
		moveable_dir[DIR.SOUTH.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.SOUTH, gData) || gData.noClip;
		moveable_dir[DIR.EAST.ordinal()] = gData.tm.canMoveInDir(playerPos, DIR.EAST, gData) || gData.noClip;

		if (walking) { // take care of walking animation graphics logic
			movespritepixels += 1;
			gData.offsetY = ((playerDir == DIR.NORTH) && (moveable_dir[DIR.NORTH.ordinal()])) ? gData.offsetY + 2
					: gData.offsetY;
			gData.offsetY = ((playerDir == DIR.SOUTH) && (moveable_dir[DIR.SOUTH.ordinal()])) ? gData.offsetY - 2
					: gData.offsetY;
			gData.offsetX = ((playerDir == DIR.WEST) && (moveable_dir[DIR.WEST.ordinal()])) ? gData.offsetX + 2
					: gData.offsetX;
			gData.offsetX = ((playerDir == DIR.EAST) && (moveable_dir[DIR.EAST.ordinal()])) ? gData.offsetX - 2
					: gData.offsetX;

			gData.player.changeSprite(movespritepixels, rightFoot);
		}

		// prep for door teleportation event check
		Map<Coordinate, Coordinate> dict = EnumsAndConstants.TELEPORTS.getListofTeleports();
		Set<Coordinate> k = dict.keySet();
		boolean teleported = false;

		for (Coordinate x : k) {
			if (x.equals(playerPos)) {
				gData.player.setLoc(dict.get(x));

				gData.start_coorX = (gData.player.getCurrentX() - x.getX()) * -1 * EnumsAndConstants.TILESIZE;
				gData.start_coorY = (gData.player.getCurrentY() - x.getY()) * -1 * EnumsAndConstants.TILESIZE;
				teleported = true;
				gData.player.setDir(EnumsAndConstants.DIR.NORTH);
			}
		}

		// finally, the animation is done, handle the movement logic
		if (movespritepixels >= 16 && !teleported) {
			movespritepixels = 0; // reset animation counter
			walking = false; // player is no longer in animation
			rightFoot = (!rightFoot);
			if (moveable_dir[playerDir.ordinal()])
				gData.player.move(playerDir);
			for (Pokemon p : playerPokemon) { // deal PZN/BRN damage
				if ((p.statusEffect == 2) || (p.statusEffect == 3))
					p.doDamageWithoutSound(1);
			}
		}

		// check for trainer encounter with any NPC
		for (NPC curNPC : EnumsAndConstants.npc_lib.npcs) {
			boolean beaten = gData.player.beatenTrainers.contains(curNPC.getName());
			if (curNPC.isTrainer() && !walking && !gData.noBattle && !beaten && playerInRange(curNPC) && !gData.inMenu) {
				NPCTHREAD.stop = true;
				enemyTrainerAnimation(curNPC);
				doTrainerBattle(curNPC);
			} else {
				movable = true;
			}
		}
	}

	private boolean playerInRange(NPC curNPC) {
		int playerCurY = gData.player.getCurrentY();
		int playerCurX = gData.player.getCurrentX();
		int NPC_X = curNPC.getCurrentX();
		int NPC_Y = curNPC.getCurrentY();
		DIR NPC_DIR = curNPC.getDirection();

		return (((playerCurX == curNPC.getCurrentX()) && (((playerCurY < NPC_Y) && (NPC_Y - playerCurY <= 5) && (NPC_DIR == DIR.NORTH)) || ((playerCurY > NPC_Y)
				&& (playerCurY - NPC_Y <= 5) && (NPC_DIR == DIR.SOUTH)))) || ((playerCurY == NPC_Y) && (((playerCurX < NPC_X)
				&& (NPC_X - playerCurX <= 5) && (NPC_DIR == DIR.WEST)) || ((playerCurX > NPC_X)
				&& (playerCurX - NPC_X <= 5) && (NPC_DIR == DIR.EAST)))));
	}

	private void enemyTrainerAnimation(NPC curNPC) {
		Utils.pickTrainerMusic();
		System.out.println("TODO: Player ! upon beeing seen.");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		DIR NPC_DIR = curNPC.getDirection();

		gData.tm.set(curNPC.nData.position, EnumsAndConstants.TILE);
		if (NPC_DIR == DIR.NORTH) {
			gData.player.setSpriteFacing(DIR.SOUTH);
			while (curNPC.getCurrentY() > gData.player.getCurrentY() + 1) {
				gData.tm.set(curNPC.nData.position, EnumsAndConstants.TILE);
				curNPC.move(DIR.NORTH);
				paintComponent(getGraphics());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException localInterruptedException) {}
			}
		}
		if (NPC_DIR == DIR.SOUTH) {
			gData.player.setSpriteFacing(DIR.NORTH);
			while (curNPC.getCurrentY() < gData.player.getCurrentY() - 1) {
				gData.tm.set(curNPC.nData.position, EnumsAndConstants.TILE);
				curNPC.move(DIR.SOUTH);
				paintComponent(getGraphics());
				try {
					Thread.sleep(500L);
				} catch (InterruptedException localInterruptedException1) {}
			}
		}
		if (NPC_DIR == DIR.EAST) {
			gData.player.setSpriteFacing(DIR.WEST);
			while (curNPC.getCurrentX() < gData.player.getCurrentX() - 1) {
				gData.tm.set(curNPC.nData.position, EnumsAndConstants.TILE);
				curNPC.move(DIR.EAST);
				paintComponent(getGraphics());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException localInterruptedException2) {}
			}
		}
		if (NPC_DIR == DIR.WEST) {
			gData.player.setSpriteFacing(DIR.EAST);
			while (curNPC.getCurrentX() > gData.player.getCurrentX() + 1) {
				gData.tm.set(curNPC.nData.position, EnumsAndConstants.TILE);
				curNPC.move(DIR.WEST);
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
			gData.player.beatenTrainers.add(encounter.enemy.getName());
		}
	}

	private void handleWhiteOut() {
		System.out.println("Player Pokemon has fainted");
		System.out.println(gData.player.getName() + " is all out of usable Pokemon!");
		System.out.println(gData.player.getName() + " whited out.");
		encounter.whiteOut();
		gData.player.setSprite(EnumsAndConstants.sprite_lib.getSprites("PLAYER").get(9));
		gData.player.getPokemon().get(0).heal(-1);
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
		gData.inBattle = true;
		encounter.Start();
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (gData.atTitle) {
			if (keyCode == KeyEvent.VK_ENTER) {
				gData.atTitle = false;
				Utils.playBackgroundMusic(MUSIC.CONTINUE);
				gData.atContinueScreen = true;
			}
		} else if (gData.inIntro) {
			if (keyCode == KeyEvent.VK_X) {
				nameScreen.removeChar();
			}
			if (keyCode == KeyEvent.VK_Z) {
				introScreen.stage += 2;
				if (introScreen.stage > EnumsAndConstants.npc_lib.getNPC("Professor Oak").getTextLength() - 1) {
					Utils.playBackgroundMusic(MUSIC.NEWBARKTOWN);
					gData.inIntro = !gData.inIntro;
				}
				if (introScreen.stage == 15) {
					gData.inNameScreen = true;
					nameScreen.setToBeNamed("PLAYER");
					gData.inIntro = false;
				}
			}
		} else if (gData.inNameScreen) {
			if (keyCode == KeyEvent.VK_X) {
				nameScreen.removeChar();
			}
			if ((keyCode == KeyEvent.VK_Z)) {
				if (nameScreen.rowSelection == 5) {
					if (nameScreen.colSelection == 1 && nameScreen.getNameSelected().length() > 0) {
						gData.player.setName(nameScreen.getNameSelected());
						nameScreen.reset();
						gData.inNameScreen = false;
						gData.inIntro = true;
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
		} else if ((gData.atContinueScreen) && (!gData.atTitle)) {
			if (keyCode == KeyEvent.VK_UP) {
				Utils.playSelectSound();
				if (gData.menuSelection > 0) {
					gData.menuSelection -= 1;
				}
			} else if (keyCode == KeyEvent.VK_DOWN) {
				Utils.playSelectSound();
				if (gData.menuSelection < 2) {
					gData.menuSelection += 1;
				}
			}
			if (keyCode == KeyEvent.VK_Z) {
				Utils.playSelectSound();
				if (gData.menuSelection == 0) {
					GameInitializer.startgame(true, this);
				} else if (gData.menuSelection == 1) {
					GameInitializer.startgame(false, this);
				}
			}
		} else {
			if ((!gData.inMenu) && (movable) && (!gData.inBattle) && (!walking)) {
				eventHandler.handleWorldEvent(keyCode);
			}
			if (gData.inMenu) {
				eventHandler.handleMenuEvent(keyCode);
			}
			if (gData.inBattle) {
				eventHandler.handleBattleEvent(keyCode);
			}
		}
		repaint();
		validate();
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

}