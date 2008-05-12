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

import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class InstantMessagingModule implements Module {
    private static final Class<ChatManagerDefault> COMPONENT_CHAT = ChatManagerDefault.class;
    private static final Class<Roster> COMPONENT_ROSTER = Roster.class;
    private static final Class<RosterManager> COMPONENT_ROSTER_MANAGER = RosterManager.class;
    private static final Class<PresenceManager> COMPONENT_MANAGER = PresenceManager.class;

    public static ChatManagerDefault getChat(final Container container) {
	return container.getInstance(COMPONENT_CHAT);
    }

    public static PresenceManager getManager(final Container container) {
	return container.getInstance(COMPONENT_MANAGER);
    }

    public static Roster getRoster(final Container container) {
	return container.getInstance(COMPONENT_ROSTER);
    }

    public static RosterManager getRosterManager(final Container container) {
	return container.getInstance(COMPONENT_ROSTER_MANAGER);
    }

    public void load(final Container container) {
	final Emite emite = CoreModule.getEmite(container);
	final ChatManagerDefault chatManagerDefault = new ChatManagerDefault(emite);
	container.register(COMPONENT_CHAT, chatManagerDefault);

	final Roster roster = new Roster();
	final RosterManager rosterManager = new RosterManager(emite, roster);
	container.register(COMPONENT_ROSTER, roster);
	container.register(COMPONENT_ROSTER_MANAGER, rosterManager);

	final PresenceManager manager = new PresenceManager(emite);
	container.register(COMPONENT_MANAGER, manager);
    }

}
