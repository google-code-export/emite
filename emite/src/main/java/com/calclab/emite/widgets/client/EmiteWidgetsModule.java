package com.calclab.emite.widgets.client;

import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.browser.client.PageController;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.emite.widgets.client.base.ComposedController;
import com.calclab.emite.widgets.client.chat.CharlaWidget;
import com.calclab.emite.widgets.client.chat.ChatController;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.calclab.emite.widgets.client.chat.GWTChatWidget;
import com.calclab.emite.widgets.client.habla.ConversationsController;
import com.calclab.emite.widgets.client.habla.ConversationsWidget;
import com.calclab.emite.widgets.client.logger.LoggerController;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginController;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutController;
import com.calclab.emite.widgets.client.logout.LogoutWidget;
import com.calclab.emite.widgets.client.room.ComentaWidget;
import com.calclab.emite.widgets.client.room.GWTRoomWidget;
import com.calclab.emite.widgets.client.room.RoomController;
import com.calclab.emite.widgets.client.room.RoomPresenceController;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.emite.widgets.client.roster.GWTRosterWidget;
import com.calclab.emite.widgets.client.roster.RosterController;
import com.calclab.emite.widgets.client.roster.RosterWidget;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.NoScope;
import com.calclab.suco.client.scope.SingletonScope;

public class EmiteWidgetsModule extends AbstractModule {
    public EmiteWidgetsModule() {
	super();
    }

    @Override
    protected void onLoad() {
	register(SingletonScope.class, new Factory<WidgetsRegistry>(WidgetsRegistry.class) {
	    public WidgetsRegistry create() {
		return new WidgetsRegistry($(Container.class));
	    }
	}, new Factory<AutoDeploy>(AutoDeploy.class) {
	    public AutoDeploy create() {
		return new AutoDeploy($(WidgetsRegistry.class), $(PageController.class), $(DomAssist.class));
	    }
	});

	// composed widget registry
	register(NoScope.class, // conversations widget
		new Factory<ConversationsController>(ConversationsController.class) {
		    public ConversationsController create() {
			return new ConversationsController($(ChatManager.class), $$(ChatWidget.class));
		    }
		}, new Factory<ConversationsWidget>(ConversationsWidget.class) {
		    public ConversationsWidget create() {
			final ConversationsWidget widget = new ConversationsWidget();
			$(ConversationsController.class).setWidget(widget);
			return widget;
		    }
		}, // charla widget
		new Factory<ComposedController>(ComposedController.class) {
		    public ComposedController create() {
			return new ComposedController($(Session.class));
		    }
		}, new Factory<CharlaWidget>(CharlaWidget.class) {
		    public CharlaWidget create() {
			final CharlaWidget widget = new CharlaWidget($(LoginWidget.class), $(ChatWidget.class),
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
		}, new Factory<ChatWidget>(ChatWidget.class) {
		    public ChatWidget create() {
			final ChatWidget widget = new GWTChatWidget();
			$(ChatController.class).setWidget(widget);
			return widget;
		    }
		}, // roster widget
		new Factory<RosterController>(RosterController.class) {
		    public RosterController create() {
			return new RosterController($(Session.class), $(XRosterManager.class));
		    }
		}, new Factory<RosterWidget>(RosterWidget.class) {
		    public RosterWidget create() {
			final RosterWidget widget = new GWTRosterWidget();
			$(RosterController.class).setWidget(widget);
			return widget;
		    }
		}, // room widget
		new Factory<RoomController>(RoomController.class) {
		    public RoomController create() {
			return new RoomController($(Session.class), $(RoomManager.class), $$(RoomPresenceWidget.class));
		    }
		}, new Factory<RoomWidget>(RoomWidget.class) {
		    public RoomWidget create() {
			final RoomWidget widget = new GWTRoomWidget();
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
