package graphics;

import utilities.EnumsAndConstants.MUSIC;
import utilities.Utils;
import driver.Main;

public class IntroScene {
	Main game;
	public int stage = 1;

	public IntroScene(Main theGame) {
		game = theGame;
	}

	public void Start() {
		game.inIntro = true;
		Utils.playBackgroundMusic(MUSIC.INTRO);
	}
}
