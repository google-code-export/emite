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
package com.calclab.emite.im.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.ChatManagerImpl;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.presence.PresenceManagerImpl;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterManager;
import com.calclab.emite.im.client.roster.RosterManagerImpl;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;
import com.google.gwt.core.client.EntryPoint;

/**
 * <p>
 * Implementation of the RFC-3921
 * </p>
 * <p>
 * This module exports the following components:
 * </p>
 * <ul>
 * <li>ChatManager: PURPOSE-FIXME</li>
 * <li>RosterManager: FIXME purpose...</li>
 * <li>PresenceManager: FIXME purpose</li>
 * </ul>
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html
 */
public class InstantMessagingModule extends AbstractModule implements EntryPoint {

    public InstantMessagingModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<Roster>(Roster.class) {
	    public Roster create() {
		return new Roster();
	    }
	}, new Factory<ChatManager>(ChatManager.class) {
	    public ChatManagerImpl create() {
		return new ChatManagerImpl($(Session.class));
	    }
	}, new Factory<RosterManager>(RosterManager.class) {
	    public RosterManager create() {
		return new RosterManagerImpl($(Session.class), $(Roster.class));
	    }
	}, new Factory<PresenceManager>(PresenceManager.class) {
	    public PresenceManager create() {
		return new PresenceManagerImpl($(Session.class), $(RosterManager.class));
	    }
	});

    }

    public void onModuleLoad() {
	Suco.install(this);
    }
}
