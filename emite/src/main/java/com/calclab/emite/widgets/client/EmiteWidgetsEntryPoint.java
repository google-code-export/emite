package com.calclab.emite.widgets.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.browser.client.BrowserModule;
import com.calclab.emite.core.client.EmiteModule;
import com.calclab.emite.core.client.services.gwt.GWTServicesModule;
import com.calclab.emite.xep.muc.client.MUCModule;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.google.gwt.core.client.EntryPoint;

public class EmiteWidgetsEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	Log.debug("Emite widgets loading...");
	final Container container = Suco.create(new GWTServicesModule(), new EmiteModule(), new MUCModule(),
		new BrowserModule(), new EmiteWidgetsModule());

	Log.debug("Emite widgets deploying...");
	container.getInstance(AutoDeploy.class);
	Log.debug("Emite widgets ready.");

    }

}
