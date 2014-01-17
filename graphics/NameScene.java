package graphics;

import utilities.EnumsAndConstants;

public class NameScene {
	public int rowSelection = 0;
	public int colSelection = 0;

	StringBuilder buildName = new StringBuilder();

	public String toBeNamed;

	public void setToBeNamed(String tbn) {
		toBeNamed = tbn;
	}

	public char[][] charArray = { { 'A', 'B', 'C', 'D', 'E', 'F' }, { 'G', 'H', 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X' }, { 'Y', 'Z', ' ', '!', '?', '.' } };

	public void addSelectedChar() {
		if (buildName.length() < EnumsAndConstants.MAX_NAME_SIZE) {
			buildName.append(charArray[rowSelection][colSelection]);
		}
	}

	public String getNameSelected() {
		return buildName.toString();
	}

	public void removeChar() {
		int size = buildName.length();
		if (size > 0) {
			buildName.deleteCharAt(size - 1);
		}
	}

	public void reset() {
		int size = buildName.length();
		buildName.delete(0, size);
	}
}
