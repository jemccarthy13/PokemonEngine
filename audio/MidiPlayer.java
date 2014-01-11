package audio;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer {
	private Sequencer sequencer;
	private String filename;
	private boolean bgm;

	public MidiPlayer(String file, boolean b) {
		this.filename = file;
		this.bgm = b;
		try {
			Sequence sequence = MidiSystem.getSequence(MidiPlayer.class.getResourceAsStream(this.filename));
			sequencer = MidiSystem.getSequencer();
			this.sequencer.open();
			this.sequencer.setSequence(sequence);
			if (this.bgm) {
				this.sequencer.setLoopCount(999);
			}
		} catch (Exception e) {}
	}

	public void start() {
		try {
			this.sequencer.start();
		} catch (Exception e) {}
	}

	public void stop() {
		this.sequencer.stop();
	}
}