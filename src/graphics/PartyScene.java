package graphics;

import java.awt.Color;
import java.awt.Graphics;

import model.Coordinate;
import party.Party;
import controller.GameController;

/**
 * A representation of the party scene
 */
public class PartyScene extends BaseScene {

	private static final long serialVersionUID = 4269262897256769883L;
	/**
	 * Singleton instance
	 */
	public static PartyScene instance = new PartyScene();

	private static String partyFirstMember = "PartyFirst";
	private static String partyMember = "PartyBar";
	private static String partyBackground = "PartyBG";
	private static String partyCancel = "PartyCancel";

	/**
	 * Render the party scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.getImage(partyBackground), 0, 0, null);

		int party_index = 0;
		int selection = gameControl.getCurrentRowSelection();

		if (selection == party_index) {
			g.drawImage(SpriteLibrary.getImage(partyFirstMember + "_Selected"), 40, 20, null);
		} else {
			g.drawImage(SpriteLibrary.getImage(partyFirstMember + "_NotSelected"), 40, 20, null);
		}

		int paintX = 190;
		int paintY = 20;

		for (party_index = 1; party_index < 6; party_index++) {
			if (party_index == selection) {
				g.drawImage(SpriteLibrary.getImage(partyMember + "_Selected"), paintX, paintY, null);
			} else {
				g.drawImage(SpriteLibrary.getImage(partyMember + "_NotSelected"), paintX, paintY, null);
			}
			paintY += 50;
		}

		Party playerPokemon = gameControl.getPlayer().getParty();
		if (playerPokemon.size() > 0) {
			g.drawImage((playerPokemon.get(0)).getIcon().getImage(), 75, 40, null);
			Painter.paintSmallString(g, (playerPokemon.get(0)).getName(), 45, 125);
		}

		paintX = 200;
		paintY = 25;

		for (party_index = 1; party_index < playerPokemon.size(); party_index++) {
			g.drawImage((playerPokemon.get(party_index)).getIcon().getImage(), paintX, paintY, null);
			g.drawString((playerPokemon.get(party_index)).getName(), paintX + 50, paintY + 20);
			paintY += 50;
		}

		paintX += 170;
		paintY -= 5;
		if (selection > 6) {
			g.drawImage(SpriteLibrary.getImage(partyCancel + "_Selected"), paintX, paintY, null);
		} else {
			g.drawImage(SpriteLibrary.getImage(partyCancel + "_NotSelected"), paintX, paintY, null);
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
		int row = control.getCurrentRowSelection();
		int col = control.getCurrentColSelection();
		int numPokemon = control.getPlayer().getParty().size();
		if (row > numPokemon - 1) {
			control.setCurrentSelection(new Coordinate(numPokemon - 1, col));
		} else if (control.getCurrentRowSelection() > 0)
			control.decrementRowSelection();
	}

	/**
	 * up arrow button press at Party scene
	 */
	public void doDown(GameController control) {
		int row = control.getCurrentRowSelection();
		int col = control.getCurrentColSelection();
		int numPokemon = control.getPlayer().getParty().size();
		if (row < numPokemon - 1)
			control.incrementRowSelection();
		else if (row == numPokemon - 1) {
			control.setCurrentSelection(new Coordinate(7, col));
		}
	}
}
