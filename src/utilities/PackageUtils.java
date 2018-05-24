package utilities;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageUtils {

	public static void printFilesInJar() {
		try {
			ArrayList<String> classes = new ArrayList<String>();

			JarInputStream jarFile = new JarInputStream(new FileInputStream("PokemonGame.jar"));
			JarEntry jarEntry;

			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().endsWith(".jpg"))) {
					DebugUtility.printMessage("Found " + jarEntry.getName().replaceAll("/", "\\."));
					classes.add(jarEntry.getName().replaceAll("/", "\\."));
				}
			}
			jarFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
