package test;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.junit.Test;

import graphics.NPCThread;
import trainers.Actor;
import trainers.NPCLibrary;

/**
 * 
 * Unit testing for sprite generation.
 * 
 */
public class NPCTest extends JFrame {

	/**
	 * Serialization information
	 */
	private static final long serialVersionUID = 8257946860941590572L;

	/**
	 * The main panel for testing
	 */
	JPanel p = new JPanel() {
		private static final long serialVersionUID = 7322676857941810785L;

		@Override
		public void paintComponent(Graphics g) {
			this.setBackground(Color.BLACK);

			Actor sam = NPCLibrary.getInstance().get("Joey");

			g.drawImage(sam.tData.sprite.getImage(), 0, 0, null);
			g.drawImage(sam.tData.sprite.getImage(), 50, 50, null);

			repaint();
			validate();
		}
	};

	/**
	 * The thread that powers random NPC directions
	 */
	Thread gameThread = new Thread() {
		@Override
		public void run() {
			while (true) {
				NPCTest.this.p.paintComponents(getGraphics());
				repaint();
				validate();
			}
		}
	};

	/**
	 * Start NPC testing
	 */
	public NPCTest() {
		NPCThread npct = new NPCThread();
		npct.start();
		this.gameThread.start();
		setSize(300, 300);
		add(this.p);
		repaint();
		validate();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Run the test
	 */
	@Test
	public static void testSpriteDraw() {
		NPCTest frame = new NPCTest();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}