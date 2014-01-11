package factories;

import audio.MidiPlayer;

public class AudioFactory {
	public MidiPlayer TITLE = new MidiPlayer("/audio_lib/BGM/Title.mid", true);
	public MidiPlayer CONTINUEBGM = new MidiPlayer("/audio_lib/BGM/Continue.mid", true);
	public MidiPlayer NEWBARKTOWN = new MidiPlayer("/audio_lib/BGM/NewBarkTown.mid", true);
	public MidiPlayer TRAINER_BATTLE = new MidiPlayer("/audio_lib/BGM/TrainerBattle.mid", true);
	public MidiPlayer INTRO = new MidiPlayer("/audio_lib/BGM/Intro.mid", true);
	public MidiPlayer TRAINER1 = new MidiPlayer("/audio_lib/BGM/Encounter-1.mid", true);
	public MidiPlayer TRAINER2 = new MidiPlayer("/audio_lib/BGM/Encounter-2.mid", true);
	public MidiPlayer TRAINER3 = new MidiPlayer("/audio_lib/BGM/Encounter-3.mid", true);
	public MidiPlayer TRAINER4 = new MidiPlayer("/audio_lib/BGM/Encounter-4.mid", true);
	public MidiPlayer TRAINER5 = new MidiPlayer("/audio_lib/BGM/Encounter-5.mid", true);
	public MidiPlayer TRAINER6 = new MidiPlayer("/audio_lib/BGM/Encounter-6.mid", true);
}