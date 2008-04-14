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
package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.components.Startable;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.IPacket;

public abstract class EmiteComponent implements Startable {
    protected final Emite emite;

    public EmiteComponent(final Emite emite) {
	this.emite = emite;
    }

    public abstract void attach();

    public void onStartComponent() {
	attach();
    }

    public void onStopComponent() {
    }

    public void when(final IPacket packet, final PacketListener packetListener) {
	when(new PacketMatcher(packet), packetListener);
    }

    public void when(final Matcher matcher, final PacketListener packetListener) {
	emite.subscribe(matcher, packetListener);
    }

    public void when(final String packetName, final PacketListener packetListener) {
	when(new PacketMatcher(packetName), packetListener);
    }

}
