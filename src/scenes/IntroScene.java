package scenes;

import java.awt.Graphics;

import audio.AudioLibrary;
import controller.GameController;
import graphics.GameGraphicsData;
import graphics.SpriteLibrary;
import model.GameData;
import model.MessageQueue;
import model.NameBuilder;
import trainers.Actor.DIR;

/**
 * A representation of intro scene
 */
public class IntroScene extends BaseScene {

	private static final long serialVersionUID = 2833416784544170753L;
	/**
	 * Singleton instance
	 */
	public static IntroScene instance = new IntroScene();

	/**
	 * Render the Introduction scene.
	 */
	@Override
	public void render(Graphics g, GameController gameControl) {
		g.drawImage(SpriteLibrary.getImage("Beginning"), 0, 0, null);
		g.drawImage(SpriteLibrary.getSpriteForDir("PROFESSOROAK_LARGE", DIR.SOUTH).getImage(), 150, 20, null);
	}

	/**
	 * Perform "Z" button click
	 */
	@Override
	public void doAction(GameController control) {
		GameController.incrIntroStage();
		if (GameData.getInstance().introStage == 15) {
			GameGraphicsData.getInstance().setScene(NameScene.instance);
			NameBuilder.getInstance().setToBeNamed("PLAYER");
		}

		if (!MessageQueue.getInstance().hasNextMessage()) {
			AudioLibrary.playBackgroundMusic("NewBarkTown");
			GameGraphicsData.getInstance().setScene(WorldScene.instance);
		}

	}
}
