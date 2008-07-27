package com.calclab.emite.widgets.client.deploy;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;

public class AutoDeploy {
    public AutoDeploy(final Connection connection, final Session session, final Deployer deployer) {
	autoLogout(session);
	configureConnectionSettings(connection, deployer);
	deployWidgets(deployer);
    }

    private void autoLogout(final Session session) {
	Window.addWindowCloseListener(new WindowCloseListener() {
	    public void onWindowClosed() {
		session.logout();
	    }

	    public String onWindowClosing() {
		return null;
	    }

	});
    }

    private void configureConnectionSettings(final Connection connection, final Deployer deployer) {
	final Settings boshSettings = deployer.getMeta("emite.bosh.", new Settings("httpBase", "host"));
	Log.debug("Bosh settings: " + boshSettings.toString());
	connection.setSettings(new Bosh3Settings(boshSettings.get("httpBase"), boshSettings.get("host")));
    }

    private void deployWidgets(final Deployer deployer) {
	deployer.deploy("emite-widget-logger", LoggerWidget.class);
	deployer.deploy("emite-widget-login", LoginWidget.class);
	deployer.deploy("emite-widget-room", RoomWidget.class);
	deployer.deploy("emite-widget-room-presence", RoomPresenceWidget.class);
    }
}
