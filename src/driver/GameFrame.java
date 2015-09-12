package driver;

import graphics.SpriteLibrary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import trainers.NPCLibrary;
import utilities.DebugUtility;
import audio.AudioLibrary;
import driver.GameData.SCREEN;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 8002391898226135401L;
	static GamePanel pokemonGame;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default initializer for game.
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameFrame() {
		// Title changes with each game release (along with version)
		setTitle("Pokemon: Metallic Silver");

		setIconImage(SpriteLibrary.getImage("Icon"));

		// Add the main game panel to the game
		pokemonGame = new GamePanel();
		pokemonGame.addKeyListener(kListener);
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		add(pokemonGame);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// The absolute main starting point for the Pokemon game.
	// Creates the JFrame that houses all game logic.
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		GameFrame pf = new GameFrame();
		pf.setVisible(true);
		pf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pf.setResizable(false);
		pf.pack();
		pf.setLocationRelativeTo(null);

		DebugUtility.printMessage("Game session id: " + pokemonGame.game.getId());
		DebugUtility.printHeader("Startup completed");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// KeyListener override - the heart of the game driver
	//
	// Each menu / screen has buttons mapped to events
	// TODO - fix audio errors in linux
	//
	// ////////////////////////////////////////////////////////////////////////
	KeyListener kListener = new KeyListener() {

		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch (pokemonGame.game.getScreen()) {
			case TITLE: // title screen "press enter"
				if (keyCode == KeyEvent.VK_ENTER) {
					pokemonGame.game.playBackgroundMusic("Continue");
					pokemonGame.game.setScreen(SCREEN.CONTINUE);
				}
				break;
			case CONTINUE: // continue screen choice select
				if (keyCode == KeyEvent.VK_UP) {
					if (pokemonGame.game.getCurrentSelection() > 0)
						pokemonGame.game.decrementSelection();
				} else if (keyCode == KeyEvent.VK_DOWN) {
					if (pokemonGame.game.getCurrentSelection() < 2)
						pokemonGame.game.incrementSelection();
				}
				if (keyCode == KeyEvent.VK_Z) {
					// remove(pokemonGame);
					if (pokemonGame.game.getCurrentSelection() == 0) {
						pokemonGame.game.startGame(true);
					} else if (pokemonGame.game.getCurrentSelection() == 1) {
						pokemonGame.game.startGame(false);
					}
					pokemonGame.game.startNewTimer(pokemonGame);
					repaint();
					validate();
				}
				pokemonGame.game.playClip(AudioLibrary.SE_SELECT);
				break;
			case INTRO: // intro screen, advance oak's text
				if (keyCode == KeyEvent.VK_X)
					pokemonGame.nameScreen.removeChar();
				if (keyCode == KeyEvent.VK_Z) {
					pokemonGame.game.incrIntroStage();
					if (pokemonGame.game.getIntroStage() > NPCLibrary.getInstance().get("Professor Oak")
							.getTextLength() - 1) {
						pokemonGame.game.playBackgroundMusic("NewBarkTown");
						pokemonGame.game.setScreen(SCREEN.WORLD);
					} else if (pokemonGame.game.getIntroStage() == 15) {
						pokemonGame.game.setScreen(SCREEN.NAME);
						pokemonGame.nameScreen.setToBeNamed("PLAYER");
					}
				}
				break;
			case NAME:// name screen, add or remove chars
				if (keyCode == KeyEvent.VK_X)
					pokemonGame.nameScreen.removeChar();
				if ((keyCode == KeyEvent.VK_Z)) {
					if (pokemonGame.nameScreen.rowSelection == 5) {
						// check for end or del
						if (pokemonGame.nameScreen.colSelection == 1
								&& pokemonGame.nameScreen.getChosenName().length() > 0) {
							pokemonGame.game.getPlayer().setName(pokemonGame.nameScreen.getChosenName());
							pokemonGame.nameScreen.reset();
							pokemonGame.game.setScreen(SCREEN.INTRO);
						} else if (pokemonGame.nameScreen.colSelection == 0)
							pokemonGame.nameScreen.removeChar();
					} else {
						pokemonGame.nameScreen.addSelectedChar();
					}
				}
				if (keyCode == KeyEvent.VK_DOWN && pokemonGame.nameScreen.rowSelection < 5) {
					pokemonGame.nameScreen.rowSelection++;
					if (pokemonGame.nameScreen.rowSelection == 5 && pokemonGame.nameScreen.colSelection < 3)
						pokemonGame.nameScreen.colSelection = 0;
					if (pokemonGame.nameScreen.rowSelection == 5 && pokemonGame.nameScreen.colSelection >= 3)
						pokemonGame.nameScreen.colSelection = 1;
				} else if (keyCode == KeyEvent.VK_UP && pokemonGame.nameScreen.rowSelection > 0) {
					pokemonGame.nameScreen.rowSelection--;
				} else if (keyCode == KeyEvent.VK_LEFT && pokemonGame.nameScreen.colSelection > 0) {
					pokemonGame.nameScreen.colSelection--;
				} else if (keyCode == KeyEvent.VK_RIGHT && pokemonGame.nameScreen.rowSelection == 5
						&& pokemonGame.nameScreen.colSelection < 1) {
					pokemonGame.nameScreen.colSelection++;
				} else if (keyCode == KeyEvent.VK_RIGHT && pokemonGame.nameScreen.rowSelection < 5
						&& pokemonGame.nameScreen.colSelection < 5) {
					pokemonGame.nameScreen.colSelection++;
				}
				break;
			case POKEMON:
			case BAG:
			case SAVE:
			case OPTION:
			case POKEDEX:
			case POKEGEAR:
			case TRAINERCARD:
			case MENU:
				pokemonGame.eventHandler.handleMenuEvent(keyCode);
				break;
			default:
				// otherwise, fire the eventHandler
				if ((pokemonGame.game.getScreen() != SCREEN.MENU) && (pokemonGame.game.isMovable())
						&& (!pokemonGame.game.isInBattle()) && (!pokemonGame.game.isPlayerWalking())) {
					pokemonGame.eventHandler.handleWorldEvent(keyCode);
				} else if (pokemonGame.game.isInBattle()) {
					pokemonGame.eventHandler.handleBattleEvent(keyCode);
				}
			}
			repaint();
			validate();
		}

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}
	};
}
