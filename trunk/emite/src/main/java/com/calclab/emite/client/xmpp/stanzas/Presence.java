package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.Packet;

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

	public Integer getPriority() {
		Integer value = null;
		final Packet priority = getFirstChild("priority");
		if (priority != null) {
			try {
				value = Integer.parseInt(priority.getText());
			} catch (final NumberFormatException e) {
				value = null;
			}
		}
		return value;
	}

	// TODO: revisar esto (type == null -> available)
	// http://www.xmpp.org/rfcs/rfc3921.html#presence
	public PresenceType getType() {
		final String type = getAttribute(BasicStanza.TYPE);
		return type != null ? PresenceType.valueOf(type) : PresenceType.available;
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
