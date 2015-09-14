package audio;

import java.io.InputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import model.Configuration;
import utilities.DebugUtility;
import audio.AudioLibrary.SOUND_EFFECT;

// ////////////////////////////////////////////////////////////////////////
//
// Jukebox - handles playing clips
//
// ////////////////////////////////////////////////////////////////////////
public class JukeBox {

	// saved clips that have already been loaded
	private HashMap<String, SoundEffect> availableClips = new HashMap<String, SoundEffect>();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Loads a given audio sound effect file and maps to given sound name
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Play a sound effect; load first if necessary
	//
	// ////////////////////////////////////////////////////////////////////////
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
			clip.play();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Class for sound effect sounds that get played by the Juke Box
	//
	// ////////////////////////////////////////////////////////////////////////
	private class SoundEffect implements LineListener {
		private Clip m_clip;

		// ////////////////////////////////////////////////////////////////////////
		//
		// Construct a sound effect from a given file
		//
		// ////////////////////////////////////////////////////////////////////////
		public SoundEffect(String resourcePath) throws Exception {
			// create the necessary steps to make a new Clip
			InputStream resourceStream = JukeBox.class.getResourceAsStream(resourcePath);
			AudioInputStream ais = AudioSystem.getAudioInputStream(resourceStream);
			DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());

			// store the clip internal to this class
			this.m_clip = (Clip) AudioSystem.getLine(info);
			this.m_clip.addLineListener(this);
		}

		// ////////////////////////////////////////////////////////////////////////
		//
		// LineListener necessary method
		//
		// ////////////////////////////////////////////////////////////////////////
		public void update(LineEvent event) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				this.m_clip.stop();
				this.m_clip.setFramePosition(0);
			}
		}

		// ////////////////////////////////////////////////////////////////////////
		//
		// Play the loaded sound effect
		//
		// ////////////////////////////////////////////////////////////////////////
		public void play() {
			this.m_clip.start();
		}
	}
}