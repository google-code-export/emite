package com.calclab.emite.client.packet.stanza;

import com.calclab.emite.client.packet.Packet;

public interface Stanza extends Packet {
	String getFrom();

	String getTo();

	void setFrom(String from);

	void setTo(String to);
}
