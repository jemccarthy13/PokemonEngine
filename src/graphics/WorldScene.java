package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

import audio.AudioLibrary;
import audio.AudioLibrary.SOUND_EFFECT;
import controller.GameController;
import model.Coordinate;
import model.GameData;
import tiles.Tile;
import tiles.TileSet;
import trainers.Actor;
import trainers.Actor.DIR;
import trainers.NPCLibrary;
import trainers.Player;

/**
 * A representation of the world scene
 */
public class WorldScene extends BaseScene {

	private static final long serialVersionUID = 1476556509051786242L;

	/**
	 * Singleton instance
	 */
	public static WorldScene instance = new WorldScene();

	/**
	 * Render the world scene.
	 */
	@Override
	public void render(Graphics g, GameController control) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		g2.setTransform(at);

		g.setColor(Color.BLACK);
		g.setClip(new Rectangle(-16, -42, 704, 438));

		int offsetX = GameData.getInstance().getOffsetX();
		int offsetY = GameData.getInstance().getOffsetY();
		Player player = control.getPlayer();
		int map_height = GameMap.getInstance().getHeight();
		int map_width = GameMap.getInstance().getWidth();

		int startX = GameData.getInstance().getStartCoordX();
		int startY = GameData.getInstance().getStartCoordY();

		g.translate(offsetX - Tile.TILESIZE, offsetY - 2 * Tile.TILESIZE);

		for (int layer = 1; layer < 3; layer++) {
			int x_coor = startX;
			int y_coor = startY;

			int tile_number = 0;
			for (int y = 1; y <= map_height; y++) {
				for (int x = 1; x <= map_width; x++) {
					int tilePic = GameMap.getInstance().getMapImageAt(layer, tile_number);

					if (!(layer == 2 && tilePic == 0)) {
						g.drawImage((Image) TileSet.getInstance().get(tilePic), x_coor, y_coor, null);
					}
					x_coor += Tile.TILESIZE;
					tile_number++;
				}
				x_coor = startX;
				y_coor += Tile.TILESIZE;
			}
		}

		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			g.drawImage(curNPC.tData.sprite.getImage(), curNPC.getCurrentX() * Tile.TILESIZE + startX,
					curNPC.getCurrentY() * Tile.TILESIZE + startY - 10, null);
			GameMap.getInstance().setMapTileAt(curNPC.getPosition(), TileSet.OBSTACLE);
		}

		// TODO - remove this line and the 2 setTranform lines to start
		// debugging transition to 20x20 painting
		g.translate(-offsetX, -offsetY);

		g2.setTransform(at);
		g.drawImage(player.tData.sprite.getImage(), 224, 118, null);
		g.setColor(Color.WHITE);
		g.drawString(player.getCurrentX() + "," + player.getCurrentY(), 10, 25);
	}

	/**
	 * Take a given key input and convert to a DIRection if it is a directional key
	 * press
	 * 
	 * @param keyCode
	 *            - the key pressed
	 * @return a DIR from the direction buttons if applicable
	 */
	private DIR getDirFromButton(int keyCode) {
		DIR toTravel = null;
		switch (keyCode) {
		case KeyEvent.VK_UP:
			toTravel = DIR.NORTH;
			break;
		case KeyEvent.VK_DOWN:
			toTravel = DIR.SOUTH;
			break;
		case KeyEvent.VK_LEFT:
			toTravel = DIR.WEST;
			break;
		case KeyEvent.VK_RIGHT:
			toTravel = DIR.EAST;
			break;
		}
		return toTravel;
	}

	/**
	 * Perform "z" button click at the world scene
	 */
	public void doAction(GameController control) {
		AudioLibrary.playClip(SOUND_EFFECT.SELECT);
		// overhead cost for following logic
		Player player = control.getPlayer();
		DIR playerDir = player.getDirection();
		for (Actor curNPC : NPCLibrary.getInstance().values()) {
			if (playerDir == DIR.WEST) {
				control.tryBorderNPC(curNPC, new Coordinate(player.getCurrentX() - 1, player.getCurrentY()), playerDir);
			} else if (playerDir == DIR.NORTH) {
				control.tryBorderNPC(curNPC, new Coordinate(player.getCurrentX(), player.getCurrentY() - 1), playerDir);
			} else if (playerDir == DIR.EAST) {
				control.tryBorderNPC(curNPC, new Coordinate(player.getCurrentX() + 1, player.getCurrentY()), playerDir);
			} else if (playerDir == DIR.SOUTH) {
				control.tryBorderNPC(curNPC, new Coordinate(player.getCurrentX(), player.getCurrentY() + 1), playerDir);
			}
		}
	}

	/**
	 * Handle a key press at the world scene
	 */
	@Override
	public void keyPress(int keyCode, GameController control) {
		if (keyCode == KeyEvent.VK_ENTER) {
			AudioLibrary.playClip(SOUND_EFFECT.MENU);
			control.setScene(MenuScene.instance);
		}
		// match the key to a direction, is null if the button was not
		// UP, LEFT, DOWN, or RIGHT
		DIR toTravel = getDirFromButton(keyCode);

		if (toTravel != null) {
			// one of the movement buttons was pressed, so try to move in that
			// direction
			control.getPlayer().setDirection(toTravel);
			if (control.getPlayer().canMoveInDir(toTravel)) {
				control.getPlayer().isWalking = true;
			} else {
				AudioLibrary.playClip(SOUND_EFFECT.COLLISION);
			}
		} else {
			super.keyPress(keyCode, control);
		}
	}
}
