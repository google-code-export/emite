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
package com.calclab.emite.client.im;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

public class InstantMessagingModule extends AbstractModule {

    public InstantMessagingModule() {
	super(InstantMessagingModule.class);
    }

    @Override
    public void onLoad() {
	// FIXME: Roster should not be singleton!!!
	register(SingletonScope.class, new Factory<Roster>(Roster.class) {
	    public Roster create() {
		return new Roster();
	    }
	}, new Factory<ChatManager>(ChatManager.class) {
	    public ChatManagerDefault create() {
		return new ChatManagerDefault($(Session.class));
	    }
	}, new Factory<RosterManager>(RosterManager.class) {
	    public RosterManager create() {
		return new RosterManager($(Session.class), $(Roster.class));
	    }
	}, new Factory<PresenceManager>(PresenceManager.class) {
	    public PresenceManager create() {
		return new PresenceManager($(Session.class), $(Roster.class));
	    }
	});

    }
}
