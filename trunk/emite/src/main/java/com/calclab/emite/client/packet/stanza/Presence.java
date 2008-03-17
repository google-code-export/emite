package com.calclab.emite.client.packet.stanza;

import com.calclab.emite.client.packet.Packet;

public class Presence extends BasicStanza {

	public static final String SHOW_AWAY = "away";
	public static final String SHOW_BUSY = "dnd";
	public static final String SHOW_CHAT = "chat";
	public static final String SHOW_XA = "xa";

	public Presence(final Packet stanza) {
		super(stanza);
	}

	public Presence(final String from) {
		super("presence", "jabber:client");
		setFrom(from);
	}

	public void setShow(final String showValue) {
		Packet show = getFirst("show");
		if (show == null) {
			show = add("show", null);
		}
		show.setText(showValue);
	}

}
