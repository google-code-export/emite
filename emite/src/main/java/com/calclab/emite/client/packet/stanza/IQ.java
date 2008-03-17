package com.calclab.emite.client.packet.stanza;

import com.calclab.emite.client.packet.Packet;

public class IQ extends BasicStanza {
	public IQ(final Packet stanza) {
		super(stanza);
	}

	public IQ(final String id, final IQType type) {
		super(NAME, "jabber:client");
		setId(id);
		setType(type.toString());
	}

	public Packet setQuery(final String namespace) {
		return add("query", namespace);
	}
}
