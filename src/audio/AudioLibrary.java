package audio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// AudioLibrary intializes variables to be used as constants in playing
// music from this library.
//
// ////////////////////////////////////////////////////////////////////////
public class AudioLibrary {

	private static AudioLibrary m_audioLib = new AudioLibrary();

	private static final String bgMusicPath = "resources/audio_lib/BGM/";
	public static final String soundEffectsPath = "resources/audio_lib/SE/";

	private MidiPlayer m_currentTrack = null;
	private JukeBox m_JukeBox = new JukeBox();

	private ArrayList<String> m_encounterTracks;
	private HashMap<String, MidiPlayer> m_trackList;

	public String SE_SELECT = "Select";
	public String SE_DAMAGE = "Damage";
	public String SE_COLLISION = "Collision";
	public String SE_MENU = "Menu";

	// ////////////////////////////////////////////////////////////////////////
	//
	// Load the audio library from disk
	//
	// ////////////////////////////////////////////////////////////////////////
	private AudioLibrary() {
		System.err.println("Loading audio library...");
		m_encounterTracks = new ArrayList<String>();
		m_trackList = new HashMap<String, MidiPlayer>();

		File[] listOfFiles = new File(bgMusicPath).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				MidiPlayer musicTrack = new MidiPlayer(bgMusicPath + listOfFiles[i].getName(), true);

				// compile a list of trainer / encounter music
				Pattern p = Pattern.compile("Encounter");
				if (p.matcher(listOfFiles[i].getName()).matches()) {
					m_encounterTracks.add(listOfFiles[i].getName().replace(".mid", ""));
				}

				// strip out the .mid in the track title for adding to the list
				// also strip out the Location portion if the track is for a
				// location
				m_trackList.put(listOfFiles[i].getName().replace("Location", "").replace(".mid", ""), musicTrack);
			}
		}

		System.err.println("Loaded primary music files.");

		listOfFiles = new File(soundEffectsPath).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				m_JukeBox.loadClip(new String(soundEffectsPath + listOfFiles[i].getName()).replace("resources", ""),
						listOfFiles[i].getName().replace(".wav", ""), 1);
			}
		}
		System.err.println("Loaded primary sound effect files.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Singleton implementation - initialize the audio library once at startup
	//
	// ////////////////////////////////////////////////////////////////////////
	public static AudioLibrary getInstance() {
		return m_audioLib;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Given a midi track title, play the associated midi file
	//
	// ////////////////////////////////////////////////////////////////////////
	public void playBackgroundMusic(String songTitle) {

		// stop the current track, if playing
		if (m_currentTrack != null) {
			m_currentTrack.stop();
			m_currentTrack = null;
		}

		// switch to the next track and play, if the track is valid
		if (m_trackList.containsKey(songTitle)) {
			m_currentTrack = getInstance().m_trackList.get(songTitle);
			if (m_currentTrack != null) {
				m_currentTrack.start();
			}
		} else {
			System.err.println("Can't play " + songTitle);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pick a random enemy encounter track
	//
	// TODO - Looks like no encounter tracks are loaded
	//
	// ////////////////////////////////////////////////////////////////////////
	public void pickTrainerMusic() {
		if (m_encounterTracks.size() > 0) {
			int choice = RandomNumUtils.generateRandom(m_encounterTracks.size(), 0);
			String songTitle = m_encounterTracks.get(choice);
			this.playBackgroundMusic(songTitle);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pauses the current music track, typically to
	// interrupt the music thread with another song
	//
	// ////////////////////////////////////////////////////////////////////////
	public void pauseBackgrondMusic() {
		if (m_currentTrack != null) {
			m_currentTrack.stop();
		}
	}

	public void playClip(String string, boolean option_sound) {
		m_JukeBox.playClip(string, option_sound);
	}
}