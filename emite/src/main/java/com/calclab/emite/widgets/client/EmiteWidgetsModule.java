package com.calclab.emite.widgets.client;

import com.calclab.emite.client.browser.DomAssist;
import com.calclab.emite.client.browser.PageController;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.widgets.client.base.ComposedController;
import com.calclab.emite.widgets.client.chat.CharlaWidget;
import com.calclab.emite.widgets.client.chat.ChatController;
import com.calclab.emite.widgets.client.chat.GWTChatWidget;
import com.calclab.emite.widgets.client.logger.LoggerController;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginController;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutController;
import com.calclab.emite.widgets.client.logout.LogoutWidget;
import com.calclab.emite.widgets.client.room.ComentaWidget;
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
	register(SingletonScope.class, new Factory<AutoDeploy>(AutoDeploy.class) {
	    public AutoDeploy create() {
		return new AutoDeploy($(Container.class), $(PageController.class), $(DomAssist.class));
	    }
	});

	// composed widget registry
	register(NoScope.class, // charla widget
		new Factory<ComposedController>(ComposedController.class) {
		    public ComposedController create() {
			return new ComposedController($(Session.class));
		    }
		}, new Factory<CharlaWidget>(CharlaWidget.class) {
		    public CharlaWidget create() {
			final CharlaWidget widget = new CharlaWidget($(LoginWidget.class), $(GWTChatWidget.class),
				$(LogoutWidget.class));
			$(ComposedController.class).setWidget(widget);
			return widget;
		    }
		}, new Factory<ComentaWidget>(ComentaWidget.class) {
		    public ComentaWidget create() {
			final ComentaWidget widget = new ComentaWidget($(LoginWidget.class), $(RoomWidget.class));
			$(ComposedController.class).setWidget(widget);
			return widget;
		    }
		});

	// simple widget registry
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
		}, // logout widget
		new Factory<LogoutController>(LogoutController.class) {
		    public LogoutController create() {
			return new LogoutController($(Session.class));
		    }
		}, new Factory<LogoutWidget>(LogoutWidget.class) {
		    public LogoutWidget create() {
			final LogoutWidget widget = new LogoutWidget();
			$(LogoutController.class).setWidget(widget);
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
		}, // chat widget
		new Factory<ChatController>(ChatController.class) {
		    public ChatController create() {
			return new ChatController($(Session.class), $(ChatManager.class));
		    }
		}, new Factory<GWTChatWidget>(GWTChatWidget.class) {
		    public GWTChatWidget create() {
			final GWTChatWidget widget = new GWTChatWidget();
			$(ChatController.class).setWidget(widget);
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
