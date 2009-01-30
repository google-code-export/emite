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

import java.util.HashMap;
import java.util.Set;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.chat.CharlaWidget;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.calclab.emite.widgets.client.habla.ConversationsWidget;
import com.calclab.emite.widgets.client.logger.LoggerWidget;
import com.calclab.emite.widgets.client.login.LoginWidget;
import com.calclab.emite.widgets.client.logout.LogoutWidget;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget;
import com.calclab.emite.widgets.client.room.RoomWidget;
import com.calclab.emite.widgets.client.roster.RosterWidget;
import com.calclab.suco.client.ioc.Container;
import com.calclab.suco.client.ioc.Provider;

public class WidgetsRegistry {
    private final HashMap<String, Class<? extends EmiteWidget>> registry;
    private final Container container;

    public WidgetsRegistry(final Container container) {
	this.container = container;
	this.registry = new HashMap<String, Class<? extends EmiteWidget>>();
	initDefaults();
    }

    public Set<String> getClasses() {
	return registry.keySet();
    }

    public void register(final String htmlClass, final Class<? extends EmiteWidget> widgetClass) {
	registry.put(htmlClass, widgetClass);
    }

    Provider<? extends EmiteWidget> getProvider(final String divClass) {
	return container.getProvider(registry.get(divClass));
    }

    private void initDefaults() {
	register("emite-widget-conversations", ConversationsWidget.class);
	register("emite-widget-charla", CharlaWidget.class);
	register("emite-widget-logger", LoggerWidget.class);
	register("emite-widget-login", LoginWidget.class);
	register("emite-widget-chat", ChatWidget.class);
	register("emite-widget-roster", RosterWidget.class);
	register("emite-widget-room", RoomWidget.class);
	register("emite-widget-room-presence", RoomPresenceWidget.class);
	register("emite-widget-logout", LogoutWidget.class);
    }

}
