package mapmaker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

public class Map {
	int tileWidth = 32;
	int tileHeight = 32;
	int zoomWidth = 32;
	int zoomHeight = 32;
	int viewWidth = 400;
	int viewHeight = 400;
	GraphicsBank gfx;
	ArrayList<MapChangeListener> changeListeners;
	static final int LAYERS = 3;
	MapTile[][][] tiles;

	public Map(int paramInt1, int paramInt2) {
		this.tiles = new MapTile[paramInt1][paramInt2][3];
		this.changeListeners = new ArrayList<MapChangeListener>();
	}

	public Map(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		this(paramInt1, paramInt2);
		this.tileWidth = paramInt3;
		this.tileHeight = paramInt4;
		this.zoomWidth = paramInt3;
		this.zoomHeight = paramInt4;
	}

	public void setTile(int paramInt1, int paramInt2, int paramInt3,
			MapTile paramTile) {
		this.tiles[paramInt1][paramInt2][paramInt3] = paramTile;
	}

	void setZoom(float paramFloat) {
		this.zoomWidth = ((int) (this.tileWidth * paramFloat));
		this.zoomHeight = ((int) (this.tileHeight * paramFloat));
	}

	public void setViewSize(int paramInt1, int paramInt2) {
		this.viewWidth = paramInt1;
		this.viewHeight = paramInt2;
	}

	public void render(Graphics paramGraphics, int paramInt1, int paramInt2) {
		int i = Math.max(paramInt1 / this.zoomWidth, 0);
		int j = Math.min((paramInt1 + this.viewWidth) / this.zoomWidth,
				this.tiles.length);

		int k = Math.max(paramInt2 / this.zoomHeight, 0);
		int m = Math.min((paramInt2 + this.viewHeight) / this.zoomHeight,
				this.tiles[0].length);
		for (int n = 0; n < 3; n++) {
			for (int i1 = i; i1 < j; i1++) {
				for (int i2 = k; i2 < m; i2++) {
					if (this.tiles[i1][i2][n] != null) {
						this.tiles[i1][i2][n].render(paramGraphics, i1
								* this.zoomWidth - paramInt1, i2
								* this.zoomHeight - paramInt2);
					}
				}
			}
		}
	}

	public void render(Graphics paramGraphics, Camera paramCamera) {
		setViewSize(paramCamera.viewWidth, paramCamera.viewHeight);
		render(paramGraphics, (int) (paramCamera.viewx - this.viewWidth / 2),
				(int) (paramCamera.viewy - this.viewHeight / 2));
	}

	public void render(Graphics paramGraphics, Point paramPoint,
			Dimension paramDimension) {
		double d1 = Math.max(paramPoint.getX() / this.zoomWidth, 0.0D);
		double d2 = Math.min((paramPoint.getX() + paramDimension.getWidth())
				/ this.zoomWidth, this.tiles.length);

		double d3 = Math.max(paramPoint.getY() / this.zoomHeight, 0.0D);
		double d4 = Math.min((paramPoint.getY() + paramDimension.getHeight())
				/ this.zoomHeight, this.tiles[0].length);
		for (int i = 0; i < 3; i++) {
			for (int j = (int) d3; j < d4; j++) {
				for (int k = (int) d1; k < d2; k++) {
					if (this.tiles[k][j][i] != null) {
						this.tiles[k][j][i].render(paramGraphics, k
								* this.zoomWidth + this.zoomWidth, j
								* this.zoomHeight + this.zoomHeight);
					}
				}
			}
		}
	}

	public void render(Graphics paramGraphics, Point paramPoint,
			Dimension paramDimension, int paramInt) {
		double d1 = Math.max(paramPoint.getX() / this.zoomWidth, 0.0D);
		double d2 = Math.min((paramPoint.getX() + paramDimension.getWidth())
				/ this.zoomWidth, this.tiles.length);

		double d3 = Math.max(paramPoint.getY() / this.zoomHeight, 0.0D);
		double d4 = Math.min((paramPoint.getY() + paramDimension.getHeight())
				/ this.zoomHeight, this.tiles[0].length);
		int i = paramInt;
		for (int j = (int) d3; j < d4; j++) {
			for (int k = (int) d1; k < d2; k++) {
				if (this.tiles[k][j][i] != null) {
					this.tiles[k][j][i].render(paramGraphics, k
							* this.zoomWidth + this.zoomWidth, j
							* this.zoomHeight + this.zoomHeight);
				}
			}
		}
	}

	public int getWidth() {
		return this.tiles.length;
	}

	public int getHeight() {
		return this.tiles[0].length;
	}

	public int getTileWidth() {
		return this.tileWidth;
	}

	public int getTileHeight() {
		return this.tileHeight;
	}

	public int getZoomWidth() {
		return this.zoomWidth;
	}

	public int getZoomHeight() {
		return this.zoomHeight;
	}

	public MapTile getTile(int paramInt1, int paramInt2, int paramInt3) {
		return this.tiles[paramInt1][paramInt2][paramInt3];
	}

	void resize(int paramInt1, int paramInt2) {
		resize(paramInt1, paramInt2, 3);
	}

	void resize(int paramInt1, int paramInt2, int paramInt3) {
		System.out.println("Call resize");

		paramInt1 = Math.max(1, paramInt1);
		paramInt2 = Math.max(1, paramInt2);
		MapTile[][][] arrayOfTile = new MapTile[paramInt1][paramInt2][paramInt3];

		int i = Math.min(paramInt1, this.tiles.length);
		int j = Math.min(paramInt2, this.tiles[0].length);
		int k = Math.min(paramInt3, this.tiles[0][0].length);
		for (int m = 0; m < i; m++) {
			for (int n = 0; n < j; n++) {
				for (int i1 = 0; i1 < k; i1++) {
					arrayOfTile[m][n][i1] = this.tiles[m][n][i1];
				}
			}
		}
		this.tiles = arrayOfTile;
	}

	void shift(int paramInt1, int paramInt2) {
		System.out.println("Shift to new offset " + paramInt1 + ", "
				+ paramInt2 + ".");
		MapTile[][][] arrayOfTile = new MapTile[this.tiles.length][this.tiles[0].length][3];

		int i = Math.max(0, -paramInt1);
		int j = Math.max(0, -paramInt2);
		int k = Math.min(this.tiles.length, this.tiles.length - paramInt1);
		int m = Math
				.min(this.tiles[0].length, this.tiles[0].length - paramInt2);
		for (int n = i; n < k; n++) {
			for (int i1 = j; i1 < m; i1++) {
				for (int i2 = 0; i2 < 3; i2++) {
					arrayOfTile[(n + paramInt1)][(i1 + paramInt2)][i2] = this.tiles[n][i1][i2];
				}
			}
		}
		this.tiles = arrayOfTile;
	}

	void clear() {
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					this.tiles[i][j][k] = null;
				}
			}
		}
	}

	public int[][][] toIntArray() {
		int[][][] arrayOfInt = new int[this.tiles.length][this.tiles[0].length][this.tiles[0][0].length];
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					if (this.tiles[i][j][k] != null) {
						arrayOfInt[i][j][k] = this.tiles[i][j][k].number;
					} else {
						arrayOfInt[i][j][k] = 0;
					}
				}
			}
		}
		return arrayOfInt;
	}

	public void setAllTiles(int[][][] paramArrayOfInt,
			GraphicsBank paramGraphicsBank) {
		this.gfx = paramGraphicsBank;
		resize(this.tiles.length, this.tiles[0].length, this.tiles[0][0].length);
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[0].length; j++) {
				for (int k = 0; k < 3; k++) {
					this.tiles[i][j][k] = paramGraphicsBank
							.getMapTile(paramArrayOfInt[i][j][k]);
				}
			}
		}
	}

	public void setTileset(GraphicsBank paramGraphicsBank) {
		int[][][] arrayOfInt = toIntArray();
		setAllTiles(arrayOfInt, paramGraphicsBank);
	}

	public void addChangeListener(MapChangeListener paramMapChangeListener) {
		this.changeListeners.add(paramMapChangeListener);
	}

	public void removeChangeListener(MapChangeListener paramMapChangeListener) {
		this.changeListeners.remove(paramMapChangeListener);
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.Map
 * 
 * JD-Core Version: 0.7.0.1
 */