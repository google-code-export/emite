package com.calclab.seecode;

import java.io.File;

public class SeeCode {

    public static void main(final String args[]) {
	final File data = getDirectory("./seecode/data");
	final File src = getDirectory("./src/main/java");
	final String pattern = ".java";
	final Code code = new Code();
	new DirectoryScanner(src, pattern).walk(new CodeFileWalker(code, src));
	code.walk(new JSONCodeWalker(System.out));
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
