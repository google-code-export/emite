package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.Packet;

public class Message extends BasicStanza {

	private static final String TYPE_CHAT = "chat";

	public Message(final Packet packet) {
		super(packet);
	}

	public Message(final String to, final String msg) {
		super("message", "jabber:client");
		setType(TYPE_CHAT);
		setTo(to);
		setMessage(msg);
	}

	public String getBody() {
		return getFirstChild("body").getText();
	}

	public MessageType getType() {
		final String type = getAttribute(TYPE);
		return type != null ? MessageType.valueOf(type) : null;
	}

	private void setMessage(final String msg) {
		final Packet body = add("body", null);
		body.addText(msg);
	}
}
