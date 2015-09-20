package utilities;

/**
 * Logging wrapper around Syso / syserr
 */
public class DebugUtility {

	/**
	 * The max length of debug messages
	 * 
	 * @NOTE: not strictly enforced except for debug header
	 */
	static int MESSAGE_LENGTH = 80;

	/**
	 * A description of possible logging levels
	 */
	public enum DEBUG_LEVEL {
		/**
		 * Debug logging
		 */
		DEBUG,
		/**
		 * Error logging
		 */
		ERROR,
		/**
		 * No logging
		 */
		OFF
	}

	/**
	 * Set the DEBUG_LEVEL
	 */
	private static DEBUG_LEVEL level = DEBUG_LEVEL.DEBUG;

	/**
	 * Set the current debug level
	 * 
	 * @param lvl
	 *            - the level to set to
	 */
	public static void setLevel(DEBUG_LEVEL lvl) {
		level = lvl;
	}

	/**
	 * Print a formatted debug header section
	 * 
	 * @param header
	 *            - the message to print
	 */
	public static void printHeader(String header) {
		if (level != DEBUG_LEVEL.OFF && level != DEBUG_LEVEL.ERROR) {
			int banner = (MESSAGE_LENGTH - (header.length() + 2)) / 2;

			StringBuilder b = new StringBuilder();
			for (int x = 0; x < banner; x++) {
				b.append("=");
			}
			b.append(" " + header + " ");

			for (int x = 0; x < banner; x++) {
				b.append("=");
			}

			System.out.print(b.toString());
			for (int x = 0; x < (MESSAGE_LENGTH - b.toString().length()); x++) {
				System.out.print("=");
			}
			System.out.println();
		}
	}

	/**
	 * Print the given message on a new line
	 * 
	 * @param string
	 *            - the message to print
	 */
	public static void printMessage(String string) {
		if (level != DEBUG_LEVEL.OFF && level != DEBUG_LEVEL.ERROR) {
			System.out.println("* " + string);
		}
	}

	/**
	 * Print a new line
	 */
	public static void printMessage() {
		if (level != DEBUG_LEVEL.OFF && level != DEBUG_LEVEL.ERROR) {
			System.out.println("* ");
		}
	}

	/**
	 * Print a message without a new line
	 * 
	 * @param string
	 *            - the message to print
	 */
	public static void print(String string) {
		if (level != DEBUG_LEVEL.OFF && level != DEBUG_LEVEL.ERROR) {
			System.out.print(string);
		}
	}

	/**
	 * Print an error without exiting
	 * 
	 * @param string
	 *            - the message to print
	 */
	public static void printError(String string) {
		if (level != DEBUG_LEVEL.OFF) {
			System.err.println("* " + string);
		}
	}

	/**
	 * Print an error and exit
	 * 
	 * @param string
	 *            - the message to print
	 */
	public static void error(String string) {
		if (level != DEBUG_LEVEL.OFF) {
			System.err.println("* " + string);
			System.exit(-1);
		}
	}

	/**
	 * Wrapper around quitting the JVM
	 */
	public static void quit() {
		System.exit(0);
	}
}
