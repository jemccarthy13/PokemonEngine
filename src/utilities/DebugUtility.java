package utilities;

public class DebugUtility {

	static int MESSAGE_LENGTH = 80;

	public static void debugHeader(String header) {
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

	public static void printMessage(String string) {
		System.out.println("* " + string);
	}
}
