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

public class Message extends BasicStanza {
    public static enum Type {
	chat, error, groupchat, headlines, normal, notSpecified, unknown
    }

    private static final String TYPE_CHAT = "chat";

    public Message(final IPacket iPacket) {
	super(iPacket);
    }

    public Message(final String from, final String to, final String msg) {
	super("message", "jabber:client");
	setType(TYPE_CHAT);
	setFrom(from);
	setTo(to);
	setMessage(msg);
    }

    public Message(final XmppURI fromUri, final XmppURI toURI, final String message) {
	this(fromUri.toString(), toURI.toString(), message);
    }

    public String getBody() {
	return getFirstChild("body").getText();
    }

    public String getThread() {
	final IPacket thread = getFirstChild("thread");
	return thread != null ? thread.getText() : null;
    }

    public Type getType() {
	final String type = getAttribute(TYPE);
	try {
	    return type != null ? Type.valueOf(type) : Type.notSpecified;
	} catch (final IllegalArgumentException e) {
	    return Type.unknown;
	}
    }

    public void setType(final Type type) {
	setType(type.toString());
    }

    public Message Thread(final String thread) {
	setThread(thread);
	return this;
    }

    private void setMessage(final String msg) {
	final IPacket body = add("body", null);
	body.setText(msg);
    }

    private void setThread(final String thread) {
	super.setTextToChild("thread", thread);

    }
}
