package mapmaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelativePath {
	private static List<String> getPathList(File paramFile) {
		ArrayList<String> localArrayList = new ArrayList<String>();
		try {
			File localFile = paramFile.getCanonicalFile();
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

	private static String matchPathLists(List<String> paramList1,
			List<String> paramList2) {
		String str = "";
		int i = paramList1.size() - 1;
		int j = paramList2.size() - 1;
		do {
			i--;
			j--;
			if ((i < 0) || (j < 0)) {
				break;
			}
		} while (((String) paramList1.get(i)).equals(paramList2.get(j)));
		for (; i >= 0; i--) {
			str = str + ".." + File.separator;
		}
		for (; j >= 1; j--) {
			str = str + (String) paramList2.get(j) + File.separator;
		}
		str = str + (String) paramList2.get(j);
		return str;
	}

	public static String getRelativePath(File paramFile1, File paramFile2) {
		List<String> localList1 = getPathList(paramFile1);
		List<String> localList2 = getPathList(paramFile2);
		String str = matchPathLists(localList1, localList2);

		return str;
	}

	public static void main(String[] paramArrayOfString) {
		if (paramArrayOfString.length != 2) {
			System.out.println("RelativePath <home> <file>");
			return;
		}
		System.out.println("home = " + paramArrayOfString[0]);
		System.out.println("file = " + paramArrayOfString[1]);
		System.out.println("path = "
				+ getRelativePath(new File(paramArrayOfString[0]), new File(
						paramArrayOfString[1])));
	}
}

/*
 * Location: C:\eclipse\workspace\PokemonOrange.jar
 * 
 * Qualified Name: mapmaker.RelativePath
 * 
 * JD-Core Version: 0.7.0.1
 */