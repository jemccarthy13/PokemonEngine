package data_structures;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LocationData {

	public String name;

	public LocationData(String path) {
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(fs);
		name = s.nextLine();
		s.close();
	}

}
