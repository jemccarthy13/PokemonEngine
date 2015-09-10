package graphics;

import java.awt.Image;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ImageIcon;

import utilities.DebugUtility;

// ////////////////////////////////////////////////////////////////////////
//
// Create all images used in game for reference.
//
// ////////////////////////////////////////////////////////////////////////
public class SpriteLibrary extends HashMap<String, ArrayList<ImageIcon>> {

	private static final long serialVersionUID = 8298201257452310707L;

	public static String libPath = "resources/graphics_lib/";

	private static SpriteLibrary m_instance = new SpriteLibrary();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Default constructor
	//
	// SpriteLibrary as a whole lazy loads all images
	//
	// ////////////////////////////////////////////////////////////////////////
	private SpriteLibrary() {
		DebugUtility.debugHeader("Graphics");
		DebugUtility.printMessage("Initialized image library.");
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Returns the single instance of the sprite library
	//
	// ////////////////////////////////////////////////////////////////////////
	public static SpriteLibrary getInstance() {
		return m_instance;
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Returns the Font character Image corresponding to the given char
	//
	// ////////////////////////////////////////////////////////////////////////
	public ImageIcon getFontChar(Character c) {
		if (containsKey(c + ".png")) {
			return get(c.toString() + ".png").get(0);
		} else {
			getImageIcon(c.toString());
			return get(c.toString() + ".png").get(0);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get the set of 12 directional sprites that correspond to the given name
	//
	// ////////////////////////////////////////////////////////////////////////
	public ArrayList<ImageIcon> getSprites(String name) {
		if (containsKey(name)) {
			return get(name);
		} else if (loadActorSprites(name)) {
			return get(name);
		} else {
			System.err.println(name + " images not found in sprite library.");
			return null;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Static access wrapper method to get a paintable image
	//
	// ////////////////////////////////////////////////////////////////////////
	public static Image getImage(String name) {
		return getInstance().getImageIcon(name).getImage();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Returns an image from the instance of the library
	//
	// ////////////////////////////////////////////////////////////////////////
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

			if (containsKey(name)) {
				return get(name).get(0);
			} else {
				System.err.println(name + " not found in graphics library.");
				return null;
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Try to find the image resource in a given path
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Try to create a group of sprites for a given NPC
	//
	// ////////////////////////////////////////////////////////////////////////
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

	// ////////////////////////////////////////////////////////////////////////
	//
	// Loads an image from a given path
	//
	// ////////////////////////////////////////////////////////////////////////
	public static ImageIcon createImage(String path) {
		File f = new File(path);
		if (!f.exists()) {
			System.err.println("Image path does not exist: " + path);
			System.exit(0);
		}
		path = "/" + path.replace("resources/", "").replace("resources\\", "");
		ImageIcon thisIcon = new ImageIcon(System.class.getResource(path));
		return thisIcon;
	}
}