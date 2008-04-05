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
package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.components.Startable;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.Packet;

public abstract class DispatcherComponent implements Startable {

	protected Dispatcher dispatcher;

	public DispatcherComponent(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public abstract void attach();

	public void start() {
		attach();
	}

	public void stop() {
	}

	public void when(final Packet packet, final PacketListener packetListener) {
		when(new PacketMatcher(packet), packetListener);
	}

	public void when(final String packetName, final PacketListener packetListener) {
		when(new PacketMatcher(packetName), packetListener);
	}

	private void when(final PacketMatcher matcher, final PacketListener packetListener) {
		dispatcher.subscribe(matcher, packetListener);
	}
}
