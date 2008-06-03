package com.calclab.seecode;

import java.io.File;

import com.calclab.seecode.DirectoryScanner.FileWalker;

public class CodeFileWalker implements FileWalker {
    private final int baseLength;
    private final Code code;

    public CodeFileWalker(final Code code, final File root) {
	this.code = code;
	baseLength = root.getPath().length() + 1;
    }

    public void onFile(final File file) {
	final String fileName = file.getPath().substring(baseLength);
	code.addSource(fileName);
    }

}
