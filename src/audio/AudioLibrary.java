package audio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import model.Configuration;
import utilities.DebugUtility;
import utilities.RandomNumUtils;

// ////////////////////////////////////////////////////////////////////////
//
// AudioLibrary intializes variables to be used as constants in playing
// music from this library.
//
// ////////////////////////////////////////////////////////////////////////
public class AudioLibrary {

	private static AudioTrack currentTrack = null;
	private static JukeBox jukeBox = new JukeBox();

	private static ArrayList<String> encounterTrackNames = new ArrayList<String>();;
	private static HashMap<String, AudioTrack> musicTracks = new HashMap<String, AudioTrack>();

	public enum SOUND_EFFECT {
		SELECT("Select"), DAMAGE("Damage"), COLLISION("Collision"), MENU("Menu");

		private String value;

		SOUND_EFFECT(String v) {
			value = v;
		}

		String getValue() {
			return value;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initialize some variables for the audio library
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Given a midi track title, play the associated midi file
	//
	// ////////////////////////////////////////////////////////////////////////
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

		// switch to the next track and play, if the track is valid
		currentTrack = musicTracks.get(songTitle);
		if (currentTrack != null) {
			currentTrack.start();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pick a random enemy encounter track
	//
	// ////////////////////////////////////////////////////////////////////////
	public void pickTrainerMusic() {
		if (encounterTrackNames.size() > 0) {
			int choice = RandomNumUtils.generateRandom(encounterTrackNames.size(), 0);
			playBackgroundMusic(encounterTrackNames.get(choice));
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Pauses the current music track, typically to play another song
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void pauseBackgroundMusic() {
		if (currentTrack != null) {
			currentTrack.stop();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Play the given sound effect
	//
	// ////////////////////////////////////////////////////////////////////////
	public static void playClip(SOUND_EFFECT effect) {
		jukeBox.playClip(effect);
	}
}