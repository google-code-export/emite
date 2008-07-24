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
import com.calclab.emite.client.xmpp.session.ISession;
import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;
import com.calclab.suco.client.scopes.SingletonScope;

public class InstantMessagingModule implements Module {
    public Class<? extends Module> getType() {
	return InstantMessagingModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(Roster.class, new Provider<Roster>() {
	    public Roster get() {
		return new Roster();
	    }
	}, SingletonScope.class);

	builder.registerProvider(ChatManager.class, new Provider<ChatManager>() {
	    public ChatManagerDefault get() {
		return new ChatManagerDefault(builder.getInstance(ISession.class));
	    }
	}, SessionScope.class);

	builder.registerProvider(RosterManager.class, new Provider<RosterManager>() {
	    public RosterManager get() {
		return new RosterManager(builder.getInstance(ISession.class), builder.getInstance(Roster.class));
	    }
	}, SessionScope.class);

	builder.registerProvider(PresenceManager.class, new Provider<PresenceManager>() {
	    public PresenceManager get() {
		return new PresenceManager(builder.getInstance(ISession.class), builder.getInstance(Roster.class));
	    }
	}, SessionScope.class);

    }
}
