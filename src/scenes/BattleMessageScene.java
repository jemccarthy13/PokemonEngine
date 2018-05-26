package scenes;

import java.awt.Graphics;

import controller.GameController;

/**
 * A representation of a battle message scene
 */
public class BattleMessageScene extends BaseScene {

	private static final long serialVersionUID = 8333025208389293827L;
	/**
	 * Singleton instance
	 */
	public static BattleMessageScene instance = new BattleMessageScene();

	/**
	 * Render a battle message scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		BattleScene.instance.render(g, gameControl);
	}

	/**
	 * Perform "z" button click
	 */
	public void doAction(GameController gameControl) {
		gameControl.setScene(WorldScene.instance);
	}

}
