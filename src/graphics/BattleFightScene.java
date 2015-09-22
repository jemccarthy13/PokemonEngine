package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import party.Battler;
import utilities.BattleEngine;
import utilities.BattleEngine.TURN;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of the battle fight scene
 */
public class BattleFightScene implements Scene {

	/**
	 * Singleton instance
	 */
	public static BattleFightScene instance = new BattleFightScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BattleFightScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 5;

	/**
	 * Render the battle fight scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		// fight menu is layered on main battle scene
		BattleScene.instance.render(g, gameControl);

		Battler playerPokemon = BattleEngine.getInstance().playerCurrentPokemon;

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
		g.drawImage(SpriteLibrary.getImage("Arrow"), arrowX[selX], arrowY[selY], null);

		Painter.paintBattlerInfo(g);
	}

	/**
	 * Handle a key press at the battle fight scene
	 */
	@Override
	public void keyPress(int keyCode, GameController gameControl) {
		// at move selection menu
		int selX = BattleEngine.getInstance().currentSelectionFightX;
		int selY = BattleEngine.getInstance().currentSelectionFightY;

		switch (keyCode) {
		case KeyEvent.VK_UP:
			selY = 0;
			break;
		case KeyEvent.VK_DOWN:
			selY = 1;
			break;
		case KeyEvent.VK_LEFT:
			selX = 0;
			break;
		case KeyEvent.VK_RIGHT:
			selX = 1;
			break;
		case KeyEvent.VK_X:
			gameControl.setScreen(BattleScene.instance);
			break;
		case KeyEvent.VK_Z:
			int move = 2 * BattleEngine.getInstance().currentSelectionFightY
					+ BattleEngine.getInstance().currentSelectionFightX;
			BattleEngine.getInstance().takeTurn(TURN.PLAYER, move);
			BattleEngine.getInstance().enemyTurn();
			break;
		}

		// make sure the move exists before we move the arrow there
		if (2 * selX + selY <= BattleEngine.getInstance().playerCurrentPokemon.getNumMoves() - 1) {
			BattleEngine.getInstance().currentSelectionFightX = selX;
			BattleEngine.getInstance().currentSelectionFightY = selY;
		}

		// play sound when any button is pressed
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
