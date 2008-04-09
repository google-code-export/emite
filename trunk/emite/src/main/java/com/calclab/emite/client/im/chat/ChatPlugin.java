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
package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class ChatPlugin {
    private static final String COMPONENT_CHAT = "chat";

    public static ChatManagerDefault getChat(final Container container) {
	return (ChatManagerDefault) container.get(COMPONENT_CHAT);
    }

    public static void install(final Container container) {
	final Emite emite = BoshPlugin.getEmite(container);
	final Globals globals = ContainerPlugin.getGlobals(container);
	final ChatManagerDefault chatManagerDefault = new ChatManagerDefault(emite, globals);
	container.install(COMPONENT_CHAT, chatManagerDefault);
    }

}
