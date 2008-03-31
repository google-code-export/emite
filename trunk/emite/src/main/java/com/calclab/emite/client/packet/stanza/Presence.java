package com.calclab.emite.client.packet.stanza;

import com.calclab.emite.client.packet.Packet;

public class Presence extends BasicStanza {

	public static enum Show {
		away, chat, dnd, xa
	}

	public Presence() {
		super("presence", "jabber:client");
	}

	public Presence(final Packet stanza) {
		super(stanza);
	}

	public Presence(final String from) {
		this();
		setFrom(from);
	}

	public void setShow(final Show value) {
		Packet show = getFirstChild("show");
		if (show == null) {
			show = add("show", null);
		}
		show.setText(value.toString());
	}

	public Presence With(final Show value) {
		setShow(value);
		return this;
	}

}
