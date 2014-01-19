package libraries;

import java.io.File;
import java.util.HashMap;

import location.LocationData;

// ////////////////////////////////////////////////////////////////////////
//
// Look through the Location data files and map Name->Data for each
//
// ////////////////////////////////////////////////////////////////////////
public class LocationLibrary {

	// ////////////////////////////////////////////////////////////////////////
	//
	// Location Map maps Name->Data
	//
	// ////////////////////////////////////////////////////////////////////////
	class LocationDataMap extends HashMap<String, LocationData> {

		private static final long serialVersionUID = 1L;

		public LocationDataMap() {
			String path = "Data/Pokemon";
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					LocationData ld = new LocationData(listOfFiles[i].getPath());
					put(ld.name, ld);
				}
			}
		}
	}

	LocationDataMap locationData = new LocationDataMap();

	public LocationData getLocation(String locKey) {
		return locationData.get(locKey);
	}
}