package mapmaker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;

public class MapTile {
	public static boolean effects_enabled = true;
	BufferedImage effectImage = null;
	float effect_rScale;
	float effect_gScale;
	float effect_bScale;
	float effect_zoom;
	float effect_hue;
	float effect_sat;
	private int imageWidth = 0;
	private int imageHeight = 0;
	int zoomWidth;
	int zoomHeight;
	String name = null;
	int number = -1;
	String type = null;
	String path = null;
	String info = null;
	Image image = null;

	public MapTile(int paramInt, String paramString1, String paramString2, String paramString3) {
		this.type = paramString3;
		this.number = paramInt;
		this.name = paramString2;
		this.path = paramString1;

		this.image = new ImageIcon(paramString1).getImage();
		if (this.image == null) {
			throw new RuntimeException("Could not load image" + paramString1);
		}
		this.imageWidth = this.image.getWidth(null);
		this.imageHeight = this.image.getHeight(null);
		this.zoomWidth = this.imageWidth;
		this.zoomHeight = this.imageHeight;
	}

	public MapTile(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4) {
		this(paramInt, paramString1, paramString2, paramString3);
		if (paramString4.indexOf(',') >= 0) {
			throw new RuntimeException("Info string cannot contain \",\" characters");
		}
		this.info = paramString4;
	}

	public MapTile(MapTile paramTile) {
		System.err.println("WARNING: Creating shallow copy of tile");

		this.number = paramTile.number;
		this.type = paramTile.type;
		this.name = paramTile.name;
		this.path = paramTile.path;
		this.image = paramTile.image;
	}

	String getImageLocation() {
		return this.path;
	}

	public boolean equals(MapTile paramTile) {
		if (paramTile == null) {
			return false;
		}
		if (this.number == paramTile.number) {
			return true;
		}
		return false;
	}

	static boolean areEqual(MapTile paramTile1, MapTile paramTile2) {
		if ((paramTile1 == null) && (paramTile2 == null)) {
			return true;
		}
		if (paramTile1 != null) {
			return paramTile1.equals(paramTile2);
		}
		return false;
	}

	public MapTile() {
		this.image = null;
	}

	public Image getImage() {
		return this.image;
	}

	public String getType() {
		return this.type;
	}

	public int getNumber() {
		return this.number;
	}

	public String getName() {
		return this.name;
	}

	public String getInfo() {
		return this.info;
	}

	public String getPath() {
		return this.path;
	}

	void initEffectImage() {
		this.effectImage = new BufferedImage((int) (this.imageWidth * this.effect_zoom),
				(int) (this.imageHeight * this.effect_zoom), 2);
		Graphics2D localGraphics2D = (Graphics2D) this.effectImage.getGraphics();
		localGraphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		localGraphics2D.drawImage(this.image, 0, 0, (int) (this.imageWidth * this.effect_zoom),
				(int) (this.imageHeight * this.effect_zoom), null, null);
	}

	void adjustRGBHS(float[] floatParams) {
		if (effects_enabled) {
			if ((floatParams[5] == this.effect_zoom) && (floatParams[0] == this.effect_rScale)
					&& (floatParams[1] == this.effect_gScale) && (floatParams[2] == this.effect_bScale)) {
				return;
			}
			this.effect_zoom = floatParams[5];
			this.zoomWidth = ((int) (this.imageWidth * floatParams[5]));
			this.zoomHeight = ((int) (this.imageHeight * floatParams[5]));

			initEffectImage();

			float[] arrayOfFloat = new float[3];

			WritableRaster localWritableRaster = this.effectImage.getRaster();

			int[] arrayOfInt = new int[this.zoomWidth * this.zoomHeight * 4];

			arrayOfInt = localWritableRaster.getPixels(0, 0, this.zoomWidth, this.zoomHeight, arrayOfInt);
			for (int i = 0; i < arrayOfInt.length / 4; i++) {
				if ((floatParams[3] != 0.0F) || (floatParams[4] != 1.0F)) {
					arrayOfFloat = Color.RGBtoHSB(arrayOfInt[(i * 4)], arrayOfInt[(i * 4 + 1)],
							arrayOfInt[(i * 4 + 2)], arrayOfFloat);
					arrayOfFloat[0] += floatParams[3];
					arrayOfFloat[1] *= floatParams[4];
					if (arrayOfFloat[1] > 1.0F) {
						arrayOfFloat[1] = 1.0F;
					}
					Color localColor = new Color(Color.HSBtoRGB(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]));
					arrayOfInt[(i * 4 + 0)] = localColor.getRed();
					arrayOfInt[(i * 4 + 1)] = localColor.getGreen();
					arrayOfInt[(i * 4 + 2)] = localColor.getBlue();
				}
				arrayOfInt[(i * 4)] = ((int) (arrayOfInt[(i * 4)] * floatParams[0]));
				if (arrayOfInt[(i * 4)] > 255) {
					arrayOfInt[(i * 4)] = 255;
				}
				arrayOfInt[(i * 4 + 1)] = ((int) (arrayOfInt[(i * 4 + 1)] * floatParams[1]));
				if (arrayOfInt[(i * 4 + 1)] > 255) {
					arrayOfInt[(i * 4 + 1)] = 255;
				}
				arrayOfInt[(i * 4 + 2)] = ((int) (arrayOfInt[(i * 4 + 2)] * floatParams[2]));
				if (arrayOfInt[(i * 4 + 2)] > 255) {
					arrayOfInt[(i * 4 + 2)] = 255;
				}
			}
			localWritableRaster.setPixels(0, 0, this.zoomWidth, this.zoomHeight, arrayOfInt);
		}
	}

	public void render(Graphics paramGraphics, int paramInt1, int paramInt2) {
		if ((effects_enabled) && (this.effectImage != null)) {
			paramGraphics.drawImage(this.effectImage, paramInt1 - this.zoomWidth, paramInt2 - this.zoomHeight, null);
		} else if (this.image != null) {
			paramGraphics.drawImage(this.image, paramInt1 - this.imageWidth, paramInt2 - this.imageHeight, null);
		}
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.MapTile
 * 
 * JD-Core Version: 0.7.0.1
 */