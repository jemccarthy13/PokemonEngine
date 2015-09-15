package model;

public class NameBuilder {
	// name that grows and shrinks dynamically according to user's choices
	private StringBuilder buildName = new StringBuilder();

	// character selection information
	private int rowSelection = 0;
	private int colSelection = 0;

	// the name screen layout makes it easer to get selected characters
	private char[][] charArray = { { 'A', 'B', 'C', 'D', 'E', 'F' }, { 'G', 'H', 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X' }, { 'Y', 'Z', ' ', '!', '?', '.' } };

	// the name of the object to be named
	private String toBeNamed;

	public String getToBeNamed() {
		return toBeNamed;
	}

	public void setToBeNamed(String toBeNamed) {
		this.toBeNamed = toBeNamed;
	}

	public int getColSelection() {
		return colSelection;
	}

	public void setColSelection(int colSelection) {
		this.colSelection = colSelection;
	}

	public int getRowSelection() {
		return rowSelection;
	}

	public void setRowSelection(int rowSelection) {
		this.rowSelection = rowSelection;
	}

	public int maxRows() {
		return charArray.length;
	}

	public int maxCols() {
		return charArray[0].length;
	}

	public void addSelectedChar() {
		if (buildName.length() < Configuration.MAX_NAME_SIZE) {
			buildName.append(charArray[rowSelection][colSelection]);
		}
	}

	public String toString() {
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
