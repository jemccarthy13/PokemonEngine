package utilities;

import java.text.DecimalFormat;

// ////////////////////////////////////////////////////////////////////////
//
// TimeStruct - easier way to manipulate and update system time 
//
// TODO will need to adjust for saved/loaded games - running total + offset
//
// ////////////////////////////////////////////////////////////////////////
@SuppressWarnings("static-access")
public class TimeStruct {
	private static int seconds = 0;
	private static int hours = 0;
	private static int minutes = 0;

	public void updateTime(long timeStarted) {
		setSeconds(((int) ((System.currentTimeMillis() - timeStarted) / 1000L)));
		setHours((getSeconds() / 3600));
		setMinutes(((getSeconds() - getHours() * 3600) / 60));
		setSeconds((getSeconds() - getHours() * 3600 - getMinutes() * 60));
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatTime - formats the system time into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatTime() {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(getHours()) + ": " + df.format(getMinutes()) + ": " + df.format(getSeconds());
	}

	public static int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public static int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public static int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
