package com.calclab.emite.client.core.packet.stanza;

import com.calclab.emite.client.core.packet.Packet;

public interface Stanza extends Packet {
	public Stanza To(String to);

	String getFrom();

	String getTo();

	void setFrom(String from);

	void setTo(String to);
}
