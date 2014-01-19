package libraries;

import utilities.EnumsAndConstants;
import utilities.MidiPlayer;

// ////////////////////////////////////////////////////////////////////////
//
// AudioLibrary intializes variables to be used as constants in playing
// music from this library.
//
// ////////////////////////////////////////////////////////////////////////
public class AudioLibrary {

	String musicPath = EnumsAndConstants.BGMUSICPATH;
	public MidiPlayer TITLE = new MidiPlayer(musicPath + "Title.mid", true);
	public MidiPlayer CONTINUEBGM = new MidiPlayer(musicPath + "Continue.mid", true);
	public MidiPlayer NEWBARKTOWN = new MidiPlayer(musicPath + "NewBarkTown.mid", true);
	public MidiPlayer TRAINER_BATTLE = new MidiPlayer(musicPath + "TrainerBattle.mid", true);
	public MidiPlayer INTRO = new MidiPlayer(musicPath + "Intro.mid", true);
	public MidiPlayer TRAINER1 = new MidiPlayer(musicPath + "Encounter-1.mid", true);
	public MidiPlayer TRAINER2 = new MidiPlayer(musicPath + "Encounter-2.mid", true);
	public MidiPlayer TRAINER3 = new MidiPlayer(musicPath + "Encounter-3.mid", true);
	public MidiPlayer TRAINER4 = new MidiPlayer(musicPath + "Encounter-4.mid", true);
	public MidiPlayer TRAINER5 = new MidiPlayer(musicPath + "Encounter-5.mid", true);
	public MidiPlayer TRAINER6 = new MidiPlayer(musicPath + "Encounter-6.mid", true);
}