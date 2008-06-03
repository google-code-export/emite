/**
 *
 */
package com.calclab.seecode;

public class SourceFile {
    private final String fileName;
    private Folder parent;

    public SourceFile(final String fileName) {
	this.fileName = fileName;
    }

    public String getFileName() {
	return fileName;
    }

    public Folder getParent() {
	return parent;
    }

    public void setParent(final Folder parent) {
	this.parent = parent;

    }

}
