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

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.CorePlugin;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;

public class InstantMessagingModule {
    private static final String COMPONENT_CHAT = "chat";

    private static final String COMPONENT_ROSTER = "roster";

    private static final String COMPONENT_ROSTER_MANAGER = "roster:manager";

    private static final String COMPONENT_MANAGER = "presence:manager";

    public static ChatManagerDefault getChat(final Container container) {
	return (ChatManagerDefault) container.get(COMPONENT_CHAT);
    }

    public static PresenceManager getManager(final Container container) {
	return (PresenceManager) container.get(COMPONENT_MANAGER);
    }

    public static Roster getRoster(final Container container) {
	return (Roster) container.get(COMPONENT_ROSTER);
    }

    public static RosterManager getRosterManager(final Container container) {
	return (RosterManager) container.get(COMPONENT_ROSTER_MANAGER);
    }

    public static void install(final Container container) {
	final Emite emite = CorePlugin.getEmite(container);
	final ChatManagerDefault chatManagerDefault = new ChatManagerDefault(emite);
	container.install(COMPONENT_CHAT, chatManagerDefault);

	final Roster roster = new Roster();
	final RosterManager rosterManager = new RosterManager(emite, roster);
	container.register(COMPONENT_ROSTER, roster);
	container.install(COMPONENT_ROSTER_MANAGER, rosterManager);

	final PresenceManager manager = new PresenceManager(emite);
	container.install(COMPONENT_MANAGER, manager);
    }

}
