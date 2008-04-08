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

import com.calclab.emite.client.core.packet.APacket;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;

public class Body extends BasicStanza {

	public Body(final APacket delegated) {
		super(delegated);
	}

	Body(final Long rid, final String sid) {
		super("body", "http://jabber.org/protocol/httpbind");
		setAttribute("rid", rid.toString());
		setSID(sid);
	}

	public String getCondition() {
		return getAttribute("condition");
	}

	public int getPoll() {
		return getAttributeAsInt("polling") * 1000;
	}

	public String getSID() {
		return getAttribute("sid");
	}

	public boolean isEmpty() {
		return getChildrenCount() == 0;
	}

	// TODO: OpenFire devuelve "terminal" en vez de "terminate"... no sé si es
	// un bug...
	public boolean isTerminal() {
		final String type = getAttribute(TYPE);
		return type != null && (type.equals("terminate") || type.equals("terminal"));
	}

	public void setCreationState(final String domain) {
		With("content", "text/xml; charset=utf-8").With("to", domain).With("secure", "true").With("ver", "1.6").With(
				"wait", "60").With("ack", "1").With("hold", "1").With("xml:lang", "en");
	}

	public void setRestart(final String domain) {
		With("xmpp:restart", "true").With("xmlns:xmpp", "urn:xmpp:xbosh").With("xml:lang", "en").With("to", domain);
	}

	public void setSID(final String sid) {
		if (sid != null) {
			setAttribute("sid", sid);
		}
	}

	public void setTerminate() {
		setType("terminate");
	}
}
