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

	private static AudioLibrary m_instance = new AudioLibrary();

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
		System.out.println("** Loading audio library...");

		final String bgMusicPath = "resources/audio_lib/BGM/";
		final String soundEffectsPath = "resources/audio_lib/SE/";

		m_encounterTracks = new ArrayList<String>();
		m_trackList = new HashMap<String, MidiPlayer>();

		File[] listOfFiles = new File(bgMusicPath).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {

				String name_of_file = listOfFiles[i].getName();

				MidiPlayer musicTrack = new MidiPlayer(bgMusicPath + name_of_file, true);

				// compile a list of trainer / encounter music
				Pattern p = Pattern.compile("Encounter");
				if (p.matcher(name_of_file).matches()) {
					m_encounterTracks.add(name_of_file.replace(".mid", ""));
				}

				// process out junk parts of the name of the track title for
				// adding to the list
				m_trackList.put(name_of_file.replace("Location", "").replace(".mid", ""), musicTrack);
			}
		}

		System.out.println("** Loaded primary music files.");

		// load all of the sound effect files
		listOfFiles = new File(soundEffectsPath).listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				m_JukeBox.loadClip(soundEffectsPath + listOfFiles[i].getName(),
						listOfFiles[i].getName().replace(".wav", ""));
			}
		}
		System.out.println("** Loaded primary sound effect files.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Singleton implementation - initialize the audio library once at startup
	//
	// ////////////////////////////////////////////////////////////////////////
	public static AudioLibrary getInstance() {
		return m_instance;
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