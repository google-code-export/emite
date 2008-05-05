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

import java.util.List;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.*;

/**
 * REPONSABILITIES: mantains a body handle id iq's SEND
 * 
 * @author dani
 * 
 */
public class EmiteBosh implements Emite, Installable {
    public static class Events {
	public static final Event onDoSend = new Event("connection:do:send");

	public static IPacket send(final IPacket packet) {
	    return new Event(EmiteBosh.Events.onDoSend).With(packet);
	}
    }

    private final Dispatcher dispatcher;
    private final IDManager manager;
    private final IStream iStream;

    public EmiteBosh(final Dispatcher dispatcher, final IStream iStream) {
	this.dispatcher = dispatcher;
	this.iStream = iStream;
	this.manager = new IDManager();
    }

    public void addListener(final DispatcherStateListener listener) {
	dispatcher.addListener(listener);
    }

    public void attach() {
	dispatcher.subscribe(new PacketMatcher(EmiteBosh.Events.onDoSend), new PacketListener() {
	    public void handle(final IPacket received) {
		onSend(received);
	    }
	});
	dispatcher.subscribe(when("iq"), new PacketListener() {
	    public void handle(final IPacket received) {
		onIQ(received);
	    }

	});
    }

    public void install() {
	attach();
    }

    public void publish(final IPacket packet) {
	dispatcher.publish(packet);
    }

    public void send(final IPacket packet) {
	dispatcher.publish(Events.send(packet));
    }

    public String sendIQ(final String category, final IPacket packet, final PacketListener packetListener) {
	final String id = manager.register(category, packetListener);
	packet.setAttribute("id", id);
	send(packet);
	return id;
    }

    public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	dispatcher.subscribe(matcher, packetListener);
    }

    public void uninstall() {

    }

    void onIQ(final IPacket received) {
	manager.handle(received);
    }

    void onSend(final IPacket received) {
	final List<? extends IPacket> children = received.getChildren();
	for (final IPacket child : children) {
	    iStream.addResponse(child);
	}
    }

}
