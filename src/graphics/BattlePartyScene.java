package graphics;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a battle switch battler scene
 */
public class BattlePartyScene implements Scene {

	private static final long serialVersionUID = 4495645750145027698L;
	/**
	 * Singleton instance
	 */
	public static BattlePartyScene instance = new BattlePartyScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private BattlePartyScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 7;

	/**
	 * Render the party scene.
	 * 
	 * TDOO finish render of battle party
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		PokemonScene.instance.render(g, gameControl);
	}

	/**
	 * Handle a key press at the party scene
	 * 
	 * TODO implement switching battlers mid battle
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X || keyCode == KeyEvent.VK_Z) {
			control.setScreen(BattleScene.instance);
		}
	}

	/**
	 * @return the ID of this scene
	 */
	@Override
	public Integer getId() {
		return this.ID;
	}

}
