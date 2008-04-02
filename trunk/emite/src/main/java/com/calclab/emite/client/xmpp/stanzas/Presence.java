package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.Packet;

public class Presence extends BasicStanza {
	public static enum Show {
		away, chat, dnd, xa
	}

	public enum Type {
		available, error, probe, subscribe, subscribed, unavailable, unsubscribe, unsubscribed
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

	/**
	 * 
	 * @return -1 means no specified?
	 */
	// TODO: igual devolver 0 en vez de -1 (-1 no es un valor válido)
	public int getPriority() {
		int value = -1;
		final Packet priority = getFirstChild("priority");
		if (priority != null) {
			try {
				value = Integer.parseInt(priority.getText());
			} catch (final NumberFormatException e) {
				value = -1;
			}
		}
		return value;
	}

	public Show getShow() {
		final Packet show = getFirstChild("show");
		final String value = show != null ? show.getText() : null;
		return value != null ? Show.valueOf(value) : null;
	}

	public String getStatus() {
		final Packet status = getFirstChild("status");
		return status != null ? status.getText() : null;
	}

	// TODO: revisar esto (type == null -> available)
	// http://www.xmpp.org/rfcs/rfc3921.html#presence
	public Type getType() {
		final String type = getAttribute(BasicStanza.TYPE);
		return type != null ? Type.valueOf(type) : Type.available;
	}

	// TODO: igual demasiado fuerte lo de la excepción... ¿qué opinas vicente?
	public void setPriority(final int value) {
		if (value < 0) {
			throw new RuntimeException("not valid priority value");
		}
		Packet priority = getFirstChild("priority");
		if (priority == null) {
			priority = add("priority", null);
		}
		priority.setText(Integer.toString(value));
	}

	public void setShow(final Show value) {
		Packet show = getFirstChild("show");
		if (show == null) {
			show = add("show", null);
		}
		show.setText(value.toString());
	}

	public void setStatus(final String statusMessage) {
		Packet status = getFirstChild("status");
		if (status == null) {
			status = add("status", null);
		}
		status.setText(statusMessage);
	}

	public Presence With(final Show value) {
		setShow(value);
		return this;
	}

}
