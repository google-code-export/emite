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

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class Stream {
    private Packet body;
    private long requestID;

    public Stream() {
	body = null;
    }

    public void addResponse(final IPacket child) {
	body.addChild(child);
    }

    public Packet clearBody() {
	final Packet result = body;
	body = null;
	return result;
    }

    public boolean isEmpty() {
	return body.getChildrenCount() == 0;
    }

    /**
     * Create a new body element if dont exists previously
     */
    public void prepareBody(final String sid) {
	if (this.body == null) {
	    requestID++;
	    body = createBody();
	    setSID(sid);
	}
    }

    public void setRestart(final String domain) {
	body.With("xmpp:restart", "true").With("xmlns:xmpp", "urn:xmpp:xbosh").With("xml:lang", "en")
		.With("to", domain);
    }

    public void setSID(final String sid) {
	if (sid != null) {
	    body.setAttribute("sid", sid);
	}
    }

    public void setTerminate() {
	body.setAttribute("type", "terminate");
    }

    public void start(final String domain) {
	this.requestID = (int) (Math.random() * 1245234);
	body = createInitialBody(domain);
    }

    /**
     * Testing purposes only
     * 
     * @return
     */
    Packet getBody() {
	return body;
    }

    private Packet createBody() {
	final Packet initalBody = new Packet("body", "http://jabber.org/protocol/httpbind");
	initalBody.setAttribute("rid", String.valueOf(requestID));
	return initalBody;
    }

    private Packet createInitialBody(final String domain) {
	final Packet initalBody = createBody();
	initalBody.With("content", "text/xml; charset=utf-8").With("to", domain).With("secure", "true").With("wait",
		"60").With("ack", "1").With("hold", "1").With("xml:lang", "en");
	initalBody.With("xmpp:version", "1.0").With("xmlns:xmpp", "urn:xmpp:xbosh");
	return initalBody;
    }
}
