package graphics;

import java.awt.Color;
import java.awt.Graphics;

import party.Party;
import controller.GameController;

/**
 * A representation of the party scene
 */
public class PokemonScene extends BaseScene {

	private static final long serialVersionUID = 4269262897256769883L;
	/**
	 * Singleton instance
	 */
	public static PokemonScene instance = new PokemonScene();

	/**
	 * Render the party scene.
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
	 * "x" button press at Party scene
	 */
	public void doBack(GameController control) {
		control.setScene(MenuScene.instance);
	}

	/**
	 * up arrow button press at Party scene
	 */
	public void doUp(GameController control) {
		control.decrementRowSelection();
	}

	/**
	 * up arrow button press at Party scene
	 */
	public void doDown(GameController control) {
		control.incrementRowSelection();
	}
}
