package mapmaker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Scene {
	int offsetX = 0;
	int offsetY = 0;
	float effect_rScale = 1.0F;
	float effect_gScale = 1.0F;
	float effect_bScale = 1.0F;
	float effect_hue = 0.0F;
	float effect_sat = 1.0F;
	Map map;
	ArrayList<?> sprites;
	GraphicsBank tileset;

	public Scene(Map paramMap, ArrayList<?> paramArrayList, GraphicsBank paramGraphicsBank) {
		this.map = paramMap;
		this.sprites = paramArrayList;
		this.tileset = paramGraphicsBank;
	}

	public Scene() {
		this.map = new Map(10, 10, 32, 32);
		this.tileset = new GraphicsBank();
	}

	public GraphicsBank getTileset() {
		return this.tileset;
	}

	public void setTileset(GraphicsBank paramGraphicsBank) {
		this.tileset = paramGraphicsBank;
		this.map.setTileset(paramGraphicsBank);
	}

	static Scene loadScene(File paramFile) throws IOException {
		int i = 0;
		float f1 = 1.0F;
		float f2 = 1.0F;
		float f3 = 1.0F;
		float f4 = 0.0F;
		float f5 = 1.0F;

		BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramFile));

		String str1 = localBufferedReader.readLine();

		StringTokenizer localStringTokenizer = new StringTokenizer(str1);
		int j = Integer.parseInt(localStringTokenizer.nextToken());
		int k = Integer.parseInt(localStringTokenizer.nextToken());

		String str2 = localStringTokenizer.nextToken();

		GraphicsBank localGraphicsBank = new GraphicsBank();

		File localFile = new File(paramFile.getParentFile(), str2);
		System.out.println("Attempt to load tileset " + localFile.getAbsoluteFile());

		localGraphicsBank.loadMapTileset(localFile);

		Map localMap = new Map(j, k);

		str1 = localBufferedReader.readLine();
		localStringTokenizer = new StringTokenizer(str1);
		if (localStringTokenizer.nextToken().equalsIgnoreCase("colorization")) {
			i = 1;
			f1 = Float.parseFloat(localStringTokenizer.nextToken());
			f2 = Float.parseFloat(localStringTokenizer.nextToken());
			f3 = Float.parseFloat(localStringTokenizer.nextToken());
			f4 = Float.parseFloat(localStringTokenizer.nextToken());
			f5 = Float.parseFloat(localStringTokenizer.nextToken());
		}
		while (!str1.equals(".")) {
			str1 = localBufferedReader.readLine();
		}
		for (int m = 0; m < 3; m++) {
			str1 = localBufferedReader.readLine();
			localStringTokenizer = new StringTokenizer(str1);
			for (int n = 0; n < k; n++) {
				for (int i1 = 0; i1 < j; i1++) {
					String str3 = localStringTokenizer.nextToken();
					localMap.setTile(i1, n, m, localGraphicsBank.getMapTile(Integer.parseInt(str3)));
				}
			}
		}
		localBufferedReader.close();

		Scene localScene = new Scene(localMap, new ArrayList<Object>(), localGraphicsBank);
		localScene.tileset = localGraphicsBank;
		if (i != 0) {
			float[] fParams = { f1, f2, f3, f4, f5, (float) 1.0 };
			localScene.setEffect(fParams);
		}
		return localScene;
	}

	static Scene loadScene(String paramString) throws IOException {
		Scene localScene = loadScene(new File(paramString));
		return localScene;
	}

	public void saveScene(File paramFile) {
		if (this.tileset.isUnsaved()) {
			throw new RuntimeException("Tileset is unsaved. Cannot save the scene");
		}
		try {
			PrintWriter localPrintWriter = new PrintWriter(new BufferedWriter(new FileWriter(paramFile)));

			String str1 = "";

			int i = this.map.getWidth();
			int j = this.map.getHeight();

			File localFile1 = new File(paramFile.getParentFile().getCanonicalFile().toString());
			File localFile2 = new File(this.tileset.getFile().getCanonicalFile().toString());

			String str2 = RelativePath.getRelativePath(localFile1, localFile2);

			str1 = i + " " + j + " " + str2;
			localPrintWriter.println(str1);

			str1 = "colorization " + this.effect_rScale + " " + this.effect_gScale + " " + this.effect_bScale + " "
					+ this.effect_hue + " " + this.effect_sat;

			localPrintWriter.println(str1);

			localPrintWriter.println(".");
			writeScene(localPrintWriter, i, j);
			localPrintWriter.flush();
			localPrintWriter.close();
		} catch (IOException localIOException) {
			throw new RuntimeException("Could not save the level");
		}
		System.err.println("Saved");
	}

	private void writeScene(PrintWriter localPrintWriter, int i, int j) {
		for (int k = 0; k < 3; k++) {
			for (int m = 0; m < j; m++) {
				for (int n = 0; n < i; n++) {
					MapTile localTile = this.map.getTile(n, m, k);
					if (localTile != null) {
						localPrintWriter.print(localTile.getNumber() + " ");
					} else {
						localPrintWriter.print("0 ");
					}
				}
			}
			localPrintWriter.println();
		}
	}

	void logic() {
		for (int i = 0; i < this.sprites.size(); i++) {
			Sprite localSprite = (Sprite) this.sprites.get(i);
			localSprite.logic();
		}
	}

	void render(Graphics paramGraphics) {
		this.map.render(paramGraphics, this.offsetX, this.offsetY);
	}

	public void render(Graphics paramGraphics, int paramInt1, int paramInt2) {
		this.map.render(paramGraphics, paramInt1, paramInt2);
	}

	public void render(Graphics paramGraphics, Camera paramCamera) {
		this.map.render(paramGraphics, paramCamera);
	}

	public void render(Graphics paramGraphics, Point paramPoint, Dimension paramDimension) {
		this.map.render(paramGraphics, paramPoint, paramDimension);
	}

	public void render(Graphics paramGraphics, Point paramPoint, Dimension paramDimension, int paramInt) {
		this.map.render(paramGraphics, paramPoint, paramDimension, paramInt);
	}

	void setViewSize(int paramInt1, int paramInt2) {
		this.map.setViewSize(paramInt1, paramInt2);
	}

	void setOffset(int paramInt1, int paramInt2) {
		this.offsetX = paramInt1;
		this.offsetY = paramInt2;
	}

	public void setEffect(float[] floatParams) {
		this.effect_rScale = floatParams[0];
		this.effect_gScale = floatParams[1];
		this.effect_bScale = floatParams[2];
		this.effect_hue = floatParams[3];
		this.effect_sat = floatParams[4];
		this.tileset.setEffect(floatParams);
		this.map.setZoom(floatParams[5]);
	}

	public Map getMap() {
		return this.map;
	}
}