package com.calclab.emite.widgets.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.suco.client.Suco;
import com.google.gwt.core.client.EntryPoint;

public class EmiteWidgetsEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	Log.debug("Emite widgets loading...");
	Suco.install(new EmiteWidgetsModule());

	Log.debug("Emite widgets deploying...");
	Suco.get(AutoDeploy.class);
	Log.debug("Emite widgets ready.");

    }

}
