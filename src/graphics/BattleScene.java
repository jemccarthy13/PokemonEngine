package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import party.Battler;
import party.Battler.STAT;
import party.Battler.STATUS;
import utilities.BattleEngine;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class BattleScene implements Scene {

	private static final long serialVersionUID = 2665700331714781084L;
	/**
	 * Singleton instance
	 */
	public static BattleScene instance = new BattleScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BattleScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 4;

	/**
	 * Render the battle scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("BG"), 0, 0, null);

		Battler playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;
		Battler enemyPokemon = BattleEngine.getInstance().enemyCurrentPokemon;

		if (playerPokemon.getStat(STAT.HP) > 0) {
			g.drawImage(playerPokemon.getBackSprite().getImage(),
			// the getHeight(null) used to be getHeight(gamePanel)
					120 - (playerPokemon.getBackSprite().getImage().getHeight(null)) / 2, 228 - playerPokemon
							.getBackSprite().getImage().getHeight(null), null);
		}
		if (enemyPokemon.getStat(STAT.HP) > 0) {
			g.drawImage(enemyPokemon.getFrontSprite().getImage(), 310, 25, null);
		}

		g.drawImage(SpriteLibrary.getImage("Battle"), 0, 0, null);
		String[] battleMessage = gameControl.getCurrentMessage();
		if (battleMessage != null) {
			g.drawString(battleMessage[0], 30, 260);
			g.drawString(battleMessage[1], 30, 290);
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
		g.drawImage(SpriteLibrary.getImage("Arrow"), arrowX[selX], arrowY[selY], null);

		STATUS playerPartyStatus = playerPokemon.getStatusEffect();
		if (playerPartyStatus != STATUS.NORMAL) {
			g.drawImage(SpriteLibrary.getImage("Status" + playerPartyStatus), 415, 140, null);
		}

		STATUS enemyPartyStatus = enemyPokemon.getStatusEffect();
		if (enemyPartyStatus != STATUS.NORMAL) {
			g.drawImage(SpriteLibrary.getImage("Status" + enemyPartyStatus), 18, 60, null);
		}

		Painter.paintBattlerInfo(g);
	}

	/**
	 * Change this scene's selectors based on key input
	 * 
	 * @param keyCode
	 *            - the key that was pressed
	 */
	private void changeSelection(int keyCode) {
		if (keyCode == KeyEvent.VK_UP) {
			BattleEngine.getInstance().currentSelectionMainY = 0;
		} else if (keyCode == KeyEvent.VK_DOWN) {
			BattleEngine.getInstance().currentSelectionMainY = 1;
		} else if (keyCode == KeyEvent.VK_LEFT) {
			BattleEngine.getInstance().currentSelectionMainX = 0;
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			BattleEngine.getInstance().currentSelectionMainX = 1;
		}
	}

	/**
	 * Handle a key press at the battle scene
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {
		if (keyCode == KeyEvent.VK_Z) {
			// do logic based on current selection
			switch (2 * BattleEngine.getInstance().currentSelectionMainY
					+ BattleEngine.getInstance().currentSelectionMainX) {
			case 0:
				gameControl.setScreen(BattleFightScene.instance);
				break;
			case 1:
				gameControl.setScreen(BattlePartyScene.instance);
				break;
			case 2:
				gameControl.setScreen(BattleItemScene.instance);
				break;
			case 3:
				// try to run away if wild
				if (BattleEngine.getInstance().enemyName == null) {
					// TODO probability of running away...
					gameControl.setCurrentMessage("Got away safely!");
					gameControl.setScreen(WorldScene.instance);
				} else {
					// but if it's an opponent, player is stuck
					gameControl.setCurrentMessage("Can't run away from a opponent!");
				}
				break;
			}
		} else {
			changeSelection(keyCode);
		}
		gameControl.playClip(SOUND_EFFECT.SELECT);
	}

	/**
	 * @return the ID of this scene
	 */
	@Override
	public Integer getId() {
		return this.ID;
	}

}
