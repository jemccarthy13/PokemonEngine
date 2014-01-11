package graphics;

import java.awt.Color;
import java.awt.Graphics;

import utilities.EnumsAndConstants;

public class MessageBox {
	public void Message(Graphics g, String theMsg) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.MESSAGE_BOX, 0, 0, null);
		g.drawString(theMsg, 30, 260);
	}

	public void Message(Graphics g, String line1, String line2) {
		g.setFont(EnumsAndConstants.POKEFONT);
		g.setColor(Color.BLACK);
		g.drawImage(EnumsAndConstants.sprite_lib.MESSAGE_BOX, 0, 0, null);
		g.drawString(line1, 30, 260);
		g.drawString(line2, 30, 290);
	}
}