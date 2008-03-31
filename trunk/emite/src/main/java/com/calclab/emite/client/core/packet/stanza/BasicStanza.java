package com.calclab.emite.client.core.packet.stanza;

import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.DelegatedPacket;
import com.calclab.emite.client.core.packet.Packet;

public class BasicStanza extends DelegatedPacket implements Stanza {
	protected static final String NAME = "iq";
	private static final String FROM = "from";
	private static final String ID = "id";
	private static final String TO = "to";
	private static final String TYPE = "type";

	protected BasicStanza(final Packet stanza) {
		super(stanza);
	}

	protected BasicStanza(final String name, final String xmlns) {
		super(new BasicPacket(name, xmlns));
	}

	public String getFrom() {
		return getAttribute(FROM);
	}

	public String getId() {
		return getAttribute(ID);
	}

	public String getTo() {
		return getAttribute(TO);
	}

	public String getType() {
		return getAttribute(TYPE);
	}

	public void setFrom(final String from) {
		setAttribute(FROM, from);
	}

	public void setId(final String id) {
		setAttribute(ID, id);
	}

	public void setTo(final String to) {
		setAttribute(TO, to);
	}

	public void setType(final String type) {
		setAttribute(TYPE, type);
	}

	public Stanza To(final String to) {
		setTo(to);
		return this;
	}
}
