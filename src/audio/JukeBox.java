package audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import audio.AudioLibrary.SOUND_EFFECT;
import model.Configuration;
import utilities.DebugUtility;

/**
 * Handles playing clips
 */
public class JukeBox {

	/**
	 * saved clips that have already been loaded
	 */
	private HashMap<String, SoundEffect> availableClips = new HashMap<String, SoundEffect>();

	/**
	 * Loads a given audio sound effect file and maps to given sound name
	 * 
	 * @param clipName
	 *            - the name of the sound effect (should be same name as file)
	 */
	public void loadClip(String clipName) {
		String resourcePath = (Configuration.SOUND_EFFECT_PATH + clipName + ".wav").replace("resources", "");
		try {
			this.availableClips.put(clipName, new SoundEffect(resourcePath));
			DebugUtility.printMessage("Loaded clip: " + clipName);
		} catch (Exception e) {
			// unable to load a clip is a non-fatal error
			DebugUtility.printError("Unable to load clip " + clipName + ".\n" + e.getMessage());
		}
	}

	/**
	 * Load a sound effect if it hasn't already been loaded, then play it
	 * 
	 * @param effect
	 *            - the sound effect to play
	 */
	public void playClip(SOUND_EFFECT effect) {
		// check if the sound has been loaded
		if (!this.availableClips.containsKey(effect.getValue())) {
			// if not, load it
			loadClip(effect.getValue());
		}

		// get the clip from the loaded clips
		SoundEffect clip = this.availableClips.get(effect.getValue());

		// null-safe play it
		if (clip != null) {
			DebugUtility.printMessage("Playing clip...");
			clip.play();
		} else {
			DebugUtility.printMessage("Error playing clip: clip is null");
		}
	}

	/**
	 * Representation of a sound effect sounds that get played by the Juke Box
	 */
	private class SoundEffect implements LineListener {
		/**
		 * SoundEffect wraps a clip
		 */
		private Clip m_clip;

		/**
		 * Construct a sound effect from a given file
		 * 
		 * @param resourcePath
		 *            - the path to the audio resource
		 * @throws Exception
		 */
		public SoundEffect(String resourcePath) {
			// create the necessary steps to make a new Clip
			InputStream resourceStream = JukeBox.class.getResourceAsStream(resourcePath);
			AudioInputStream ais = null;
			try {
				ais = AudioSystem.getAudioInputStream(resourceStream);
			} catch (UnsupportedAudioFileException e) {
				DebugUtility.printError("Unsupported audio file: " + resourcePath);
			} catch (IOException e) {
				DebugUtility.printError("Unable to read: " + resourcePath);
			}
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());

			// store the clip internal to this class
			try {
				this.m_clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e) {
				DebugUtility.printError("Error reading: " + resourcePath);
			}
			this.m_clip.addLineListener(this);
		}

		/**
		 * Implementation of the LineListener method
		 * 
		 * @param event
		 *            - a line event that triggers an update
		 */
		public void update(LineEvent event) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				this.m_clip.stop();
				this.m_clip.setFramePosition(0);
			}
		}

		/**
		 * Play the loaded sound effect
		 */
		public void play() {
			this.m_clip.start();
		}
	}
}