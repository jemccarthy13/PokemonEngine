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
		// this.bgm = b;
		try {
			System.out.println(this.filename);
			System.out.println(MidiPlayer.class.getResourceAsStream(this.filename));
			Sequence sequence = MidiSystem.getSequence(MidiPlayer.class.getResourceAsStream(this.filename));
			this.sequencer = MidiSystem.getSequencer();
			this.sequencer.open();
			this.sequencer.setSequence(sequence);
			// if (this.bgm) {
			this.sequencer.setLoopCount(999);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		// if (this.bgm)
		// return;
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