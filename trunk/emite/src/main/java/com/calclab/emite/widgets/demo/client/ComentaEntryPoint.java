package com.calclab.emite.widgets.demo.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.widgets.client.comenta.ComentaWidget;
import com.calclab.emite.widgets.client.installer.Installer;
import com.calclab.emite.widgets.client.installer.Settings;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;

public class ComentaEntryPoint implements EntryPoint {
    public void onModuleLoad() {
	final Container container = Suco.create(new GWTServicesModule(), new EmiteModule(), new ComentaModule());
	final Installer installer = container.getInstance(Installer.class);
	GWT.log("Installer: " + (installer != null), null);
	final Connection connection = container.getInstance(Connection.class);
	installer.setConnectionSettings(connection);

	final Settings divSettings = installer.getData(DOM.getElementById("emite:widgets:comenta"), "data-comenta-",
		new Settings("login", "room"));

	installer.install("emite:widgets:comenta", container.getInstance(ComentaWidget.class));

    }
}
