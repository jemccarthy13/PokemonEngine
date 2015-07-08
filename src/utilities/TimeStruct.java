package utilities;

import java.io.Serializable;
import java.text.DecimalFormat;

// ////////////////////////////////////////////////////////////////////////
//
// TimeStruct - easier way to manipulate and update system time
//
// ////////////////////////////////////////////////////////////////////////
public class TimeStruct implements Serializable {

	private static final long serialVersionUID = 6901548663761911865L;

	public int seconds_total = 0, hours_total = 0, minutes_total = 0;
	public int sessionSeconds = 0, sessionMinutes = 0, sessionHours = 0;

	public long timeStarted = 0;

	// ////////////////////////////////////////////////////////////////////////
	//
	// default constructor
	//
	// ////////////////////////////////////////////////////////////////////////
	public TimeStruct() {}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Initialization constructor
	//
	// ////////////////////////////////////////////////////////////////////////
	public TimeStruct(int hours, int minutes, int seconds) {
		hours_total = hours;
		minutes_total = minutes;
		seconds_total = seconds;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Bank the total time played and store for next session
	//
	// ////////////////////////////////////////////////////////////////////////
	public void saveTime() {
		seconds_total += sessionSeconds;
		if (seconds_total > 60) {
			minutes_total += seconds_total / 60;
			seconds_total = 60 - (seconds_total / 60);
		}
		minutes_total += sessionMinutes;
		if (minutes_total > 60) {
			hours_total += (minutes_total / 60);
			minutes_total = 60 - (minutes_total / 60);
		}
		hours_total += sessionHours;
		timeStarted = System.currentTimeMillis();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Update the hours, minutes, seconds played based on current time. Add
	// to running time bank. Time bank allows saved games to be adjusted by
	// adding current session runtime to banked total.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void updateTime() {
		// get the time difference in hours, mins, seconds
		sessionSeconds = (((int) ((System.currentTimeMillis() - timeStarted) / 1000L)));
		sessionHours = sessionSeconds / 3600;
		sessionMinutes = (sessionSeconds - (sessionHours * 3600)) / 60;

		sessionSeconds = (sessionSeconds - sessionHours * 3600 - sessionMinutes * 60);

		// do math to add current session time to total banked time

	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatTime - formats the system time into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatTime() {

		int hours = 0, minutes = 0, seconds = 0;

		seconds = sessionSeconds + seconds_total;
		minutes = sessionMinutes + minutes_total;
		hours = sessionHours + hours_total;

		// do math to add current session time to total banked time
		if (seconds > 60) {
			minutes += seconds / 60;
			seconds = 60 - (seconds / 60);
		}
		if (minutes > 60) {
			hours += (minutes / 60);
			minutes = 60 - (minutes / 60);
		}
		DecimalFormat df = new DecimalFormat("00");
		return df.format(hours) + ": " + df.format(minutes) + ": " + df.format(seconds);
	}
}