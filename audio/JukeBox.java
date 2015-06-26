package audio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

// ////////////////////////////////////////////////////////////////////////
//
// Jukebox - handles playing clips
//
// ////////////////////////////////////////////////////////////////////////
public class JukeBox {
	private BackgroundSound bs;
	private HashMap<String, LinkedList<Sound>> availableClips;
	private HashMap<Integer, Sound> playingClips;
	private int nextClip = 0;
	private boolean debug;

	public JukeBox() {
		this.availableClips = new HashMap<String, LinkedList<JukeBox.Sound>>();
		this.playingClips = new HashMap<Integer, Sound>();
	}

	public void setDebug(boolean b) {
		this.debug = b;
	}

	public boolean loadClip(String resourcePath, String soundName, int howMany) {
		for (int x = 0; x < howMany; x++) {
			try {
				InputStream is = JukeBox.class.getResourceAsStream(resourcePath);
				URL item = JukeBox.class.getResource(resourcePath);
				if (is == null) {
					System.out.println("Can't find sound");
					return false;
				}
				boolean loaded = loadClip(is, soundName, item);
				if (!loaded) {
					System.out.println("Can't load sound");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private boolean loadClip(InputStream is, String name, URL item) {
		name = trimExtension(name);
		if (!this.availableClips.containsKey(name)) {
			this.availableClips.put(name, new LinkedList<Sound>());
		}
		List<Sound> list = (List<Sound>) this.availableClips.get(name);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (audioInputStream == null) {
			return false;
		}
		AudioFormat format = audioInputStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Sound clip = null;
		try {
			clip = new Sound((Clip) AudioSystem.getLine(info), audioInputStream, name, this.nextClip++, this);
			list.add(clip);

			audioInputStream.close();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static String trimExtension(String name) {
		int last = name.lastIndexOf(".");
		if (last == 0) {
			throw new IllegalArgumentException("can't start a name with a dot");
		}
		if (last > -1) {
			return name.substring(0, last);
		}
		return name;
	}

	public void playClip(String name, boolean option_sound) {
		if (option_sound)
			playClip(name, 1);
	}

	public synchronized void playClip(String name, int numberOfLoops) {
		name = trimExtension(name);
		if (!this.availableClips.containsKey(name)) {
			return;
		}
		List<?> clips = (List<Sound>) this.availableClips.get(name);

		if (clips.isEmpty()) {
			return;
		}

		Sound clip = (Sound) clips.remove(0);
		this.playingClips.put(new Integer(clip.getID()), clip);

		if (numberOfLoops == 1) {
			clip.play();
		} else {
			clip.loop(numberOfLoops);
		}

		return;
	}

	public static int getSoundLength(InputStream is) {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(is);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ais == null) {
			return -1;
		}
		long frames = ais.getFrameLength();

		AudioFormat format = ais.getFormat();

		float framesPerSecond = format.getFrameRate();

		int seconds = (int) ((float) frames / framesPerSecond);

		return seconds;
	}

	public synchronized BackgroundSound playBackground(InputStream is) {
		if (this.bs != null) {
			this.bs.stopBackgroundSound(false);
		}
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(is);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ais == null) {
			return null;
		}
		AudioFormat format = ais.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		SourceDataLine background = null;
		try {
			background = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		try {
			background.open();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		background.start();

		this.bs = new BackgroundSound(ais, background);
		return this.bs;
	}

	public void stopCurrentBackgroundSound(boolean noMoreSounds) {
		if (this.bs != null) {
			this.bs.stopBackgroundSound(noMoreSounds);
		}
	}

	public synchronized void makeAvailable(Sound sound) {
		if (this.availableClips.containsKey(sound.getName())) {
			this.playingClips.remove(new Integer(sound.getID()));
			((List<Sound>) this.availableClips.get(sound.getName())).add(sound);
		}
	}

	public synchronized void stopClip(int id) {
		Integer ID = new Integer(id);
		if (this.playingClips.containsKey(ID)) {
			Sound s = (Sound) this.playingClips.get(ID);
			s.stop();
		}
	}

	public synchronized void stopAllClips() {
		Iterator<Integer> it = this.playingClips.keySet().iterator();
		while (it.hasNext()) {
			Sound s = (Sound) this.playingClips.get((Integer) it.next());
			s.stop();
		}
	}

	public synchronized void close() {
		stopAllClips();
		stopCurrentBackgroundSound(true);
	}

	public class Sound implements LineListener {
		private Clip m_clip;
		private String name;
		private int id;
		private boolean looping = false;
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

		public String getName() {
			return this.name;
		}

		public void update(LineEvent event) {
			if (event.getType().equals(LineEvent.Type.STOP)) {
				if (this.looping) {
					return;
				}
				this.m_clip.stop();
				this.m_clip.setFramePosition(0);
				this.jukeBox.makeAvailable(this);
			}
		}

		public void play() {
			this.m_clip.start();
		}

		public void loop(int numberOfLoops) {
			this.m_clip.setLoopPoints(0, -1);
			this.looping = true;
			this.m_clip.loop(numberOfLoops);
		}

		public int getID() {
			return this.id;
		}

		public void stop() {
			this.looping = false;
			this.m_clip.stop();
		}
	}

	public static abstract interface BackgroundSoundObserver {
		public abstract void soundDone(JukeBox.BackgroundSound paramBackgroundSound, boolean paramBoolean);
	}

	public class BackgroundSound extends Thread {
		private AudioInputStream ais;
		private SourceDataLine sdl;
		private boolean done;
		private boolean kill;
		JukeBox.BackgroundSoundObserver bso;

		public BackgroundSound(AudioInputStream ais, SourceDataLine sdl) {
			this(ais, sdl, null);
		}

		public BackgroundSound(AudioInputStream ais, SourceDataLine sdl, JukeBox.BackgroundSoundObserver bso) {
			this.ais = ais;
			this.sdl = sdl;
			this.bso = bso;
			this.done = false;
			start();
		}

		public void run() {
			boolean allBytesRead = false;
			int nBytesRead;
			while (!done) {
				nBytesRead = 0;
				byte[] abData = new byte[2048];
				try {
					nBytesRead = this.ais.read(abData, 0, abData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (nBytesRead >= 0) {
					this.sdl.write(abData, 0, nBytesRead);
				} else {
					this.done = true;
					allBytesRead = true;
				}
			}
			this.sdl.drain();
			this.sdl.stop();
			this.sdl.close();
			if ((!this.kill) && (this.bso != null)) {
				this.bso.soundDone(this, allBytesRead);
			}
		}

		public void registerObserver(JukeBox.BackgroundSoundObserver bso) {
			this.bso = bso;
		}

		public void stopBackgroundSound(boolean noMoreSounds) {
			if (this.done) {
				return;
			}
			this.done = true;
			this.kill = noMoreSounds;
		}
	}
}