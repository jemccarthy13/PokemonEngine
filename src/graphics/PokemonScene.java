package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import party.Party;
import controller.GameController;
import controller.GameKeyListener;

/**
 * A representation of a title scene
 */
public class PokemonScene implements Scene {

	private static final long serialVersionUID = 4269262897256769883L;
	/**
	 * Singleton instance
	 */
	public static PokemonScene instance = new PokemonScene();

	/**
	 * When it is created, register itself for Painting and KeyPress
	 */
	private PokemonScene() {
		Painter.getInstance().register(this);
		GameKeyListener.getInstance().register(this);
	};

	/**
	 * The maps will use this ID to reference the Scene objects
	 */
	public int ID = 13;

	/**
	 * Render the title scene.
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
	 * Handle a key press at the title scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_X) {
			control.setScreen(MenuScene.instance);
		}
		if (keyCode == KeyEvent.VK_UP) {
			control.decrementRowSelection();
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			control.incrementRowSelection();
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
