package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.Packet;

public interface Stanza extends Packet {
	public Stanza To(String to);

	String getFrom();

	XmppURI getFromURI();

	String getTo();

	XmppURI getToURI();

	void setFrom(String from);

	void setTo(String to);
}
