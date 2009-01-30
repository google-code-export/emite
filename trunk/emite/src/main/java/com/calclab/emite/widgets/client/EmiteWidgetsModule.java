/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client;

import com.calclab.emite.browser.client.AutoConfig;
import com.calclab.emite.browser.client.DomAssist;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
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
import com.calclab.emite.widgets.client.room.GWTRoomWidget;
import com.calclab.emite.widgets.client.room.RoomController;
import com.calclab.emite.widgets.client.room.RoomPresenceController;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.emite.widgets.client.roster.GWTRosterWidget;
import com.calclab.emite.widgets.client.roster.RosterController;
import com.calclab.emite.widgets.client.roster.RosterWidget;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.decorator.NoDecoration;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class EmiteWidgetsModule extends AbstractModule {
    public EmiteWidgetsModule() {
	super();
    }

    @Override
    protected void onInstall() {
	register(Singleton.class, new Factory<WidgetsRegistry>(WidgetsRegistry.class) {
	    @Override
	    public WidgetsRegistry create() {
		return new WidgetsRegistry($(Container.class));
	    }
	}, new Factory<AutoDeploy>(AutoDeploy.class) {
	    @Override
	    public AutoDeploy create() {
		return new AutoDeploy($(WidgetsRegistry.class), $(AutoConfig.class), $(DomAssist.class));
	    }
	});

	// composed widget registry
	register(NoDecoration.class, // conversations widget
		new Factory<ConversationsController>(ConversationsController.class) {
		    @Override
		    public ConversationsController create() {
			return new ConversationsController($(ChatManager.class), $$(ChatWidget.class));
		    }
		}, new Factory<ConversationsWidget>(ConversationsWidget.class) {
		    @Override
		    public ConversationsWidget create() {
			final ConversationsWidget widget = new ConversationsWidget();
			$(ConversationsController.class).setWidget(widget);
			return widget;
		    }
		}, // charla widget
		new Factory<ComposedController>(ComposedController.class) {
		    @Override
		    public ComposedController create() {
			return new ComposedController($(Session.class));
		    }
		}, new Factory<CharlaWidget>(CharlaWidget.class) {
		    @Override
		    public CharlaWidget create() {
			final CharlaWidget widget = new CharlaWidget($(LoginWidget.class), $(ChatWidget.class),
				$(LogoutWidget.class));
			$(ComposedController.class).setWidget(widget);
			return widget;
		    }
		});

	// simple widget registry
	register(NoDecoration.class, // login widget
		new Factory<LoginController>(LoginController.class) {
		    @Override
		    public LoginController create() {
			return new LoginController($(Session.class));
		    }
		}, new Factory<LoginWidget>(LoginWidget.class) {
		    @Override
		    public LoginWidget create() {
			final LoginWidget widget = new LoginWidget();
			$(LoginController.class).setWidget(widget);
			return widget;
		    }
		}, // logout widget
		new Factory<LogoutController>(LogoutController.class) {
		    @Override
		    public LogoutController create() {
			return new LogoutController($(Session.class));
		    }
		}, new Factory<LogoutWidget>(LogoutWidget.class) {
		    @Override
		    public LogoutWidget create() {
			final LogoutWidget widget = new LogoutWidget();
			$(LogoutController.class).setWidget(widget);
			return widget;
		    }
		}, // logger widget
		new Factory<LoggerController>(LoggerController.class) {
		    @Override
		    public LoggerController create() {
			return new LoggerController($(Connection.class));
		    }
		}, new Factory<LoggerWidget>(LoggerWidget.class) {
		    @Override
		    public LoggerWidget create() {
			final LoggerWidget widget = new LoggerWidget();
			$(LoggerController.class).setWidget(widget);
			return widget;
		    }
		}, // chat widget
		new Factory<ChatController>(ChatController.class) {
		    @Override
		    public ChatController create() {
			return new ChatController($(Session.class), $(ChatManager.class));
		    }
		}, new Factory<ChatWidget>(ChatWidget.class) {
		    @Override
		    public ChatWidget create() {
			final ChatWidget widget = new GWTChatWidget();
			$(ChatController.class).setWidget(widget);
			return widget;
		    }
		}, // roster widget
		new Factory<RosterController>(RosterController.class) {
		    @Override
		    public RosterController create() {
			return new RosterController($(Session.class), $(Roster.class));
		    }
		}, new Factory<RosterWidget>(RosterWidget.class) {
		    @Override
		    public RosterWidget create() {
			final RosterWidget widget = new GWTRosterWidget();
			$(RosterController.class).setWidget(widget);
			return widget;
		    }
		}, // room widget
		new Factory<RoomController>(RoomController.class) {
		    @Override
		    public RoomController create() {
			return new RoomController($(Session.class), $(RoomManager.class), $$(RoomPresenceWidget.class));
		    }
		}, new Factory<RoomWidget>(RoomWidget.class) {
		    @Override
		    public RoomWidget create() {
			final RoomWidget widget = new GWTRoomWidget();
			$(RoomController.class).setWidget(widget);
			return widget;
		    }
		}, // room presence widget
		new Factory<RoomPresenceController>(RoomPresenceController.class) {
		    @Override
		    public RoomPresenceController create() {
			return new RoomPresenceController($(RoomManager.class));
		    }
		}, new Factory<RoomPresenceWidget>(RoomPresenceWidget.class) {
		    @Override
		    public RoomPresenceWidget create() {
			final RoomPresenceWidget widget = new RoomPresenceWidget();
			$(RoomPresenceController.class).setWidget(widget);
			return widget;
		    }
		});
    }
}
