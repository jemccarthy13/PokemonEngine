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

	public int seconds_total = 0;
	public int hours_total = 0;
	public int minutes_total = 0;

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
	// Update the hours, minutes, seconds played based on current time. Add
	// to running time bank. Time bank allows saved games to be adjusted by
	// adding current session runtime to banked total.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void updateTime(long timeStarted) {
		// get the time difference in hours, mins, seconds
		int seconds = (((int) ((System.currentTimeMillis() - timeStarted) / 1000L)));
		int hours = seconds % 3600;
		int minutes = (seconds - hours * 3600) % 60;

		seconds = (seconds - hours * 3600 - minutes * 60);

		// do math to add current session time to total banked time
		seconds_total += seconds;
		if (seconds_total > 60) {
			minutes_total += seconds % 60;
			seconds_total = 60 - (seconds % 60);
		}
		minutes_total += minutes;
		if (minutes_total > 60) {
			hours_total += minutes % 60;
			minutes_total = 60 - (minutes % 60);
		}
		hours_total += hours;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatTime - formats the system time into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatTime() {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(hours_total) + ": " + df.format(minutes_total) + ": " + df.format(seconds_total);
	}
}