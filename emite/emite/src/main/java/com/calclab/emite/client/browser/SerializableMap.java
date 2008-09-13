package com.calclab.emite.client.browser;

import java.util.HashMap;

@SuppressWarnings("serial")
public class SerializableMap extends HashMap<String, String> {

    public static SerializableMap restore(final String serialized) {
	final SerializableMap map = new SerializableMap();
	final int total = serialized.length() - 1;
	String key, value;
	int begin, end, next;
	next = -1;

	do {
	    begin = next + 1;
	    end = serialized.indexOf('#', begin + 1);
	    next = serialized.indexOf('#', end + 1);
	    key = serialized.substring(begin, end);
	    value = serialized.substring(end + 1, next);
	    map.put(key, value);
	} while (next < total);

	return map;
    }

    public String serialize() {
	final StringBuilder builder = new StringBuilder();
	for (final String key : keySet()) {
	    builder.append(key).append("#").append(get(key)).append("#");
	}
	return builder.toString();
    }

}
