package audio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import model.Configuration;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

/**
 * AudioLibrary initializes variables to be used as constants in playing music
 * from this library.
 */
public class AudioLibrary {

	/**
	 * The current track queued to be played
	 */
	private static AudioTrack currentTrack = null;
	/**
	 * JukeBox utility that plays sound effects
	 */
	private static JukeBox jukeBox = new JukeBox();

	/**
	 * An ArrayList of available encounter track names
	 */
	private static ArrayList<String> encounterTrackNames = new ArrayList<String>();
	/**
	 * An ArrayList of the tracks that have been loaded already
	 */
	private static HashMap<String, AudioTrack> musicTracks = new HashMap<String, AudioTrack>();

	/**
	 * An enumeration for sound effects
	 */
	public enum SOUND_EFFECT {
		/**
		 * The button press sound effect
		 */
		SELECT("Select"),
		/**
		 * The damage sound effect
		 */
		DAMAGE("Damage"),
		/**
		 * The collision sound effect
		 */
		COLLISION("Collision"),
		/**
		 * The menu sound effect
		 */
		MENU("Menu");

		/**
		 * The current value of this enumeration
		 */
		private String value;

		/**
		 * Enumerator constructor
		 * 
		 * @param v
		 *            - the value of the enumerated value
		 */
		SOUND_EFFECT(String v) {
			value = v;
		}

		/**
		 * Get the current value of the enumeration
		 * 
		 * @return value of enumeration
		 */
		String getValue() {
			return value;
		}
	}

	/**
	 * Constructor to initialize the audio library
	 */
	public AudioLibrary() {
		DebugUtility.printHeader("Audio");
		DebugUtility.printMessage("Initializing audio library...");

		// create a list of encounter tracks
		for (File file : new File(Configuration.MUSIC_PATH).listFiles()) {
			if (file.isFile()) {
				String name_of_file = file.getName();
				Pattern p = Pattern.compile("^Encounter.*");
				if (p.matcher(name_of_file).matches()) {
					encounterTrackNames.add(name_of_file.replace(".mid", ""));
				}
			}
		}

		DebugUtility.printMessage("Enounter Tracks loaded.");
	}

	/**
	 * Play a given music track
	 * 
	 * @param songTitle
	 *            - the song to play
	 */
	public void playBackgroundMusic(String songTitle) {
		// stop the current track, if playing
		if (currentTrack != null) {
			currentTrack.stop();
		}

		// if the track isn't loaded yet, load it from the disk
		if (!musicTracks.containsKey(songTitle)) {
			musicTracks.put(songTitle, new AudioTrack(songTitle));
			DebugUtility.printMessage("Dynamically loaded " + songTitle);
		}

		// change to the next track and play, if the track is valid
		currentTrack = musicTracks.get(songTitle);
		if (currentTrack != null) {
			currentTrack.start();
		}
	}

	/**
	 * Pick and play a random encounter track
	 */
	public void pickTrainerMusic() {
		if (encounterTrackNames.size() > 0) {
			int choice = RandomNumUtils.generateRandom(encounterTrackNames.size(), 0);
			playBackgroundMusic(encounterTrackNames.get(choice));
		}
	}

	/**
	 * Pauses the current track
	 */
	public static void pauseBackgroundMusic() {
		if (currentTrack != null) {
			currentTrack.stop();
		}
	}

	/**
	 * Play the given sound effect clip
	 * 
	 * @param effect
	 *            - the sound effect to play
	 */
	public static void playClip(SOUND_EFFECT effect) {
		jukeBox.playClip(effect);
	}
}