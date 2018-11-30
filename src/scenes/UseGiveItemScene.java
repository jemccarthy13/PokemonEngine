package scenes;

import java.awt.Graphics;

import controller.GameController;

public class UseGiveItemScene extends PartyScene {

	/**
	 * Generated ID for serialization
	 */
	private static final long serialVersionUID = 2361537826451593318L;

	static UseGiveItemScene m_instance = new UseGiveItemScene();

	private UseGiveItemScene() {}

	public static UseGiveItemScene getInstance() {
		return m_instance;
	}

	@Override
	public void render(Graphics g, GameController gameControl) {
		super.render(g, gameControl);

		// draw arrows to select party member

		// add action for use / hold item
	}
}
