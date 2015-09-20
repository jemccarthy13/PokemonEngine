package audio;

import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import model.Configuration;
import utilities.DebugUtility;

/**
 * A representation of a music track
 */
public class AudioTrack {
	/**
	 * The AudioTrack class wraps this MIDI sequencer
	 */
	private Sequencer sequencer; // Audio track wraps MIDI sequencer
	/**
	 * The title of this audio track
	 */
	private String trackTitle;

	/**
	 * Construct an audio track given the title of the track
	 * 
	 * @param songTitle
	 *            - the title of the song
	 */
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

	/**
	 * Start playing the track
	 */
	public void start() {
		if (this.sequencer != null) {
			this.sequencer.start();
		}
	}

	/**
	 * Stop playing the track
	 */
	public void stop() {
		if (this.sequencer != null) {
			this.sequencer.stop();
			this.sequencer.setMicrosecondPosition(0L);
		}
	}

	/**
	 * Returns the track title of the track
	 * 
	 * @return track title
	 */
	public String toString() {
		return this.trackTitle;
	}
}