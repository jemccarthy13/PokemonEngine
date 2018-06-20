package scenes;

import java.awt.Color;
import java.awt.Graphics;

import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;

/**
 * A representation of a battle item scene
 * 
 * TODO battle item scene painting of items and logic to use items
 */
public class BattleItemScene extends BaseScene {

	private static final long serialVersionUID = 4236362282505475572L;
	/**
	 * Singleton instance
	 */
	public static BattleItemScene instance = new BattleItemScene();

	/**
	 * Render the battle item scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage("BagScreen"), 0, 0, null);
	}

	/**
	 * Perform "z" button click
	 */
	@Override
	public void doAction(GameController control) {
		GameGraphicsData.getInstance().setScene(BattleScene.instance);
	}

	/**
	 * Perform "x" button click
	 */
	@Override
	public void doBack(GameController control) {
		GameGraphicsData.getInstance().setScene(BattleScene.instance);
	}
}
