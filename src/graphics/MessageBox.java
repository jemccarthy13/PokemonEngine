package graphics;

import java.awt.Color;
import java.awt.Graphics;

// ////////////////////////////////////////////////////////////////////////
//
// MessageBox - a utility to print messages to the screen - e.g. for signs
// or NPCs
//
// ////////////////////////////////////////////////////////////////////////
public class MessageBox {

	// ////////////////////////////////////////////////////////////////////////
	//
	// Message - constructor to print a one line message
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Message(Graphics g, String theMsg) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.MESSAGE_BOX, 0, 0, null);
		g.drawString(theMsg, 30, 260);
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Message - constructor to print a two line message
	//
	// ////////////////////////////////////////////////////////////////////////
	public void Message(Graphics g, String line1, String line2) {
		g.setColor(Color.BLACK);
		g.drawImage(SpriteLibrary.MESSAGE_BOX, 0, 0, null);
		g.drawString(line1, 30, 260);
		g.drawString(line2, 30, 290);
	}
}