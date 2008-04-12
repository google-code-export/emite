/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.DefaultContainer;
import com.calclab.emite.client.core.CorePlugin;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherMonitor;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.gwt.GWTServicesPlugin;
import com.calclab.emite.client.extra.muc.MUCPlugin;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.chat.ChatPlugin;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.presence.PresencePlugin;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterPlugin;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionPlugin;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class Xmpp {

    public static Xmpp create(final BoshOptions options) {
        final DefaultContainer c = new DefaultContainer();
        GWTServicesPlugin.install(c);
        return new Xmpp(c, options, new DispatcherMonitor() {
            public void publishing(final IPacket packet) {
                Log.debug("dispatching: " + packet.toString());
            }
        });
    }

    private final Container container;
    private final Session session;
    private boolean isStarted;

    public Xmpp(final Container container, final BoshOptions options, final DispatcherMonitor monitor) {
        this.isStarted = false;
        this.container = container;
        Plugins.installDefaultPlugins(container, options, monitor);
        this.session = SessionPlugin.getSession(container);
    }

    public ChatManager getChatManager() {
        return ChatPlugin.getChat(container);
    }

    public Container getComponents() {
        return container;
    }

    public Dispatcher getDispatcher() {
        return CorePlugin.getDispatcher(container);
    }

    public PresenceManager getPresenceManager() {
        return PresencePlugin.getManager(container);
    }

    // FIXME: Dani, revisar (añadi esto para poder mockear igual que con el
    // resto...)
    public RoomManager getRoomManager() {
        return MUCPlugin.getRoomManager(container);
    }

    public Roster getRoster() {
        return RosterPlugin.getRoster(container);
    }

    public RosterManager getRosterManager() {
        return RosterPlugin.getRosterManager(container);
    }

    public Session getSession() {
        return session;
    }

    public void login(final XmppURI uri, final String password, final Presence.Show show, final String status) {
        start();
        session.login(uri, password);
        getPresenceManager().setOwnPresence(status, show);
    }

    public void logout() {
        session.logout();
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;
            container.onStartComponent();
        }
    }

    public void stop() {
        if (isStarted) {
            logout();
            container.onStopComponent();
        }
    }

}
