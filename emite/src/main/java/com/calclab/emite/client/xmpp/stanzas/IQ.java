package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.Packet;

public class IQ extends BasicStanza {
	public static enum Type {
		error, get, result, set
	}

	public IQ(final Packet stanza) {
		super(stanza);
	}

	public IQ(final String id, final Type type) {
		this(id, type, "jabber:client");
	}

	public IQ(final String id, final Type type, final String xmlns) {
		super(NAME, xmlns);
		setId(id);
		setType(type.toString());
	}

	public IQ From(final String from) {
		setFrom(from);
		return this;
	}

	public Packet Include(final String name, final String xmlns) {
		add(name, xmlns);
		return this;
	}

	public Packet setQuery(final String namespace) {
		return add("query", namespace);
	}

	@Override
	public IQ To(final String to) {
		setTo(to);
		return this;
	}

	public IQ WithQuery(final String queryNamespace) {
		add("query", queryNamespace);
		return this;
	}
}
