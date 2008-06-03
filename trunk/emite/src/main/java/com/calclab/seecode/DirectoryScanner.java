package com.calclab.seecode;

import java.io.File;

public class DirectoryScanner {

    public static interface FileWalker {
	void onFile(File file);
    }

    private final File root;
    private final String pattern;

    public DirectoryScanner(final File root, final String pattern) {
	this.root = root;
	this.pattern = pattern;
    }

    /**
     * Recursive function to descent into the directory tree
     * 
     * @param dir
     *                A file object defining the top directory
     * @param pattern
     */
    public void walk(final File dir, final FileWalker fileWalker) {

	final File children[] = dir.listFiles();
	if (children != null) {
	    for (final File file : children) {
		if (file.isDirectory()) {
		    walk(file, fileWalker);
		} else if (file.getName().endsWith(pattern)) {
		    fileWalker.onFile(file);
		}
	    }

	}
    }

    public void walk(final FileWalker fileWalker) {
	walk(root, fileWalker);
    }

}
