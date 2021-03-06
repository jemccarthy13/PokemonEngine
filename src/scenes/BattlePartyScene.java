package scenes;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import graphics.GameGraphicsData;

/**
 * A representation of a battle switch battler scene
 */
public class BattlePartyScene extends BaseScene {

	private static final long serialVersionUID = 4495645750145027698L;
	/**
	 * Singleton instance
	 */
	public static BattlePartyScene instance = new BattlePartyScene();

	/**
	 * Render the party scene.
	 * 
	 * TDOO finish render of battle party
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		PartyScene.instance.render(g, gameControl);
	}

	/**
	 * Handle a key press at the party scene
	 * 
	 * todo implement switching battlers mid battle
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
			GameGraphicsData.getInstance().setScene(BattleScene.instance);
		}
	}
}
