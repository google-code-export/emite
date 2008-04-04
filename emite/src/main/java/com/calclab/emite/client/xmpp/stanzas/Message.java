package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Packet;

public class Message extends BasicStanza {
    public static enum MessageType {
	chat, error, groupchat, headlines, normal
    }

    private static final String TYPE_CHAT = "chat";

    public Message(final Packet packet) {
	super(packet);
    }

    public Message(final String from, final String to, final String msg) {
	super("message", "jabber:client");
	setType(TYPE_CHAT);
	setFrom(from);
	setTo(to);
	setMessage(msg);
    }

    public Message(final XmppURI fromUri, final XmppURI toURI, final String message) {
	this(fromUri.toString(), toURI.toString(), message);
    }

    public String getBody() {
	return getFirstChild("body").getText();
    }

    public String getThread() {
	final Packet thread = getFirstChild("thread");
	return thread != null ? thread.getText() : null;
    }

    public MessageType getType() {
	final String type = getAttribute(TYPE);
	return type != null ? MessageType.valueOf(type) : null;
    }

    public Message Thread(final String thread) {
	setThread(thread);
	return this;
    }

    private void setMessage(final String msg) {
	final Packet body = add("body", null);
	body.addText(msg);
    }

    private void setThread(final String thread) {
	Packet node = getFirstChild("thread");
	if (node == null) {
	    node = new BasicPacket("thread");
	    this.addChild(node);
	}
	node.setText(thread);
    }
}
