package graphics;

import java.awt.Graphics;

import controller.GameController;
import model.Coordinate;
import model.GameData;
import party.Battler;
import utilities.BattleEngine;
import utilities.BattleEngine.TURN;
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
		super.maxColSelection = 1;
		super.maxRowSelection = 1;
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

		int selX = GameData.getInstance().getCurrentColSelection(gameControl.getScene());
		int selY = GameData.getInstance().getCurrentRowSelection(gameControl.getScene());

		// draw the arrow based on current selection
		g.drawImage(SpriteLibrary.getImage("Arrow"), arrowX[selX], arrowY[selY], null);

		Painter.paintBattlerInfo(g);
	}

	private void checkMove(int rowDir, int colDir, GameController gameControl) {
		int row = GameData.getInstance().getCurrentRowSelection(gameControl.getScene()) + rowDir;
		int col = GameData.getInstance().getCurrentColSelection(gameControl.getScene()) + colDir;

		// make sure the move exists before we move the arrow there
		int choice = 2 * row + col;
		int numMoves = BattleEngine.getInstance().playerCurrentPokemon.getNumMoves() - 1;

		if (choice <= numMoves && row >= 0 && col >= 0 && row <= 1 && col <= 1) {
			GameData.getInstance().setCurrentSelection(gameControl.getScene(), new Coordinate(row, col));
		} else {
			// Don't get stuck if one pokemon has 4 moves and next has 2
			// default to the first
			GameData.getInstance().setCurrentSelection(gameControl.getScene(), new Coordinate(0, 0));
		}
	}

	/**
	 * Up arrow button press
	 */
	public void doUp(GameController gameControl) {
		checkMove(-1, 0, gameControl);
	}

	/**
	 * Down arrow button press
	 */
	public void doDown(GameController gameControl) {
		checkMove(1, 0, gameControl);
	}

	/**
	 * Left arrow button press
	 */
	public void doLeft(GameController gameControl) {
		checkMove(0, -1, gameControl);
	}

	/**
	 * Right arrow button press
	 */
	public void doRight(GameController gameControl) {
		checkMove(0, 1, gameControl);
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
		int move = 2 * GameData.getInstance().getCurrentRowSelection(gameControl.getScene())
				+ GameData.getInstance().getCurrentColSelection(gameControl.getScene());
		BattleEngine.getInstance().takeTurn(TURN.PLAYER, move);
		BattleEngine.getInstance().takeTurn(TURN.OPPONENT,
				RandomNumUtils.generateRandom(0, BattleEngine.getInstance().enemyCurrentPokemon.getNumMoves() - 1));
		GameData.getInstance().setCurrentSelection(gameControl.getScene(), new Coordinate(0, 0));
	}
}
