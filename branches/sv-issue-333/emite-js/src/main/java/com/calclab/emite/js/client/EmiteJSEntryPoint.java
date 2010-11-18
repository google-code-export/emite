package com.calclab.emite.js.client;

import org.timepedia.exporter.client.Exporter;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class EmiteJSEntryPoint implements EntryPoint {
    public void onModuleLoad() {
	Log.debug("Loading emite js...");
	try {
	    final Exporter exporter = (Exporter) GWT.create(Emite.class);
	    Log.debug("Exporter: " + exporter);
	    exporter.export();
	    Log.debug("Emite-JS ready.");
	} catch (final Exception e) {
	    Log.debug("Exception: " + e);
	}
    }
}
