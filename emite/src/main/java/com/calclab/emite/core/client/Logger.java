package com.calclab.emite.core.client;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;

public class Logger {

    private static boolean isFirst;

    public static void debug(final String message) {
	Log.debug(message);
    }

    public static void debug(final String name, final Collection<?> collection) {
	final StringBuilder builder = new StringBuilder(name + " {");
	isFirst = true;
	for (final Object o : collection) {
	    if (isFirst)
		isFirst = false;
	    else
		builder.append(", ");
	    builder.append(o.toString());
	}
	builder.append("}");
	Log.debug(builder.toString());
    }
}
