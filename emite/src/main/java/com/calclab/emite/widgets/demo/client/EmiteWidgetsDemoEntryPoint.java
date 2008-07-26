package com.calclab.emite.widgets.demo.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.widgets.client.EmiteWidgetsModule;
import com.calclab.emite.widgets.client.deploy.Deployer;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;

public class EmiteWidgetsDemoEntryPoint implements EntryPoint {
    public void onModuleLoad() {
	final Container container = Suco.create(new GWTServicesModule(), new EmiteModule(), new EmiteWidgetsModule());

	final Deployer deployer = container.getInstance(Deployer.class);
	final Connection connection = container.getInstance(Connection.class);
	deployer.setConnectionSettings(connection);

	final ArrayList<Element> elements = deployer.findElementsByClass("emite.widgets.login");
	for (final Element e : elements) {
	    Log.debug("Element by class: " + e.getId());
	}

	deployer.deploy("emite.widgets.login", LoginWidget.class, container);
    }
}
