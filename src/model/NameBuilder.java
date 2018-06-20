package model;

/**
 * Underlying data storage that helps to build a name
 */
public class NameBuilder {

	private static NameBuilder instance = new NameBuilder();

	// name that grows and shrinks dynamically according to user's choices
	private StringBuilder buildName = new StringBuilder();

	// the name screen layout makes it easer to get selected characters
	private char[][] charArray = { { 'A', 'B', 'C', 'D', 'E', 'F' }, { 'G', 'H', 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X' }, { 'Y', 'Z', ' ', '!', '?', '.' } };

	// the name of the object to be named
	private String toBeNamed;

	public static NameBuilder getInstance() {
		return instance;
	}

	/**
	 * @return the object currently being named
	 */
	public String getToBeNamed() {
		return this.toBeNamed;
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
		return this.charArray.length;
	}

	/**
	 * Retrieve the maximum number of columns
	 * 
	 * @return max columns
	 */
	public int maxCols() {
		return this.charArray[0].length;
	}

	/**
	 * Based on the current row / column, add the selected character to the name
	 * buffer
	 * 
	 * @param selection
	 *            current row / column
	 */
	public void addSelectedChar(Coordinate selection) {
		if (this.buildName.length() < Configuration.MAX_NAME_SIZE) {
			this.buildName.append(this.charArray[selection.getX()][selection.getY()]);
		}
	}

	/**
	 * Get what is currently in the name buffer
	 * 
	 * @return name string
	 */
	@Override
	public String toString() {
		return this.buildName.toString();
	}

	/**
	 * Remove the last character from the name buffer
	 */
	public void removeChar() {
		int size = this.buildName.length();
		if (size > 0) {
			this.buildName.deleteCharAt(size - 1);
		}
	}

	/**
	 * Delete all characters from the name buffer
	 */
	public void reset() {
		int size = this.buildName.length();
		this.buildName.delete(0, size);
	}
}
