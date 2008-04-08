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
package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.IPacket;

public class Presence extends BasicStanza {

    public static enum Show {
	available, away, chat, dnd, xa
    }

    public enum Type {
	available, error, probe, subscribe, subscribed, unavailable, unsubscribe, unsubscribed
    }

    public Presence() {
	this(null, (String) null, (String) null);
    }

    public Presence(final IPacket stanza) {
	super(stanza);
    }

    public Presence(final Type type, final String from, final String to) {
	super("presence", "jabber:client");
	if (type != null) {
	    setType(type.toString());
	}
	if (from != null) {
	    setFrom(from.toString());
	}
	if (to != null) {
	    setTo(to.toString());
	}
    }

    public Presence(final Type type, final XmppURI from, final XmppURI to) {
	this(type, from != null ? from.toString() : null, to != null ? to.toString() : null);
    }

    public Presence(final XmppURI from) {
	this(null, from.toString(), null);
    }

    public int getPriority() {
	int value = 0;
	final IPacket priority = getFirstChild("priority");
	if (priority != null) {
	    try {
		value = Integer.parseInt(priority.getText());
	    } catch (final NumberFormatException e) {
		value = 0;
	    }
	}
	return value;
    }

    public Show getShow() {
	final IPacket show = getFirstChild("show");
	final String value = show != null ? show.getText() : null;
	return value != null ? Show.valueOf(value) : null;
    }

    public String getStatus() {
	final IPacket status = getFirstChild("status");
	return status != null ? status.getText() : null;
    }

    // TODO: revisar esto (type == null -> available)
    // http://www.xmpp.org/rfcs/rfc3921.html#presence
    public Type getType() {
	final String type = getAttribute(BasicStanza.TYPE);
	return type != null ? Type.valueOf(type) : Type.available;
    }

    public void setPriority(final int value) {
	IPacket priority = getFirstChild("priority");
	if (priority == null) {
	    priority = add("priority", null);
	}
	priority.setText(Integer.toString(value >= 0 ? value : 0));
    }

    public void setShow(final Show value) {
	IPacket show = getFirstChild("show");
	if (show == null) {
	    show = add("show", null);
	}
	show.setText(value.toString());
    }

    public void setStatus(final String statusMessage) {
	IPacket status = getFirstChild("status");
	if (status == null) {
	    status = add("status", null);
	}
	status.setText(statusMessage);
    }

    public Presence With(final Show value) {
	setShow(value);
	return this;
    }

}
