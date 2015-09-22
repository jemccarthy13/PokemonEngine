package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import party.Party;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a battle switch battler scene
 */
public class BattlePartyScene implements Scene {

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
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage(partyBackground), 0, 0, null);
		g.drawImage(SpriteLibrary.getImage(partyFirstMember), 40, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 20, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 70, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 120, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 170, null);
		g.drawImage(SpriteLibrary.getImage(partyMember), 190, 220, null);

		Party playerPokemon = gameControl.getPlayer().getParty();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon().getImage(), 75, 40, null);
			g.drawString((playerPokemon.get(0)).getName(), 65, 130);
		}
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
