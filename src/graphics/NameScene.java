package graphics;

import java.io.Serializable;

import driver.Configuration;

// ////////////////////////////////////////////////////////////////////////
//
// NameScene holds all variables for printing a name selection screen
// can name any NPC, player, or Pokemon with this screen
//
// ////////////////////////////////////////////////////////////////////////
public class NameScene implements Serializable {

	private static final long serialVersionUID = 3726070037021401282L;

	// name that and shrinks dynamically according to user's choices
	StringBuilder buildName = new StringBuilder();

	// char selection information
	public int rowSelection = 0;
	public int colSelection = 0;

	// the name screen layout makes it easer to get selected characters
	char[][] charArray = { { 'A', 'B', 'C', 'D', 'E', 'F' }, { 'G', 'H', 'I', 'J', 'K', 'L' },
			{ 'M', 'N', 'O', 'P', 'Q', 'R' }, { 'S', 'T', 'U', 'V', 'W', 'X' }, { 'Y', 'Z', ' ', '!', '?', '.' } };

	// the name of the object to be named
	public String toBeNamed;

	// ////////////////////////////////////////////////////////////////////////
	//
	// setToBeNamed - Sets the toBeNamed object. Used for graphics painting
	// to get the sprite of the thing to be named.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void setToBeNamed(String tbn) {
		toBeNamed = tbn;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// addSelectedChar - Sets the toBeNamed object. Used for graphics painting
	// to get the sprite of the thing to be named.
	//
	// ////////////////////////////////////////////////////////////////////////
	public void addSelectedChar() {
		if (buildName.length() < Configuration.MAX_NAME_SIZE) {
			buildName.append(charArray[rowSelection][colSelection]);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// getChosenName - gets the name built by the user in the current session
	//
	// ////////////////////////////////////////////////////////////////////////
	public String getChosenName() {
		return buildName.toString();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Removes a char from the string in the proccess of being built
	//
	// ////////////////////////////////////////////////////////////////////////
	public void removeChar() {
		int size = buildName.length();
		if (size > 0) {
			buildName.deleteCharAt(size - 1);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Resets the string builder (clear all chars)
	//
	// ////////////////////////////////////////////////////////////////////////
	public void reset() {
		int size = buildName.length();
		buildName.delete(0, size);
	}
}
