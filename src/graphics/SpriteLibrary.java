package graphics;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;

import trainers.Actor.DIR;
import utilities.DebugUtility;

// ////////////////////////////////////////////////////////////////////////
//
// Create all images used in game for reference.
//
// ////////////////////////////////////////////////////////////////////////
/**
 * Create all images used in game for reference.
 */
public class SpriteLibrary extends HashMap<String, ArrayList<ImageIcon>> {

	private static final long serialVersionUID = 8298201257452310707L;

	/**
	 * The location of the graphics library
	 */
	private String libPath = "resources/graphics_lib/";

	private static SpriteLibrary m_instance = new SpriteLibrary();

	/**
	 * Default private constructor - singleton flyweight pattern
	 */
	private SpriteLibrary() {
		DebugUtility.printHeader("Graphics");
		DebugUtility.printMessage("Initialized image library.");
	}

	/**
	 * Returns the single instance of the sprite library
	 * 
	 * @return SpriteLibrary instance
	 */
	public static SpriteLibrary getInstance() {
		return m_instance;
	}

	/**
	 * Returns the Font character Image corresponding to the given char
	 * 
	 * @param c
	 *            - the character to look up
	 * @return an ImageIcon for a text character
	 */
	public ImageIcon getFontChar(Character c) {
		if (containsKey(c + ".png")) {
			return get(c.toString() + ".png").get(0);
		} else {
			getImageIcon(c.toString());
			return get(c.toString() + ".png").get(0);
		}
	}

	/**
	 * Returns the Font character Image corresponding to the given char
	 * 
	 * @param c
	 *            - the character to look up
	 * @return an ImageIcon for a text character
	 */
	public ImageIcon getSmallFontChar(Character c) {
		String name = c.toString() + "_small.png";
		if (containsKey(name)) {
			return get(name).get(0);
		} else {
			getImageIcon(c.toString() + "_small");
			return get(name).get(0);
		}
	}

	/**
	 * Get the set of 12 directional sprites that correspond to the given name
	 * 
	 * @param name
	 *            - the name of the sprite to look up
	 * @return ArrayList<ImageIcon> all directional sprites
	 */
	public static ArrayList<ImageIcon> getSprites(String name) {
		if (m_instance.containsKey(name)) {
			return m_instance.get(name);
		} else if (m_instance.loadActorSprites(name)) {
			return m_instance.get(name);
		} else {
			DebugUtility.printError(name + " images not found in sprite library.");
			return null;
		}
	}

	/**
	 * Get a specific directional sprite
	 * 
	 * @param name
	 *            - the name of the sprite set
	 * @param dir
	 *            - the direction the sprite is facing
	 * @return an ImageIcon for the specified sprite in the given direction
	 */
	public static ImageIcon getSpriteForDir(String name, DIR dir) {
		ArrayList<ImageIcon> sprites = getSprites(name);
		ImageIcon retSprite = null;
		switch (dir) {
		case NORTH:
			retSprite = sprites.get(9);
			break;
		case EAST:
			retSprite = sprites.get(6);
			break;
		case SOUTH:
			retSprite = sprites.get(0);
			break;
		case WEST:
			retSprite = sprites.get(3);
			break;
		default:
			retSprite = sprites.get(0);
		}
		return retSprite;
	}

	/**
	 * Static access wrapper method to retrieve a paintable image
	 * 
	 * @param name
	 *            - the image to look for
	 * @return an Image if it can be retrieved
	 */
	public static Image getImage(String name) {
		return getInstance().getImageIcon(name).getImage();
	}

	/**
	 * Returns an image from the instance of the library if the image exists. If
	 * the image does not exist, try some of the graphics library paths in order
	 * to load the graphic flyweight
	 * 
	 * @param name
	 *            - the image to look for
	 * @return an ImageIcon, if it can be created
	 */
	public ImageIcon getImageIcon(String name) {
		name += ".png";
		// if the image exists, get it and return it
		if (containsKey(name)) {
			return get(name).get(0);
		} else {
			tryPath("Characters/BattleSprites", name);
			tryPath("Titles", name);
			tryPath("Pictures", name);
			tryPath("Font", name);
			tryPath("tiles", name);
			tryPath("tiles", name);

			if (containsKey(name)) {
				return get(name).get(0);
			} else {
				DebugUtility.printError(name + " not found in graphics library.");
				return null;
			}
		}
	}

	/**
	 * Try to find the image resource in a given relative path
	 * 
	 * @param path
	 *            - the relative path to look in
	 * @param name
	 *            - the name of the image to find
	 * @return whether or not the path had the image
	 */
	public boolean tryPath(String path, String name) {
		File folder = new File(libPath + path + "/");
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (file.isFile() && name.equals(file.getName())) {
				ArrayList<ImageIcon> spriteGroup = new ArrayList<ImageIcon>();
				spriteGroup.add(createImage(file.getPath().replace("\\", "/")));
				put(name, spriteGroup);
				return true;
			}
		}

		return false;
	}

	/**
	 * Try to load a group of sprites for a given character
	 * 
	 * @param name
	 *            - the Actor to get sprites for
	 * @return whether or not the sprites were loaded successfully
	 */
	public boolean loadActorSprites(String name) {

		// for each NPC character directory, create a list of available sprites
		// and map NPC name -> sprite images
		try {
			String npcFilePath = "resources/graphics_lib/Characters/Sprites/";
			File file = new File(npcFilePath);
			String[] directories = file.list(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return new File(dir, name).isDirectory();
				}
			});

			// for each of the directories (characters)
			for (String npcCharacter : directories) {
				// if this is the character of interest
				if (npcCharacter.equals(name)) {
					// gather a group of all relevant sprites, and map to name
					File f = new File(npcFilePath + npcCharacter);
					String[] files = f.list();

					// make sure files load in the proper order expected for
					// walking/direction animations
					Arrays.sort(files);

					ArrayList<ImageIcon> spriteGroup = new ArrayList<ImageIcon>();
					for (String directionImage : files) {
						spriteGroup.add(createImage(npcFilePath + npcCharacter + "/" + directionImage));
					}
					put(npcCharacter, spriteGroup);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Loads an image from a given path
	 * 
	 * @param path
	 * @return an ImageIcon if it was loaded, otherwise an error
	 */
	public static ImageIcon createImage(String path) {
		File f = new File(path);
		if (!f.exists()) {
			DebugUtility.error("Image path does not exist: " + path);
		}
		path = "/" + path.replace("resources/", "").replace("resources\\", "");
		URL iconURL = System.class.getResource(path);
		if (iconURL == null) {
			DebugUtility.error("Cannot find resource" + path);
		}
		ImageIcon thisIcon = new ImageIcon(System.class.getResource(path));
		return thisIcon;
	}

	/**
	 * Get the graphics library path
	 * 
	 * @return a String path to the graphics library
	 */
	public static String getLibPath() {
		return m_instance.libPath;
	}
}