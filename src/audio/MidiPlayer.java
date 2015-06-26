package audio;

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
			Sequence sequence = MidiSystem.getSequence(MidiPlayer.class.getResourceAsStream(this.filename));
			this.sequencer = MidiSystem.getSequencer();
			this.sequencer.open();
			this.sequencer.setSequence(sequence);
			this.sequencer.setLoopCount(999);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		try {
			this.sequencer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		this.sequencer.stop();
	}

	public String toString() {
		String retStr = this.filename;
		return retStr;
	}
}