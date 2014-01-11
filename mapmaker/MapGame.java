package mapmaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class MapGame implements KeyListener {
	static Scene scene;
	static JFrame mainFrame;
	static PlayerSprite player;
	static Camera cam;
	static BufferStrategy strat;
	static long time;
	boolean leftPressed = false;
	boolean rightPressed = false;
	boolean upPressed = false;
	boolean downPressed = false;

	public void keyTyped(KeyEvent paramKeyEvent) {
	}

	public void keyPressed(KeyEvent paramKeyEvent) {
		switch (paramKeyEvent.getKeyCode()) {
		case 37:
			this.leftPressed = true;
			break;
		case 39:
			this.rightPressed = true;
			break;
		case 38:
			this.upPressed = true;
			break;
		case 40:
			this.downPressed = true;
		}
	}

	public void keyReleased(KeyEvent paramKeyEvent) {
		switch (paramKeyEvent.getKeyCode()) {
		case 37:
			this.leftPressed = false;
			break;
		case 39:
			this.rightPressed = false;
			break;
		case 38:
			this.upPressed = false;
			break;
		case 40:
			this.downPressed = false;
		}
	}

	public static void main(String[] paramArrayOfString) throws Exception {
		mainFrame = new JFrame("MapGame");
		mainFrame.setSize(800, 600);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
				System.exit(0);
			}
		});
		mainFrame.setVisible(true);
		Thread.sleep(50L);
		mainFrame.createBufferStrategy(2);
		strat = mainFrame.getBufferStrategy();

		scene = Scene.loadScene("scenes/mySceneTest.dat");
		cam = new Camera();
		player = new PlayerSprite();

		cam.trackSprite(player);

		int i = 0;
		for (;;) {
			time = System.currentTimeMillis();
			if (!strat.contentsLost()) {
				Graphics localGraphics = strat.getDrawGraphics();
				localGraphics.setColor(Color.WHITE);
				localGraphics.fillRect(0, 0, mainFrame.getWidth(),
						mainFrame.getHeight());

				cam.logic();
				scene.render(localGraphics, cam);
				player.render(localGraphics, cam);

				strat.show();
				localGraphics.dispose();

				player.move(2.356195F, (float) (Math.random() * 10.0D));

				System.out.print(".");
				if (i++ % 100 == 0) {
					cam.setViewSize(mainFrame.getWidth() + 100,
							mainFrame.getHeight() + 100);
				}
			} else {
				System.out.print("!");
				mainFrame.createBufferStrategy(2);
				strat = mainFrame.getBufferStrategy();
			}
			Thread.sleep(15L);
		}
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.MapGame
 * 
 * JD-Core Version: 0.7.0.1
 */