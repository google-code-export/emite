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

import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.bosh3.Connection;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.services.gwt.GWTServicesModule;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.DelegatedContainer;
import com.calclab.suco.client.modules.Module;

public class Xmpp extends DelegatedContainer {

    /**
     * Ready to use Xmpp object in GWT environments
     * 
     * @return
     */
    public static Xmpp create() {
	return create(new GWTServicesModule());
    }

    /**
     * Create a Xmpp object and install the specified modules before (you need
     * to specify a ServicesModule, like GWTServicesModule first) for example:
     * <code>Xmpp.create(new GWTServicesModule());</code>
     * 
     * @param container
     * @return
     */
    public static Xmpp create(final Module... modules) {
	final Container container = Suco.create(modules);
	Suco.add(container, new EmiteModule());
	return container.getInstance(Xmpp.class);
    }

    private Session session;
    private final boolean isStarted;

    protected Xmpp(final Container container) {
	super(container);
	this.isStarted = false;
    }

    public ChatManager getChatManager() {
	getSession();
	return this.getInstance(ChatManager.class);
    }

    public PresenceManager getPresenceManager() {
	getSession();
	return getInstance(PresenceManager.class);
    }

    public Roster getRoster() {
	getSession();
	return getInstance(Roster.class);
    }

    public RosterManager getRosterManager() {
	return getInstance(RosterManager.class);
    }

    public Session getSession() {
	if (this.session == null) {
	    this.session = getInstance(Session.class);
	}
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

    public void setBoshSettings(final Bosh3Settings settings) {
	getInstance(Connection.class).setSettings(settings);
    }

    public void start() {
	getSession();
    }

    public void stop() {
	if (isStarted) {
	    logout();
	}
    }

}
