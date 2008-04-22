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
package com.calclab.emite.client.extra.muc;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.CoreModule;
import com.calclab.emite.client.core.bosh.Emite;

public class MUCModule {

    private static final String COMPONENTS_MANAGER = "muc:manager";

    public static RoomManager getRoomManager(final Container components) {
	return (RoomManager) components.get(COMPONENTS_MANAGER);
    }

    public static void install(final Container container) {
	final Emite emite = CoreModule.getEmite(container);
	final MUCRoomManager rooms = new MUCRoomManager(emite);
	container.install(COMPONENTS_MANAGER, rooms);
    }
}
