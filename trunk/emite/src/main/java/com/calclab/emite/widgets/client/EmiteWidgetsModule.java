package com.calclab.emite.widgets.client;

import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.widgets.client.deploy.Deployer;
import com.calclab.emite.widgets.client.logger.LoggerController;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginController;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.room.RoomController;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.NoScope;
import com.calclab.suco.client.scopes.SingletonScope;

public class EmiteWidgetsModule extends AbstractModule {
    public EmiteWidgetsModule() {
	super(EmiteWidgetsModule.class);
    }

    @Override
    protected void onLoad() {
	register(SingletonScope.class, new Factory<Deployer>(Deployer.class) {
	    public Deployer create() {
		return new Deployer();
	    }
	});

	register(NoScope.class, // login widget
		new Factory<LoginController>(LoginController.class) {
		    public LoginController create() {
			return new LoginController($(Session.class));
		    }
		}, new Factory<LoginWidget>(LoginWidget.class) {
		    public LoginWidget create() {
			final LoginWidget widget = new LoginWidget();
			$(LoginController.class).setWidget(widget);
			return widget;
		    }
		}, // logger widget
		new Factory<LoggerController>(LoggerController.class) {
		    public LoggerController create() {
			return new LoggerController($(Connection.class));
		    }
		}, new Factory<LoggerWidget>(LoggerWidget.class) {
		    public LoggerWidget create() {
			final LoggerWidget widget = new LoggerWidget();
			$(LoggerController.class).setWidget(widget);
			return widget;
		    }
		}, new Factory<RoomController>(RoomController.class) {
		    public RoomController create() {
			return new RoomController($(Session.class), $(RoomManager.class));
		    }
		}, new Factory<RoomWidget>(RoomWidget.class) {
		    public RoomWidget create() {
			final RoomWidget widget = new RoomWidget();
			$(RoomController.class).setWidget(widget);
			return widget;
		    }
		});
    }

}
