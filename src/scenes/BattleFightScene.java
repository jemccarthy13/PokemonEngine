package scenes;

import java.awt.Graphics;

import controller.BattleEngine;
import controller.BattleEngine.TURN;
import controller.GameController;
import graphics.GameGraphicsData;
import graphics.Painter;
import graphics.SpriteLibrary;
import party.Battler;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * A representation of the battle fight scene
 */
public class BattleFightScene extends SelectionScene {

	private static final long serialVersionUID = 1420590933250593485L;
	/**
	 * Singleton instance
	 */
	public static BattleFightScene instance = new BattleFightScene();

	private BattleFightScene() {
		this.maxColSelection = 1;
		this.maxRowSelection = 1;
	}

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

		// draw the arrow based on current selection
		g.drawImage(SpriteLibrary.getImage("Arrow"), arrowX[this.rowSelection], arrowY[this.colSelection], null);

		Painter.paintBattlerInfo(g);
	}

	private void checkMove(int rowDir, int colDir) {
		int row = this.rowSelection + rowDir;
		int col = this.colSelection + colDir;

		// make sure the move exists before we move the arrow there
		int choice = (2 * col) + row;
		int numMoves = BattleEngine.getInstance().playerCurrentPokemon.getNumMoves() - 1;

		if (!(choice <= numMoves && row >= 0 && col >= 0 && row <= 1 && col <= 1)) {
			row = 0;
			col = 0;
		}
		this.rowSelection = row;
		this.colSelection = col;

		DebugUtility.printMessage("Selected: " + row + " " + col + " " + ((2 * col) + row));
	}

	/**
	 * Up arrow button press
	 */
	@Override
	public void doUp(GameController gameControl) {
		checkMove(0, -1);
	}

	/**
	 * Down arrow button press
	 */
	@Override
	public void doDown(GameController gameControl) {
		checkMove(0, 1);
	}

	/**
	 * Left arrow button press
	 */
	@Override
	public void doLeft(GameController gameControl) {
		checkMove(-1, 0);
	}

	/**
	 * Right arrow button press
	 */
	@Override
	public void doRight(GameController gameControl) {
		checkMove(1, 0);
	}

	/**
	 * "x" button press
	 */
	@Override
	public void doBack(GameController gameControl) {
		GameGraphicsData.getInstance().setScene(BattleScene.instance);
	}

	/**
	 * "z" button press
	 */
	@Override
	public void doAction(GameController gameControl) {
		int move = (2 * this.colSelection) + this.rowSelection;
		DebugUtility.printMessage("Selected: " + move);
		DebugUtility.printMessage(BattleEngine.getInstance().playerCurrentPokemon.getMove(move).toString());
		BattleEngine.getInstance().takeTurn(TURN.PLAYER, move);
		BattleEngine.getInstance().takeTurn(TURN.OPPONENT,
				RandomNumUtils.generateRandom(0, BattleEngine.getInstance().enemyCurrentPokemon.getNumMoves() - 1));
		this.rowSelection = 0;
		this.colSelection = 0;
	}
}
