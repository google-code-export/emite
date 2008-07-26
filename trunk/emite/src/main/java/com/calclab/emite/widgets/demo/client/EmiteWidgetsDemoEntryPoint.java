package com.calclab.emite.widgets.demo.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emite.widgets.client.EmiteWidgetsModule;
import com.calclab.emite.widgets.client.deploy.Deployer;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.google.gwt.core.client.EntryPoint;

public class EmiteWidgetsDemoEntryPoint implements EntryPoint {
    public void onModuleLoad() {
	final Container container = Suco.create(new GWTServicesModule(), new EmiteModule(), new MUCModule(),
		new EmiteWidgetsModule());

	final Deployer deployer = container.getInstance(Deployer.class);
	final Connection connection = container.getInstance(Connection.class);
	deployer.setConnectionSettings(connection);

	deployer.deploy("emite-widget-login", LoginWidget.class, container);
	deployer.deploy("emite-widget-room", RoomWidget.class, container);
	deployer.deploy("emite-widget-logger", LoggerWidget.class, container);
    }
}
