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

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;

public class InstantMessagingModule implements Module {
    public Class<? extends Module> getType() {
	return InstantMessagingModule.class;
    }

    public void onLoad(final Container container) {
	container.registerProvider(ChatManager.class, new Provider<ChatManager>() {
	    public ChatManagerDefault get() {
		return new ChatManagerDefault(container.getInstance(Emite.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(Roster.class, new Provider<Roster>() {
	    public Roster get() {
		return new Roster();
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(RosterManager.class, new Provider<RosterManager>() {
	    public RosterManager get() {
		return new RosterManager(container.getInstance(Emite.class), container.getInstance(Roster.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(PresenceManager.class, new Provider<PresenceManager>() {
	    public PresenceManager get() {
		return new PresenceManager(container.getInstance(Emite.class));
	    }
	}, Scopes.SINGLETON_EAGER);

    }

}
