package audio;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

import utilities.DebugUtility;

// ////////////////////////////////////////////////////////////////////////
//
// Jukebox - handles playing clips
//
// ////////////////////////////////////////////////////////////////////////
public class JukeBox {
	private HashMap<String, LinkedList<Sound>> availableClips;
	private int nextClip = 0;

	public JukeBox() {
		this.availableClips = new HashMap<String, LinkedList<JukeBox.Sound>>();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Loads a given audio sound effect file and maps to given sound name
	//
	// ////////////////////////////////////////////////////////////////////////
	public void loadClip(String resourcePath, String soundName) {
		try {
			InputStream resourceStream = JukeBox.class.getResourceAsStream(resourcePath.replace("resources", ""));
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceStream);

			DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
			Sound clip = null;

			clip = new Sound((Clip) AudioSystem.getLine(info), audioInputStream, soundName, this.nextClip++, this);

			if (this.availableClips.containsKey(soundName)) {
				this.availableClips.get(soundName).add(clip);
			} else {
				LinkedList<Sound> list = new LinkedList<Sound>();
				list.add(clip);
				this.availableClips.put(soundName, list);
			}

			audioInputStream.close();

		} catch (Exception e) {
			DebugUtility.printError("Unable to load clip " + soundName);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Play a sound effect, with optional master sound option tag
	//
	// ////////////////////////////////////////////////////////////////////////
	public synchronized void playClip(String name) {
		if (!this.availableClips.containsKey(name)) {
			DebugUtility.printError("Cannot play sound " + name);
			return;
		}
		List<?> clips = (List<Sound>) this.availableClips.get(name);

		if (clips.isEmpty()) {
			return;
		}

		Sound clip = (Sound) clips.remove(0);

		clip.play();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Class for sound effect sounds that get played by the Juke Box
	//
	// ////////////////////////////////////////////////////////////////////////
	public class Sound implements LineListener {
		private Clip m_clip;
		private String name;
		private int id;
		JukeBox jukeBox;

		public Sound(Clip clip, AudioInputStream ais, String name, int id, JukeBox jukeBox)
				throws LineUnavailableException, IOException {
			this.name = name;
			this.id = id;
			this.m_clip = clip;
			this.m_clip.addLineListener(this);
			this.m_clip.open(ais);
			this.jukeBox = jukeBox;
		}

		public void update(LineEvent event) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				this.m_clip.stop();
				this.m_clip.setFramePosition(0);

				if (this.jukeBox.availableClips.containsKey(this.name)) {
					this.jukeBox.availableClips.remove(new Integer(this.id));
					((List<Sound>) this.jukeBox.availableClips.get(this.name)).add(this);
				}
			}
		}

		public void play() {
			this.m_clip.start();
		}

		public void stop() {
			this.m_clip.stop();
		}
	}
}