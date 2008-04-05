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

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.XMLService;

public class EmiteBosh extends DispatcherComponent implements Emite {
    public static class Events {
	public static final Event send = new Event("connection:do:send");
    }

    private Body body;

    private long rid;

    private final XMLService xmler;

    private final boolean isDispatching;

    public EmiteBosh(final Dispatcher dispatcher, final XMLService xmler) {
	super(dispatcher);
	this.xmler = xmler;
	isDispatching = false;

	// dispatcher.addListener(new DispatcherStateListener() {
	// public void afterDispatching() {
	// isDispatching = false;
	// }
	//
	// public void beforeDispatching() {
	// isDispatching = true;
	// }
	// });

	clear();
    }

    @Override
    public void attach() {
	when(EmiteBosh.Events.send, new PacketListener() {
	    public void handle(final Packet received) {
		final List<? extends Packet> children = received.getChildren();
		for (final Packet child : children) {
		    send(child);
		}
	    }
	});
    }

    public Packet bodyFromResponse(final String content) {
	return xmler.toXML(content);
    }

    public void clear() {
	rid = (long) (Math.random() * 1245234);
	this.body = null;
    }

    public void clearBody() {
	body = null;
    }

    public Body getBody() {
	return body;
    }

    public Dispatcher getDispatcher() {
	return dispatcher;
    }

    public String getResponse() {
	return xmler.toString(body);
    }

    public void initBody(final String sid) {
	if (this.body == null) {
	    rid++;
	    body = new Body(rid, sid);
	}
    }

    public void publish(final Event event) {
	dispatcher.publish(event);
    }

    public void send(final Packet packet) {
	body.addChild(packet);
    }

}
