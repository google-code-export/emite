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

import com.calclab.emite.client.components.DelegatedContainer;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.HashContainer;
import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.services.gwt.GWTServicesModule;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.MUCModule;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.im.InstantMessagingModule;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.XMPPModule;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class Xmpp extends DelegatedContainer {

    public static Xmpp create(final BoshOptions options) {
	final HashContainer c = new HashContainer();
	GWTServicesModule.load(c);
	return new Xmpp(c, options);
    }

    private Session session;
    private boolean isStarted;

    public Xmpp(final Container container, final BoshOptions options) {
	super(container);
	this.isStarted = false;
	this.session = null;
	installDefaultPlugins(container, options);
    }

    public ChatManager getChatManager() {
	return InstantMessagingModule.getChat(this);
    }

    public Dispatcher getDispatcher() {
	return CoreModule.getDispatcher(this);
    }

    public PresenceManager getPresenceManager() {
	return InstantMessagingModule.getManager(this);
    }

    public RoomManager getRoomManager() {
	return MUCModule.getRoomManager(this);
    }

    public Roster getRoster() {
	return InstantMessagingModule.getRoster(this);
    }

    public RosterManager getRosterManager() {
	return InstantMessagingModule.getRosterManager(this);
    }

    public Session getSession() {
	if (session == null) {
	    session = XMPPModule.getSession(this);
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

    public void start() {
	if (!isStarted) {
	    isStarted = true;
	    this.install();
	}
    }

    public void stop() {
	if (isStarted) {
	    logout();
	}
    }

    private void installDefaultPlugins(final Container container, final BoshOptions options) {
	CoreModule.load(container, options);
	XMPPModule.load(container);
	InstantMessagingModule.load(container);
	// TODO: not here!
	MUCModule.install(container);
	AvatarModule.load(container);
    }

}
