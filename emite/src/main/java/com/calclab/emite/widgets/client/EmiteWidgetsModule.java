package com.calclab.emite.widgets.client;

import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.widgets.client.deploy.AutoDeploy;
import com.calclab.emite.widgets.client.deploy.Deployer;
import com.calclab.emite.widgets.client.logger.LoggerController;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginController;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.room.RoomController;
import com.calclab.emite.widgets.client.room.RoomPresenceController;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.suco.client.container.Container;
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
		return new Deployer($(Container.class));
	    }
	}, new Factory<AutoDeploy>(AutoDeploy.class) {
	    public AutoDeploy create() {
		return new AutoDeploy($(Connection.class), $(Session.class), $(Deployer.class));
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
		}, // room widget
		new Factory<RoomController>(RoomController.class) {
		    public RoomController create() {
			return new RoomController($(Session.class), $(RoomManager.class));
		    }
		}, new Factory<RoomWidget>(RoomWidget.class) {
		    public RoomWidget create() {
			final RoomWidget widget = new RoomWidget();
			$(RoomController.class).setWidget(widget);
			return widget;
		    }
		}, // room presence widget
		new Factory<RoomPresenceController>(RoomPresenceController.class) {
		    public RoomPresenceController create() {
			return new RoomPresenceController($(RoomManager.class));
		    }
		}, new Factory<RoomPresenceWidget>(RoomPresenceWidget.class) {
		    public RoomPresenceWidget create() {
			final RoomPresenceWidget widget = new RoomPresenceWidget();
			$(RoomPresenceController.class).setWidget(widget);
			return widget;
		    }
		});
    }

}
