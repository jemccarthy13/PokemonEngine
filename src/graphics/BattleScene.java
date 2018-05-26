package graphics;

import java.awt.Graphics;

import controller.GameController;
import model.MessageQueue;
import party.Battler;
import party.Battler.STAT;
import party.Battler.STATUS;
import utilities.BattleEngine;

/**
 * A representation of main battle scene
 */
public class BattleScene extends BaseScene {

	private static final long serialVersionUID = 2665700331714781084L;

	/**
	 * Singleton instance
	 */
	public static BattleScene instance = new BattleScene();

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
					120 - (playerPokemon.getBackSprite().getImage().getHeight(null)) / 2,
					228 - playerPokemon.getBackSprite().getImage().getHeight(null), null);
		}
		if (enemyPokemon.getStat(STAT.HP) > 0) {
			g.drawImage(enemyPokemon.getFrontSprite().getImage(), 310, 25, null);
		}

		g.drawImage(SpriteLibrary.getImage("Battle"), 0, 0, null);
		String[] battleMessage = MessageQueue.getInstance().getMessages();
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
	 * Right arrow button press
	 */
	public void doRight(GameController gameControl) {
		BattleEngine.getInstance().currentSelectionMainX = 1;
	}

	/**
	 * Left arrow button press
	 */
	public void doLeft(GameController gameControl) {
		BattleEngine.getInstance().currentSelectionMainX = 0;
	}

	/**
	 * Up arrow button press
	 */
	public void doDown(GameController gameControl) {
		BattleEngine.getInstance().currentSelectionMainY = 1;
	}

	/**
	 * Up arrow button press
	 */
	public void doUp(GameController gameControl) {
		BattleEngine.getInstance().currentSelectionMainY = 0;
	}

	/**
	 * "z" button press
	 */
	public void doAction(GameController gameControl) {
		// do logic based on current selection
		switch (2 * BattleEngine.getInstance().currentSelectionMainY
				+ BattleEngine.getInstance().currentSelectionMainX) {
		case 0:
			gameControl.setScene(BattleFightScene.instance);
			break;
		case 1:
			gameControl.setScene(BattlePartyScene.instance);
			break;
		case 2:
			gameControl.setScene(BattleItemScene.instance);
			break;
		case 3:
			// try to run away if wild
			if (BattleEngine.getInstance().enemyName == null) {
				// TODO probability of running away...
				MessageQueue.getInstance().add("Got away safely!");
				gameControl.setScene(WorldScene.instance);
			} else {
				// but if it's an opponent, player is stuck
				MessageQueue.getInstance().add("Can't run away from a opponent!");
			}
			break;
		}
	}

}
