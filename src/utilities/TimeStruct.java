package utilities;

import java.io.Serializable;
import java.text.DecimalFormat;

// ////////////////////////////////////////////////////////////////////////
//
// TimeStruct - easier way to manipulate and update system time 
//
// TODO will need to adjust for saved/loaded games - running total + offset
//
// ////////////////////////////////////////////////////////////////////////
public class TimeStruct implements Serializable {

	private static final long serialVersionUID = 6901548663761911865L;

	public int seconds = 0;
	public int hours = 0;
	public int minutes = 0;

	public void updateTime(long timeStarted) {
		seconds = (((int) ((System.currentTimeMillis() - timeStarted) / 1000L)));
		hours = seconds / 3600;
		minutes = (seconds - hours * 3600) / 60;
		seconds = (seconds - hours * 3600 - minutes * 60);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// formatTime - formats the system time into a readable format
	//
	// ////////////////////////////////////////////////////////////////////////
	public String formatTime() {
		DecimalFormat df = new DecimalFormat("00");
		return df.format(hours) + ": " + df.format(minutes) + ": " + df.format(seconds);
	}
}
