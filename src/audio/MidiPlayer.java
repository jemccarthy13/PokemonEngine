package audio;

import java.io.File;
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
		System.out.println(filename);
		try {
			System.out.println(new File(filename).exists());
			InputStream resource = MidiPlayer.class.getResourceAsStream(this.filename.replace("resources", ""));
			System.out.println(resource);
			Sequence sequence = MidiSystem.getSequence(resource);
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