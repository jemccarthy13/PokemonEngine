package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import factories.SpriteFactory;

public class SpriteTest extends JFrame {

	static ArrayList<Image> sprites = new ArrayList<Image>();

	JPanel p = new JPanel() {
		public void paintComponent(Graphics g) {
			this.setBackground(Color.BLACK);
			g.drawImage(sprites.get(0), 0, 0, null);
			repaint();
			validate();
		}
	};

	public SpriteTest() {
		SpriteFactory s = new SpriteFactory();
		sprites = s.getSpritesForNPC("PROFESSOROAK_LARGE");
		System.out.println("size = " + sprites.size());
		setSize(400, 400);
		add(p);
		setLocationRelativeTo(null);
		setVisible(true);
		System.out.println(sprites.get(0));
	}

	void paintComponent(Graphics g) {
		g.drawImage(sprites.get(0), 0, 0, null);
		repaint();
		validate();
	}

	public static void main(String[] args) {
		SpriteTest frame = new SpriteTest();
		frame.setVisible(true);
	}
}