package audio;

import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import model.Configuration;
import utilities.DebugUtility;

// ////////////////////////////////////////////////////////////////////////
//
// AudioTrack - handles playing the back ground music
//
// ////////////////////////////////////////////////////////////////////////
public class AudioTrack {
	private Sequencer sequencer; // Audio track wraps MIDI sequencer
	private String trackTitle;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Construct an audio track given a title of the track
	//
	// ////////////////////////////////////////////////////////////////////////
	public AudioTrack(String songTitle) {
		trackTitle = songTitle;
		String path = (Configuration.MUSIC_PATH + trackTitle.replace(" ", "") + ".mid").replace("resources", "");
		try {
			InputStream resource = AudioTrack.class.getResourceAsStream(path);
			Sequence sequence = MidiSystem.getSequence(resource);
			this.sequencer = MidiSystem.getSequencer();
			this.sequencer.open();
			this.sequencer.setSequence(sequence);
			this.sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			DebugUtility.printError("Cannot load sound " + trackTitle);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Start this audio track
	//
	// ////////////////////////////////////////////////////////////////////////
	public void start() {
		if (this.sequencer != null) {
			this.sequencer.start();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Stop this audio track
	//
	// ////////////////////////////////////////////////////////////////////////
	public void stop() {
		if (this.sequencer != null) {
			this.sequencer.stop();
			this.sequencer.setMicrosecondPosition(0L);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Return the track title of this track
	//
	// ////////////////////////////////////////////////////////////////////////
	public String toString() {
		return this.trackTitle;
	}
}