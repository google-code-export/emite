package com.calclab.seecode;

import java.io.PrintStream;

import com.calclab.seecode.Code.CodeWalker;

public class JSONCodeWalker implements CodeWalker {
    private final JSONWriter out;

    public JSONCodeWalker(final PrintStream out) {
	this.out = new JSONWriter(out);
    }

    public void onBegin() {
	out.print("var json = ");
	out.openH().pair("name", "src").sep().data().sep().startChilds();
    }

    public void onBeginFolder(final Folder folder) {
	out.openH().pair("name", folder.getName()).sep();
	out.data().sep().startChilds();
    }

    public void onEnd() {
	out.println("};");
    }

    public void onEndFolder() {
	out.indent().println("]}").unindent();
    }

    public void onSource(final SourceFile sourceFile) {
    }

}
