package driver;

import graphics.SpriteLibrary;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import trainers.NPCLibrary;
import audio.AudioLibrary;

public class GameFrame extends JFrame {

	private static final long serialVersionUID = 8002391898226135401L;
	Game pokemonGame;

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
		pokemonGame = new Game();
		pokemonGame.addKeyListener(kListener);
		pokemonGame.setFocusable(true);
		pokemonGame.requestFocus();
		add(pokemonGame);
		System.out.println("** Issued new session id: " + pokemonGame.gData.id);

		AudioLibrary.getInstance().playBackgroundMusic("Title", pokemonGame.gData.option_sound);
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
			if (pokemonGame.gData.atTitle) {
				// title screen "press enter"
				if (keyCode == KeyEvent.VK_ENTER) {
					pokemonGame.gData.atTitle = false;
					AudioLibrary.getInstance().playBackgroundMusic("Continue", pokemonGame.gData.option_sound);
					pokemonGame.gData.atContinueScreen = true;
				}
			} else if (pokemonGame.gData.atContinueScreen) {
				// continue screen choice select
				if (keyCode == KeyEvent.VK_UP) {
					if (pokemonGame.gData.menuSelection > 0)
						pokemonGame.gData.menuSelection -= 1;
				} else if (keyCode == KeyEvent.VK_DOWN) {
					if (pokemonGame.gData.menuSelection < 2)
						pokemonGame.gData.menuSelection += 1;
				}
				if (keyCode == KeyEvent.VK_Z) {
					// remove(pokemonGame);
					if (pokemonGame.gData.menuSelection == 0) {
						pokemonGame.gData = GameInitializer.startGame(true, pokemonGame);
					} else if (pokemonGame.gData.menuSelection == 1) {
						pokemonGame.gData = GameInitializer.startGame(false, pokemonGame);
					}
					repaint();
					validate();
				}
				AudioLibrary.getInstance().playClip(AudioLibrary.getInstance().SE_SELECT,
						pokemonGame.gData.option_sound);
			} else if (pokemonGame.gData.inIntro) {
				// intro screen, advance oak's text
				if (keyCode == KeyEvent.VK_X)
					pokemonGame.nameScreen.removeChar();
				if (keyCode == KeyEvent.VK_Z) {
					pokemonGame.gData.introStage += 2;
					if (pokemonGame.gData.introStage > NPCLibrary.getInstance().get("Professor Oak").getTextLength() - 1) {
						AudioLibrary.getInstance().playBackgroundMusic("NewBarkTown", pokemonGame.gData.option_sound);
						pokemonGame.gData.inIntro = !pokemonGame.gData.inIntro;
					} else if (pokemonGame.gData.introStage == 15) {
						pokemonGame.gData.inNameScreen = true;
						pokemonGame.nameScreen.setToBeNamed("PLAYER");
						pokemonGame.gData.inIntro = false;
					}
				}
			} else if (pokemonGame.gData.inNameScreen) {
				// name screen, add or remove chars
				if (keyCode == KeyEvent.VK_X)
					pokemonGame.nameScreen.removeChar();
				if ((keyCode == KeyEvent.VK_Z)) {
					if (pokemonGame.nameScreen.rowSelection == 5) {
						// check for end or del
						if (pokemonGame.nameScreen.colSelection == 1
								&& pokemonGame.nameScreen.getChosenName().length() > 0) {
							pokemonGame.gData.player.setName(pokemonGame.nameScreen.getChosenName());
							pokemonGame.nameScreen.reset();
							pokemonGame.gData.inNameScreen = false;
							pokemonGame.gData.inIntro = true;
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
			} else {
				// otherwise, fire the eventHandler
				if ((!pokemonGame.gData.inMenu) && (pokemonGame.movable) && (!pokemonGame.gData.inBattle)
						&& (!pokemonGame.walking)) {
					pokemonGame.eventHandler.handleWorldEvent(keyCode);
				} else if (pokemonGame.gData.inMenu) {
					pokemonGame.eventHandler.handleMenuEvent(keyCode);
				} else if (pokemonGame.gData.inBattle) {
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
