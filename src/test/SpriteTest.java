package test;

import graphics.NPCThread;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import trainers.Actor;
import trainers.NPCLibrary;

//////////////////////////////////////////////////////////////////////////
//
// Unit testing for sprite generation.
//
// TODO - needs updating
//
//////////////////////////////////////////////////////////////////////////
public class SpriteTest extends JFrame {

	private static final long serialVersionUID = 8257946860941590572L;

	JPanel p = new JPanel() {
		private static final long serialVersionUID = 7322676857941810785L;

		public void paintComponent(Graphics g) {
			this.setBackground(Color.BLACK);

			Actor sam = NPCLibrary.getInstance().get("Joey");

			g.drawImage(sam.tData.sprite.getImage(), 0, 0, null);
			g.drawImage(sam.tData.sprite.getImage(), 50, 50, null);

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
		NPCThread npct = new NPCThread();
		npct.start();
		gameThread.start();
		setSize(300, 300);
		add(p);
		repaint();
		validate();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		SpriteTest frame = new SpriteTest();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}