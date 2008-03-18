package com.calclab.emite.client.packet.stanza;

import com.calclab.emite.client.packet.Packet;

public class IQ extends BasicStanza {
	public static enum Type {
		error, get, result, set
	}

	public IQ(final Packet stanza) {
		super(stanza);
	}

	public IQ(final String id, final Type type) {
		super(NAME, "jabber:client");
		setId(id);
		setType(type.toString());
	}

	public Packet Include(final String name, final String xmlns) {
		add(name, xmlns);
		return this;
	}

	public Packet setQuery(final String namespace) {
		return add("query", namespace);
	}

	public IQ To(final String to) {
		setTo(to);
		return this;
	}

	/**
	 * BUILDER METHOD
	 * 
	 * @param string
	 * @return
	 */
	public IQ WithQuery(final String queryNamespace) {
		add("query", queryNamespace);
		return this;
	}
}
