package graphics;

import java.awt.Graphics;

import model.Coordinate;
import party.Battler;
import utilities.BattleEngine;
import utilities.BattleEngine.TURN;
import controller.GameController;

/**
 * A representation of the battle fight scene
 */
public class BattleFightScene extends BaseScene {

	private static final long serialVersionUID = 1420590933250593485L;
	/**
	 * Singleton instance
	 */
	public static BattleFightScene instance = new BattleFightScene();

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

		int selX = gameControl.getCurrentColSelection();
		int selY = gameControl.getCurrentRowSelection();

		// draw the arrow based on current selection
		g.drawImage(SpriteLibrary.getImage("Arrow"), arrowX[selX], arrowY[selY], null);

		Painter.paintBattlerInfo(g);
	}

	private void checkMove(int row, int col, GameController gameControl) {
		// make sure the move exists before we move the arrow there
		int choice = 2 * row + col;
		int numMoves = BattleEngine.getInstance().playerCurrentPokemon.getNumMoves() - 1;

		System.out.println(choice + ", " + numMoves);

		if (choice <= numMoves && row >= 0 && col >= 0 && row <= 1 && col <= 1) {
			gameControl.setCurrentSelection(new Coordinate(row, col));
			gameControl.setCurrentSelection(new Coordinate(row, col));
		}
	}

	/**
	 * Up arrow button press
	 */
	public void doUp(GameController gameControl) {
		checkMove(gameControl.getCurrentRowSelection() - 1, gameControl.getCurrentColSelection(), gameControl);
	}

	/**
	 * Down arrow button press
	 */
	public void doDown(GameController gameControl) {
		checkMove(gameControl.getCurrentRowSelection() + 1, gameControl.getCurrentColSelection(), gameControl);
	}

	/**
	 * Left arrow button press
	 */
	public void doLeft(GameController gameControl) {
		checkMove(gameControl.getCurrentRowSelection(), gameControl.getCurrentColSelection() - 1, gameControl);
	}

	/**
	 * Right arrow button press
	 */
	public void doRight(GameController gameControl) {
		checkMove(gameControl.getCurrentRowSelection(), gameControl.getCurrentColSelection() + 1, gameControl);
	}

	/**
	 * "x" button press
	 */
	public void doBack(GameController gameControl) {
		gameControl.setScene(BattleScene.instance);
	}

	/**
	 * "z" button press
	 */
	public void doAction(GameController gameControl) {
		int move = 2 * gameControl.getCurrentRowSelection() + gameControl.getCurrentColSelection();
		BattleEngine.getInstance().takeTurn(TURN.PLAYER, move);
		BattleEngine.getInstance().enemyTurn();
		gameControl.setCurrentSelection(new Coordinate(0, 0));
	}
}
