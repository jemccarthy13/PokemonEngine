package audio;

import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

// ////////////////////////////////////////////////////////////////////////
//
// MidiPlayer - handles playing the back ground music
//
// ////////////////////////////////////////////////////////////////////////
public class MidiPlayer {
	private Sequencer sequencer;
	private String filename;

	public MidiPlayer(String file, boolean b) {
		this.filename = file;
		try {
			InputStream resource = MidiPlayer.class.getResourceAsStream(this.filename.replace("resources", ""));
			Sequence sequence = MidiSystem.getSequence(resource);
			this.sequencer = MidiSystem.getSequencer();
			this.sequencer.open();
			this.sequencer.setSequence(sequence);
			this.sequencer.setLoopCount(999);
		} catch (Exception e) {
			System.err.println("Cannot load sound " + file);
		}
	}

	public void start() {
		try {
			if (this.sequencer != null) {
				this.sequencer.start();
			} else {
				System.out.println("sound " + filename + " was not loaded");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		if (this.sequencer != null) {
			this.sequencer.stop();
		}
	}

	public String toString() {
		String retStr = this.filename;
		return retStr;
	}
}