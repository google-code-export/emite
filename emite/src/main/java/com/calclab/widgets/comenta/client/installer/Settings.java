package com.calclab.widgets.comenta.client.installer;

import java.util.HashMap;

public class Settings {
    private final String[] keys;
    private final HashMap<String, String> map;

    public Settings(final String... keys) {
	this.keys = keys;
	this.map = new HashMap<String, String>();
    }

    public String get(final String key) {
	return map.get(key);
    }

    public String[] getKeys() {
	return keys;
    }

    public void set(final String key, final String value) {
	map.put(key, value);
    }

    @Override
    public String toString() {
	final StringBuilder builder = new StringBuilder();
	builder.append("Settings: {");
	for (final String key : keys) {
	    builder.append(key).append(": ").append(get(key));
	    builder.append(", ");
	}
	builder.append("}");
	return builder.toString();
    }

}
