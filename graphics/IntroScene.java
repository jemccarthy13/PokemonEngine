package graphics;

import utilities.EnumsAndConstants.MUSIC;
import utilities.GameData;
import utilities.Utils;

public class IntroScene {
	GameData gData;
	public int stage = 1;

	public IntroScene(GameData theGame) {
		gData = theGame;
	}

	public void Start() {
		gData.inIntro = true;
		Utils.playBackgroundMusic(MUSIC.INTRO);
	}
}
