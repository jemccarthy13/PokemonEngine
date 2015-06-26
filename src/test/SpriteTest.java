package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import trainers.NPC;
import utilities.EnumsAndConstants;

//////////////////////////////////////////////////////////////////////////
//
// Unit testing for sprite generation.
//
// TODO - needs updating
//
//////////////////////////////////////////////////////////////////////////
public class SpriteTest extends JFrame {

	private static final long serialVersionUID = -8257946860941590572L;

	static ArrayList<Image> sprites = new ArrayList<Image>();

	NPC sam = EnumsAndConstants.npc_lib.getNPC("Joey");

	JPanel p = new JPanel() {
		private static final long serialVersionUID = 7322676857941810785L;

		public void paintComponent(Graphics g) {
			this.setBackground(Color.BLACK);
			g.drawImage(sam.getSprite(), 0, 0, null);
			g.drawImage(sam.getSprite(), 50, 50, null);
			/*
			 * g.drawImage(sprites.get(1), 25, 0, null);
			 * g.drawImage(sprites.get(2), 50, 0, null);
			 * g.drawImage(sprites.get(3), 50, 50, null);
			 * g.drawImage(sprites.get(4), 75, 50, null);
			 * g.drawImage(sprites.get(5), 100, 50, null);
			 * g.drawImage(sprites.get(6), 100, 100, null);
			 * g.drawImage(sprites.get(7), 125, 100, null);
			 * g.drawImage(sprites.get(8), 150, 100, null);
			 * g.drawImage(sprites.get(9), 150, 150, null);
			 * g.drawImage(sprites.get(10), 175, 150, null);
			 * g.drawImage(sprites.get(11), 200, 150, null);
			 */
			repaint();
			validate();
		}
	};

	Thread gameThread = new Thread() {
		public void run() {
			while (true) {
				p.paintComponents(getGraphics());
				repaint();
				validate();
			}
		}
	};

	public SpriteTest() {

		// NPCThread npct = new NPCThread();
		// npct.start();
		gameThread.start();
		setSize(1000, 1000);
		add(p);
		repaint();
		validate();
		// setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		SpriteTest frame = new SpriteTest();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}