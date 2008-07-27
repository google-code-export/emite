package com.calclab.emite.widgets.demo.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emite.widgets.client.EmiteWidgetsModule;
import com.calclab.emite.widgets.client.deploy.AutoDeploy;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.google.gwt.core.client.EntryPoint;

public class EmiteWidgetsDemoEntryPoint implements EntryPoint {
    public void onModuleLoad() {
	final Container container = Suco.create(new GWTServicesModule(), new EmiteModule(), new MUCModule(),
		new EmiteWidgetsModule());

	container.getInstance(AutoDeploy.class);

    }
}
