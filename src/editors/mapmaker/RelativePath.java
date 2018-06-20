package editors.mapmaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.DebugUtility;

/**
 * A utility to get the relative path to a file
 */
public class RelativePath {
	/**
	 * Retrieve a list of paths to a file
	 * 
	 * @param file
	 *            - the file to get paths for
	 * @return the list of paths to that file
	 */
	private static List<String> getPathList(File file) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		try {
			File localFile = file.getCanonicalFile();
			while (localFile != null) {
				localArrayList.add(localFile.getName());
				localFile = localFile.getParentFile();
			}
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
			localArrayList = null;
		}
		return localArrayList;
	}

	/**
	 * Match the path lists of the two parameter file lists
	 * 
	 * @param fileList1
	 * @param fileList2
	 * @return relative path between file list 1 and file list 2
	 */
	private static String matchPathLists(List<String> fileList1, List<String> fileList2) {
		String str = "";
		int i = fileList1.size() - 1;
		int j = fileList2.size() - 1;
		do {
			i--;
			j--;
			if ((i < 0) || (j < 0)) {
				break;
			}
		} while (fileList1.get(i).equals(fileList2.get(j)));
		for (; i >= 0; i--) {
			str = str + ".." + File.separator;
		}
		for (; j >= 1; j--) {
			str = str + fileList2.get(j) + File.separator;
		}
		str = str + fileList2.get(j);
		return str;
	}

	/**
	 * Get the relative file path
	 * 
	 * @param file1
	 *            - first file
	 * @param file2
	 *            - other file
	 * @return a string matching relative path from file 1 to file 2
	 */
	public static String getRelativePath(File file1, File file2) {
		List<String> fileList1 = getPathList(file1);
		List<String> fileList2 = getPathList(file2);
		String str = matchPathLists(fileList1, fileList2);

		return str;
	}

	/**
	 * The main entry point of the RelativePath utility
	 * 
	 * @param args
	 *            - command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			DebugUtility.printMessage("RelativePath <home> <file>");
			return;
		}
	}
}