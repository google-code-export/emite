package com.calclab.seecode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SeeCode {

    public static void main(final String args[]) throws IOException {
	final File data = getDirectory("./seecode/data");
	final File src = getDirectory("./src/main/java");
	final String pattern = ".java";
	final Code code = new Code();
	new DirectoryScanner(src, pattern).walk(new CodeFileWalker(code, src));
	final Folder root = code.getRootFolder("com");
	final String dataFileName = data.getAbsolutePath() + "/seecode-data.js";
	System.out.println(dataFileName);
	final FileWriter writer = new FileWriter(dataFileName);
	new FolderJSONWriter(new PrintWriter(writer)).write(root);
	writer.close();
    }

    private static File getDirectory(final String path) {
	final File file = new File(path);
	if (file.exists() && file.isDirectory()) {
	    return file;
	} else {
	    throw new RuntimeException(path + " folder not found: " + file);
	}
    }

}
