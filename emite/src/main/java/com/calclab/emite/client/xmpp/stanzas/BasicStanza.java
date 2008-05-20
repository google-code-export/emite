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

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.core.packet.DelegatedPacket;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.NoPacket;
import com.calclab.emite.client.core.packet.Packet;

public class BasicStanza extends DelegatedPacket implements Stanza {
    protected static final String TYPE = "type";
    private static final String FROM = "from";
    private static final String ID = "id";
    private static final String TO = "to";

    public BasicStanza(final IPacket stanza) {
	super(stanza);
    }

    public BasicStanza(final String name, final String xmlns) {
	super(new Packet(name, xmlns));
    }

    public String getFrom() {
	return getAttribute(FROM);
    }

    public XmppURI getFromURI() {
	return uri(getFrom());
    }

    public String getId() {
	return getAttribute(ID);
    }

    public String getTo() {
	return getAttribute(TO);
    }

    public XmppURI getToURI() {
	return uri(getTo());
    }

    @Deprecated
    public void setFrom(final String from) {
	setAttribute(FROM, from);
    }

    public void setFrom(final XmppURI from) {
	setFrom(from != null ? from.toString() : null);
    }

    public void setId(final String id) {
	setAttribute(ID, id);
    }

    @Deprecated
    public void setTo(final String to) {
	setAttribute(TO, to);
    }

    public void setTo(final XmppURI to) {
	setTo(to != null ? to.toString() : null);
    }

    public void setType(final String type) {
	setAttribute(TYPE, type);
    }

    public Stanza To(final String to) {
	setTo(to);
	return this;
    }

    /**
     * Add a child with a specified text. Create the child if not exists. If the
     * text is null, then removes the child
     * 
     * @param nodeName
     * @param text
     */
    protected void setTextToChild(final String nodeName, final String text) {
	if (text != null) {
	    IPacket node = getFirstChild(nodeName);
	    if (node == NoPacket.INSTANCE) {
		node = this.addChild(nodeName, null);
	    }
	    node.setText(text);
	} else {
	    removeChild(getFirstChild(nodeName));
	}
    }
}
