package model;

/**
 * Underlying data storage that helps to build a name
 */
public class NameBuilder {
	// name that grows and shrinks dynamically according to user's choices
	private StringBuilder buildName = new StringBuilder();

	// the name screen layout makes it easer to get selected characters
	private char[][] charArray = { { 'A', 'B', 'C', 'D', 'E', 'F' }, { 'G', 'H', 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X' }, { 'Y', 'Z', ' ', '!', '?', '.' } };

	// the name of the object to be named
	private String toBeNamed;

	/**
	 * @return the object currently being named
	 */
	public String getToBeNamed() {
		return toBeNamed;
	}

	/**
	 * Set the object currently being named
	 * 
	 * @param toBeNamed
	 *            - the object to be named
	 */
	public void setToBeNamed(String toBeNamed) {
		this.toBeNamed = toBeNamed;
	}

	/**
	 * Retrieve the maximum number of rows
	 * 
	 * @return max number of rows
	 */
	public int maxRows() {
		return charArray.length;
	}

	/**
	 * Retrieve the maximum number of columns
	 * 
	 * @return max columns
	 */
	public int maxCols() {
		return charArray[0].length;
	}

	/**
	 * Based on the current row / column, add the selected character to the name
	 * buffer
	 * 
	 * @param selection
	 *            current row / column
	 */
	public void addSelectedChar(Coordinate selection) {
		if (buildName.length() < Configuration.MAX_NAME_SIZE) {
			buildName.append(charArray[selection.getX()][selection.getY()]);
		}
	}

	/**
	 * Get what is currently in the name buffer
	 * 
	 * @return name string
	 */
	public String toString() {
		return buildName.toString();
	}

	/**
	 * Remove the last character from the name buffer
	 */
	public void removeChar() {
		int size = buildName.length();
		if (size > 0) {
			buildName.deleteCharAt(size - 1);
		}
	}

	/**
	 * Delete all characters from the name buffer
	 */
	public void reset() {
		int size = buildName.length();
		buildName.delete(0, size);
	}
}
