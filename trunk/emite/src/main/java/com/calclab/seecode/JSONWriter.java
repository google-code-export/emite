package com.calclab.seecode;

import java.io.PrintWriter;

public class JSONWriter {
    private static final String X = "\"";

    private final PrintWriter out;
    private int id;

    public JSONWriter(final PrintWriter out) {
	this.out = out;
	this.id = 0;
    }

    public JSONWriter childsClose() {
	return print("]");
    }

    public JSONWriter childsOpen() {
	return print("children:[");
    }

    public JSONWriter data() {
	return print(X + "data" + X + ":[]");
    }

    public JSONWriter hashClose() {
	return print("}");
    }

    public JSONWriter hashOpen() {
	return print("{");
    }

    public JSONWriter id() {
	return pair("id", id++);
    }

    public JSONWriter pair(final String name, final Object value) {
	return print(X + name + X + ":" + X + value + X);
    };

    public JSONWriter sep() {
	return print(",");
    }

    public JSONWriter write(final String text) {
	return print(text);
    }

    private JSONWriter print(final String text) {
	out.print(text);
	return this;
    }

}
