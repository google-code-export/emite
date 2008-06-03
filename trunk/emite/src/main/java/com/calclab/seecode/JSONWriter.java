package com.calclab.seecode;

import java.io.PrintStream;

public class JSONWriter {
    public static final String INDENT = "                                                                                               ";
    private static final String X = "\"";

    private final PrintStream out;
    private int indent;
    private int id;

    public JSONWriter(final PrintStream out) {
	this.out = out;
	this.indent = 0;
	this.id = 0;
    }

    public void print(final String text) {
	out.print(text);
    }

    public JSONWriter println(final String text) {
	out.println(text);
	return this;
    }

    JSONWriter data() {
	indent();
	out.print(X + "data" + X + ": [ ]");
	return this;
    }

    JSONWriter indent() {
	out.print(INDENT.substring(0, indent * 2));
	return this;
    }

    JSONWriter openH() {
	indent();
	out.println("{");
	indent++;
	pair("id", id++).sep();
	return this;
    }

    JSONWriter pair(final String name, final Object value) {
	indent();
	out.print(X + name + X + ": " + X + value + X);
	return this;
    }

    JSONWriter sep() {
	out.println(", ");
	return this;
    }

    void startChilds() {
	indent();
	out.print("children: [");
    }

    JSONWriter unindent() {
	indent--;
	return this;
    }

}
