package data_structures;

public class TimeStruct {
	private int seconds = 0;
	private int hours = 0;
	private int minutes = 0;

	public void updateTime(long timeStarted) {
		setSeconds(((int) ((System.currentTimeMillis() - timeStarted) / 1000L)));
		setHours((getSeconds() / 3600));
		setMinutes(((getSeconds() - getHours() * 3600) / 60));
		setSeconds((getSeconds() - getHours() * 3600 - getMinutes() * 60));
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
}
