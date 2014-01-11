package mapmaker;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class GraphicsBank {
	static final int DEFAULT_MapTile_WIDTH = 32;
	static final int DEFAULT_MapTile_HEIGHT = 32;
	static final String GB_VERSION = "1.0";
	static final String DELIM = ",";
	static final char COMMENT = '#';
	static final int ID = 0;
	static final int PATH = 1;
	static final int NAME = 2;
	static final int TYPE = 3;
	static final int EXTRA = 4;
	private ArrayList<MapTile> MapTiles;
	private ArrayList<GraphicsBankChangeListener> changeListeners;
	File loadedFrom;
	File baseDirectory;
	private boolean isUnsaved;
	Dimension baseMapTileSize;

	public GraphicsBank() {
		this.MapTiles = new ArrayList<MapTile>();
		this.changeListeners = new ArrayList<GraphicsBankChangeListener>();
		this.loadedFrom = null;

		this.baseMapTileSize = new Dimension(32, 32);

		this.isUnsaved = true;
	}

	public void addSprite(String paramString) {
		addSprite(new File(paramString));
	}

	public void addSprite(File paramFile) {}

	public void loadMapTileset(String paramString) throws FileNotFoundException, IOException {
		loadMapTileset(new File(paramString));
	}

	public void loadMapTileset(File paramFile) throws FileNotFoundException, IOException {
		if (this.MapTiles.size() > 0) {
			this.isUnsaved = true;
		} else {
			this.isUnsaved = false;
		}
		System.out.println(paramFile);
		this.baseDirectory = paramFile.getParentFile();
		this.loadedFrom = paramFile;

		int j = 0;
		BufferedReader localBufferedReader = new BufferedReader(new FileReader(paramFile));
		String str = localBufferedReader.readLine();
		for (;;) {
			str = localBufferedReader.readLine();
			j++;
			if (str == null) {
				break;
			}
			str = str.trim();
			if ((str.length() != 0) && (str.charAt(0) != '#')) {
				String[] arrayOfString = str.split(",");
				if (arrayOfString.length < 4) {
					System.err.println("Could not parse line " + j + ". :");
					System.err.println(str);
					System.err.println("(There are not enough tokens)");
				} else {
					int i = 0;
					try {
						i = Integer.parseInt(arrayOfString[0].trim());
					} catch (Exception localException) {
						System.err.println("Could not parse line " + j + ". :");
						System.err.println(str);
						System.err.println("(The MapTile id is not a valid number)");
					}
					arrayOfString[1] = arrayOfString[1].trim();
					arrayOfString[2] = arrayOfString[2].trim();
					arrayOfString[3] = arrayOfString[3].trim();

					File localFile = new File(this.baseDirectory, arrayOfString[1]);
					localFile = checkError(paramFile, j, localBufferedReader, arrayOfString, localFile);
					MapTile localMapTile = null;
					if (arrayOfString.length > 4) {
						localMapTile = new MapTile(i, localFile.toString(), arrayOfString[2].trim(),
								arrayOfString[3].trim(), arrayOfString[4].trim());
					} else {
						localMapTile = new MapTile(i, localFile.toString(), arrayOfString[2], arrayOfString[3]);
					}
					this.MapTiles.add(localMapTile);
				}
			}
		}
		localBufferedReader.close();
	}

	private File checkError(File paramFile, int j, BufferedReader localBufferedReader, String[] arrayOfString,
			File localFile) throws IOException, FileNotFoundException {
		if (!localFile.exists()) {
			localFile = new File(arrayOfString[1]);
			if (localFile.exists()) {
				System.err.println("WARNING: file " + arrayOfString[1]
						+ " not within the MapTilemaps working directory");
			} else {
				localBufferedReader.close();
				throw new FileNotFoundException("File " + arrayOfString[1] + " referenced on line " + j + " of "
						+ paramFile + " could not be found");
			}
		}
		return localFile;
	}

	void saveMapTileset(File paramFile) throws IOException {
		File localFile1 = paramFile.getParentFile();
		PrintWriter localPrintWriter = new PrintWriter(new FileWriter(paramFile));
		System.out.println("Saving " + this.MapTiles.size() + " MapTiles.");
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			File localFile2 = new File(localMapTile.getPath()).getCanonicalFile();
			String str = RelativePath.getRelativePath(new File(localFile1.getCanonicalPath()),
					new File(localFile2.getCanonicalPath()));
			localPrintWriter.print(localMapTile.getNumber() + ", " + str + ", " + localMapTile.getName() + ", "
					+ localMapTile.getType());
			if (localMapTile.getInfo() != null) {
				localPrintWriter.println(", " + localMapTile.getInfo());
			} else {
				localPrintWriter.println();
			}
		}
		localPrintWriter.close();
		this.baseDirectory = localFile1;
		this.loadedFrom = paramFile;
		this.isUnsaved = false;
	}

	File getFile() {
		return this.loadedFrom;
	}

	MapTile getMapTile(int paramInt) {
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			if (localMapTile.number == paramInt) {
				return localMapTile;
			}
		}
		return null;
	}

	MapTile getMapTile(String paramString) {
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			if (localMapTile.getName().equals(paramString)) {
				return localMapTile;
			}
		}
		return null;
	}

	MapTile remove(MapTile paramMapTile) {
		this.MapTiles.remove(paramMapTile);
		if (paramMapTile != null) {
			fireRemoveEvent(paramMapTile);
			this.isUnsaved = true;
		}
		return paramMapTile;
	}

	void add(MapTile paramMapTile) {
		this.MapTiles.add(paramMapTile);
		this.isUnsaved = true;
		fireAddEvent(paramMapTile);
	}

	int size() {
		return this.MapTiles.size();
	}

	Dimension getBaseMapTileSize() {
		return this.baseMapTileSize;
	}

	Iterator<MapTile> iterator() {
		return this.MapTiles.iterator();
	}

	public void setEffect(float[] floatParams) {
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		while (localIterator.hasNext()) {
			((MapTile) localIterator.next()).adjustRGBHS(floatParams);
		}
	}

	public boolean isUnsaved() {
		if (getFile() == null) {
			return true;
		}
		return this.isUnsaved;
	}

	File getBaseDirectory() {
		return this.baseDirectory;
	}

	int getUnusedNumber() {
		int i = 1;
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			if (i <= localMapTile.getNumber()) {
				i = localMapTile.getNumber() + 1;
			}
		}
		return i;
	}

	void addChangeListener(GraphicsBankChangeListener paramGraphicsBankChangeListener) {
		this.changeListeners.add(paramGraphicsBankChangeListener);
	}

	void removeChangeListener(GraphicsBankChangeListener paramGraphicsBankChangeListener) {
		this.changeListeners.remove(paramGraphicsBankChangeListener);
	}

	public void fireChangeEvent() {
		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.tilesetUpdated(this);
		}
	}

	private void fireAddEvent(MapTile paramMapTile) {
		System.out.println("Fire add event");

		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.MapTileAdded(this, paramMapTile);
		}
	}

	private void fireRemoveEvent(MapTile paramMapTile) {
		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.MapTileRemoved(this, paramMapTile);
		}
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.GraphicsBank
 * 
 * JD-Core Version: 0.7.0.1
 */