package com.calclab.seecode;

import java.io.PrintStream;
import java.io.PrintWriter;

public class FolderJSONWriter {

    private final JSONWriter out;

    public FolderJSONWriter(final PrintWriter writer) {
	this.out = new JSONWriter(writer);
    }

    public void write(final Folder root) {
	out.write("var json=");
	print(root);
	out.write(";");
    }

    void print(final Folder root) {
	out.hashOpen().id().sep();
	out.pair("name", root.getName()).sep().data().sep();
	out.childsOpen();
	boolean shouldSeparate = false;
	for (final SourceFile source : root.getSources()) {
	    shouldSeparate = checkFirst(shouldSeparate);
	    print(source);
	}
	shouldSeparate = root.getSources().size() > 0;
	for (final Folder child : root.getChildren()) {
	    shouldSeparate = checkFirst(shouldSeparate);
	    print(child);
	}
	out.childsClose();
	out.hashClose();
    }

    void print(final SourceFile source) {
	out.hashOpen().id().sep();
	out.pair("name", source.getFileName()).sep().data().sep();
	out.childsOpen().childsClose().hashClose();
    }

    private boolean checkFirst(final boolean shouldSeparate) {
	if (shouldSeparate) {
	    out.sep();
	}
	return true;
    }

    private void printData(final Folder root) {
	out.hashOpen().id().sep();
	out.pair("name", root.getName()).sep().data().sep();
	out.childsOpen().childsClose().hashClose();
    }

}
