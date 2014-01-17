package test;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JFrame;

import factories.SpriteFactory;

public class SpriteTest {
	public static void testSprite() {

		SpriteFactory s = new SpriteFactory();
		ArrayList<Image> sprites = s.getSpritesForNPC("YOUNGSTER");
		System.out.println("size = " + sprites.size());
		JFrame jf = new JFrame();
		jf.setSize(400, 400);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		System.out.println(sprites.get(0));
	}

	public static void main(String[] args) {
		testSprite();
	}
}