package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;

public class Body extends BasicStanza {

	public Body(final Packet delegated) {
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
