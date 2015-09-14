package model;

import java.io.Serializable;
import java.text.DecimalFormat;

// ////////////////////////////////////////////////////////////////////////
//
// Easier way to manipulate and update game time
//
// ////////////////////////////////////////////////////////////////////////
public class GameTime implements Serializable {

	private static final long serialVersionUID = 6901548663761911865L;

	// Keep track of total time, and time for this game session
	public int bankedHours = 0, bankedMinutes = 0, bankedSeconds = 0;
	public int sessionHours = 0, sessionMinutes = 0, sessionSeconds = 0;

	// the time that this session started
	public long timeStarted = 0;

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initialization constructor
	//
	// ////////////////////////////////////////////////////////////////////////
	public GameTime(int hours, int minutes, int seconds) {
		bankedHours = hours;
		bankedMinutes = minutes;
		bankedSeconds = seconds;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Bank the total time played and store for next session
	//
	// ////////////////////////////////////////////////////////////////////////
	public void saveTime() {
		bankedSeconds += sessionSeconds;
		if (bankedSeconds > 60) {
			bankedMinutes += bankedSeconds / 60;
			bankedSeconds = 60 - (bankedSeconds / 60);
		}
		bankedMinutes += sessionMinutes;
		if (bankedMinutes > 60) {
			bankedHours += (bankedMinutes / 60);
			bankedMinutes = 60 - (bankedMinutes / 60);
		}
		bankedHours += sessionHours;
		timeStarted = System.currentTimeMillis();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Update the session's hours, minutes, seconds based on current time
	//
	// ////////////////////////////////////////////////////////////////////////
	public void updateTime() {
		sessionSeconds = (int) ((System.currentTimeMillis() - timeStarted) / 1000L);
		sessionHours = sessionSeconds / 3600;
		sessionMinutes = (sessionSeconds - (sessionHours * 3600)) / 60;
		sessionSeconds = (sessionSeconds - sessionHours * 3600 - sessionMinutes * 60);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Formats the current total time played into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatTime() {

		// add session time to banked time
		int hours = sessionHours + bankedHours;
		int minutes = sessionMinutes + bankedMinutes;
		int seconds = sessionSeconds + bankedSeconds;

		// add any overlap to the next category
		// i.e seconds = 70, then that's +1 minute, 10 seconds
		if (seconds > 60) {
			minutes += seconds / 60;
			seconds = 60 - (seconds / 60);
		}

		// then check if the new minutes is over an hour
		if (minutes > 60) {
			hours += (minutes / 60);
			minutes = 60 - (minutes / 60);
		}
		DecimalFormat df = new DecimalFormat("00");
		return df.format(hours) + ": " + df.format(minutes) + ": " + df.format(seconds);
	}
}