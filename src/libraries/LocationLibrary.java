package libraries;

import java.io.File;
import java.util.HashMap;

import location.LocationData;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Location data files and map Name->Data for each
//
// ////////////////////////////////////////////////////////////////////////
public class LocationLibrary extends HashMap<String, LocationData> {

	private static final long serialVersionUID = 1L;
	private static LocationLibrary m_instance = new LocationLibrary();

	// ////////////////////////////////////////////////////////////////////////
	//
	// Private constructor causes single instance behavior
	//
	// ////////////////////////////////////////////////////////////////////////
	private LocationLibrary() {
		String path = "resources/data/Locations";
		try {
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					LocationData ld = new LocationData(listOfFiles[i].getPath());
					if (ld.isValidData())
						put(ld.name, ld);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Get instance returns the single instance of the LocationLibrary
	//
	// ////////////////////////////////////////////////////////////////////////
	public static LocationLibrary getInstance() {
		return m_instance;
	}
}