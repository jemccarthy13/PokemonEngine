package model;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * A structure to manipulate and update game time
 */
public class GameTime implements Serializable {

	private static final long serialVersionUID = 6901548663761911865L;

	private static GameTime instance = new GameTime();

	/**
	 * The total number of hours played
	 */
	private int bankedHours = 0;
	/**
	 * The total number of minutes played
	 */
	private int bankedMinutes = 0;
	/**
	 * The total number of seconds played
	 */
	private int bankedSeconds = 0;

	/**
	 * The number of hours played in the current session
	 */
	private int sessionHours = 0;
	/**
	 * The number of minutes played in the current session
	 */
	private int sessionMinutes = 0;
	/**
	 * The number of seconds played in the current session
	 */
	private int sessionSeconds = 0;

	/**
	 * The time that this session started
	 */
	private long timeStarted = 0;

	/**
	 * Initialize a new GameTime instance with the given values
	 * 
	 * @param hours
	 *            - the number of total hours to start at
	 * @param minutes
	 *            - the number of total minutes to start at
	 * @param seconds
	 *            - the number of total seconds to start at
	 */
	private GameTime() {}

	public static GameTime getInstance() {
		return instance;
	}

	public void setBankedTime(int hours, int minutes, int seconds) {
		this.bankedHours = hours;
		this.bankedMinutes = minutes;
		this.bankedSeconds = seconds;
	}

	/**
	 * Bank the total time played and store for next session. This function handles
	 * carry-over where session time puts the total number of minutes or seconds
	 * over 60
	 */
	public void saveTime() {
		this.bankedSeconds += this.sessionSeconds;
		if (this.bankedSeconds > 60) {
			this.bankedMinutes += this.bankedSeconds / 60;
			this.bankedSeconds = 60 - (this.bankedSeconds / 60);
		}
		this.bankedMinutes += this.sessionMinutes;
		if (this.bankedMinutes > 60) {
			this.bankedHours += (this.bankedMinutes / 60);
			this.bankedMinutes = 60 - (this.bankedMinutes / 60);
		}
		this.bankedHours += this.sessionHours;
		this.timeStarted = System.currentTimeMillis();
	}

	/**
	 * Update the session's hours, minutes, seconds based on current time
	 */
	public void updateTime() {
		this.sessionSeconds = (int) ((System.currentTimeMillis() - this.timeStarted) / 1000L);
		this.sessionHours = this.sessionSeconds / 3600;
		this.sessionMinutes = (this.sessionSeconds - (this.sessionHours * 3600)) / 60;
		this.sessionSeconds = (this.sessionSeconds - this.sessionHours * 3600 - this.sessionMinutes * 60);
	}

	/**
	 * Formats the current total time (banked + session time) into a readable format
	 * 
	 * @return a string representing total time played
	 */
	public String formatTime() {

		// add session time to banked time
		int hours = this.sessionHours + this.bankedHours;
		int minutes = this.sessionMinutes + this.bankedMinutes;
		int seconds = this.sessionSeconds + this.bankedSeconds;

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
		return df.format(hours) + ":" + df.format(minutes) + ":" + df.format(seconds);
	}

	public void start() {
		this.timeStarted = System.currentTimeMillis();
	}
}