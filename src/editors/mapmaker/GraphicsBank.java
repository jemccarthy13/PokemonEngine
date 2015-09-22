package editors.mapmaker;

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

import utilities.DebugUtility;

/**
 * Representation of a graphics bank
 */
public class GraphicsBank {
	/**
	 * Default tile width
	 */
	static final int DEFAULT_MapTile_WIDTH = 32;
	/**
	 * Default tile height
	 */
	static final int DEFAULT_MapTile_HEIGHT = 32;
	/**
	 * The graphics bank version information
	 */
	static final String GB_VERSION = "1.0";
	/**
	 * The delimiter
	 */
	static final String DELIM = ",";
	/**
	 * Comment style
	 */
	static final char COMMENT = '#';
	/**
	 * The ID field index
	 */
	static final int ID = 0;
	/**
	 * The PATH field index
	 */
	static final int PATH = 1;
	/**
	 * The NAME field index
	 */
	static final int NAME = 2;
	/**
	 * The TYPE field index
	 */
	static final int TYPE = 3;
	/**
	 * An EXTRA field index
	 */
	static final int EXTRA = 4;
	/**
	 * An ArrayList of MapTiles
	 */
	private ArrayList<MapTile> MapTiles;
	/**
	 * A List of change listeners for a graphics bank
	 */
	private ArrayList<GraphicsBankChangeListener> changeListeners;
	/**
	 * The file the graphics bank was loaded from
	 */
	File loadedFrom;
	/**
	 * The base directory to load files from
	 */
	File baseDirectory;
	/**
	 * Representing whether or not the bank was saved
	 */
	private boolean isUnsaved;
	/**
	 * the base map tile size
	 */
	Dimension baseMapTileSize;

	/**
	 * Graphics bank constructor
	 */
	public GraphicsBank() {
		this.MapTiles = new ArrayList<MapTile>();
		this.changeListeners = new ArrayList<GraphicsBankChangeListener>();
		this.loadedFrom = null;

		this.baseMapTileSize = new Dimension(32, 32);

		this.isUnsaved = true;
	}

	/**
	 * Add a sprite to the graphics bank using a String file path
	 * 
	 * @param spriteToAdd
	 *            - a string representing the path of the image to load
	 */
	public void addSprite(String spriteToAdd) {
		addSprite(new File(spriteToAdd));
	}

	/**
	 * Add a sprite to the graphics bank using a file handle
	 * 
	 * @param paramFile
	 *            - the file handle of the image to load
	 */
	public void addSprite(File paramFile) {}

	/**
	 * Loads a map tile set given the path of the file
	 * 
	 * @param tilesetPath
	 *            - the path of the tile set file
	 * @throws FileNotFoundException
	 *             - if the file cannot be found
	 * @throws IOException
	 *             - if the file cannot be read
	 */
	public void loadMapTileset(String tilesetPath) throws FileNotFoundException, IOException {
		loadMapTileset(new File(tilesetPath));
	}

	/**
	 * Loads a map tile set given the file handle
	 * 
	 * @param tileSetFile
	 *            - the file handle of the tile set file
	 * @throws FileNotFoundException
	 *             - if the file cannot be found
	 * @throws IOException
	 *             - if the file cannot be read
	 */
	public void loadMapTileset(File tileSetFile) throws FileNotFoundException, IOException {
		if (this.MapTiles.size() > 0) {
			this.isUnsaved = true;
		} else {
			this.isUnsaved = false;
		}
		DebugUtility.printMessage(tileSetFile.getAbsolutePath());
		this.baseDirectory = tileSetFile.getParentFile();
		this.loadedFrom = tileSetFile;

		int j = 0;
		BufferedReader localBufferedReader = new BufferedReader(new FileReader(tileSetFile));
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
					DebugUtility.printError("Could not parse line " + j + ". :");
					DebugUtility.printError(str);
					DebugUtility.printError("(There are not enough tokens)");
				} else {
					int i = 0;
					try {
						i = Integer.parseInt(arrayOfString[0].trim());
					} catch (Exception localException) {
						DebugUtility.printError("Could not parse line " + j + ". :");
						DebugUtility.printError(str);
						DebugUtility.printError("(The MapTile id is not a valid number)");
					}
					arrayOfString[1] = arrayOfString[1].trim();
					arrayOfString[2] = arrayOfString[2].trim();
					arrayOfString[3] = arrayOfString[3].trim();

					File localFile = new File(this.baseDirectory, arrayOfString[1]);
					localFile = checkError(tileSetFile, j, localBufferedReader, arrayOfString, localFile);
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

	/**
	 * Check for an error in the tileset file
	 * 
	 * @param tileSetFile
	 *            - the file listing tilesets
	 * @param j
	 *            - line of the tileset file
	 * @param localBufferedReader
	 *            - a buffered reader of the tileset file
	 * @param fileList
	 *            - a list of tileset files
	 * @param localFile
	 *            - a tileset file
	 * @return a new file or error if file can't be read
	 * @throws IOException
	 *             - if the file can't be read
	 * @throws FileNotFoundException
	 *             - if the file isn't available
	 */
	private File checkError(File tileSetFile, int j, BufferedReader localBufferedReader, String[] fileList,
			File localFile) throws IOException, FileNotFoundException {
		if (!localFile.exists()) {
			localFile = new File(fileList[1]);
			if (localFile.exists()) {
				DebugUtility.printError("WARNING: file " + fileList[1] + " not within the working directory");
			} else {
				localBufferedReader.close();
				throw new FileNotFoundException("File " + fileList[1] + " referenced on line " + j + " of "
						+ tileSetFile + " could not be found");
			}
		}
		return localFile;
	}

	/**
	 * Save the map tileset
	 * 
	 * @param saveFile
	 *            - the name of the save file
	 * @throws IOException
	 *             if there's an error with the save file
	 */
	void saveMapTileset(File saveFile) throws IOException {
		File fileDir = saveFile.getParentFile();
		PrintWriter saveWriter = new PrintWriter(new FileWriter(saveFile));
		DebugUtility.printMessage("Saving " + this.MapTiles.size() + " MapTiles.");
		Iterator<MapTile> mapTileIterator = this.MapTiles.iterator();
		while (mapTileIterator.hasNext()) {
			MapTile currentMapTile = (MapTile) mapTileIterator.next();
			File compareFile = new File(currentMapTile.getPath()).getCanonicalFile();
			String str = RelativePath.getRelativePath(new File(fileDir.getCanonicalPath()),
					new File(compareFile.getCanonicalPath()));
			saveWriter.print(currentMapTile.getNumber() + ", " + str + ", " + currentMapTile.getName() + ", "
					+ currentMapTile.getType());
			if (currentMapTile.getInfo() != null) {
				saveWriter.println(", " + currentMapTile.getInfo());
			} else {
				saveWriter.println();
			}
		}
		saveWriter.close();
		this.baseDirectory = fileDir;
		this.loadedFrom = saveFile;
		this.isUnsaved = false;
	}

	/**
	 * Get the file this graphics bank was loaded from
	 * 
	 * @return the file the bank was loaded from
	 */
	File getFile() {
		return this.loadedFrom;
	}

	/**
	 * Retrieve a map tile that has a specific number
	 * 
	 * @param tileNumber
	 *            - the number to look for
	 * @return a MapTile
	 */
	MapTile getMapTile(int tileNumber) {
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		MapTile retTile = null;

		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			if (localMapTile.getNumber() == tileNumber) {
				retTile = localMapTile;
			}
		}
		return retTile;
	}

	/**
	 * Retrieve a map tile whose name matches the specified number
	 * 
	 * @param nameToMatch
	 * @return the matching maptile
	 */
	MapTile getMapTile(String nameToMatch) {
		Iterator<MapTile> localIterator = this.MapTiles.iterator();
		MapTile retTile = null;
		while (localIterator.hasNext()) {
			MapTile localMapTile = (MapTile) localIterator.next();
			if (localMapTile.getName().equals(nameToMatch)) {
				retTile = localMapTile;
			}
		}
		return retTile;
	}

	/**
	 * Remove a map tile from the graphics bank
	 * 
	 * @param tileToRemove
	 * @return the tile that was removed
	 */
	MapTile remove(MapTile tileToRemove) {
		this.MapTiles.remove(tileToRemove);
		if (tileToRemove != null) {
			fireRemoveEvent(tileToRemove);
			this.isUnsaved = true;
		}
		return tileToRemove;
	}

	/**
	 * Add a map tile to the graphics bank
	 * 
	 * @param tileToAdd
	 */
	void add(MapTile tileToAdd) {
		this.MapTiles.add(tileToAdd);
		this.isUnsaved = true;
		fireAddEvent(tileToAdd);
	}

	/**
	 * Get the size of the graphics bank
	 * 
	 * @return the size of the map tiles container
	 */
	int size() {
		return this.MapTiles.size();
	}

	/**
	 * Get the base maptile size
	 * 
	 * @return the size of a maptile
	 */
	Dimension getBaseMapTileSize() {
		return this.baseMapTileSize;
	}

	/**
	 * Get the iterator over the map tiles
	 * 
	 * @return an iterator over the map tiles
	 */
	Iterator<MapTile> iterator() {
		return this.MapTiles.iterator();
	}

	/**
	 * Set the effects of each mapTile
	 * 
	 * @param RGBHs
	 *            - the effects to adjust (RBBH)
	 */
	public void setEffect(float[] RGBHs) {
		Iterator<MapTile> tileIterator = this.MapTiles.iterator();
		while (tileIterator.hasNext()) {
			((MapTile) tileIterator.next()).adjustRGBHS(RGBHs);
		}
	}

	/**
	 * Check whether or not the file has recently been saved
	 * 
	 * @return is it unsaved
	 */
	public boolean isUnsaved() {
		return this.isUnsaved || getFile() == null;
	}

	/**
	 * Get the base directory
	 * 
	 * @return the base file directory
	 */
	File getBaseDirectory() {
		return this.baseDirectory;
	}

	/**
	 * Get the unused number of graphics
	 * 
	 * @return unused number
	 */
	int getUnusedNumber() {
		int i = 1;
		Iterator<MapTile> mapTileIterator = this.MapTiles.iterator();
		while (mapTileIterator.hasNext()) {
			MapTile currentTile = (MapTile) mapTileIterator.next();
			if (i <= currentTile.getNumber()) {
				i = currentTile.getNumber() + 1;
			}
		}
		return i;
	}

	/**
	 * Add a change listener to the graphics bank
	 * 
	 * @param listener
	 *            the listener to add
	 */
	void addChangeListener(GraphicsBankChangeListener listener) {
		this.changeListeners.add(listener);
	}

	/**
	 * Remove a change listener to the graphics bank
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	void removeChangeListener(GraphicsBankChangeListener listener) {
		this.changeListeners.remove(listener);
	}

	/**
	 * Fire a change event
	 */
	public void fireChangeEvent() {
		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.tilesetUpdated(this);
		}
	}

	/**
	 * Handle an add event
	 * 
	 * @param addedMapTile
	 */
	private void fireAddEvent(MapTile addedMapTile) {
		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.MapTileAdded(this, addedMapTile);
		}
	}

	/**
	 * Handle an add event
	 * 
	 * @param removedMapTile
	 */
	private void fireRemoveEvent(MapTile removedMapTile) {
		Iterator<GraphicsBankChangeListener> localIterator = this.changeListeners.iterator();
		while (localIterator.hasNext()) {
			GraphicsBankChangeListener localGraphicsBankChangeListener = (GraphicsBankChangeListener) localIterator
					.next();
			localGraphicsBankChangeListener.MapTileRemoved(this, removedMapTile);
		}
	}
}