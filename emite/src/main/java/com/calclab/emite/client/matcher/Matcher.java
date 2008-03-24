package com.calclab.emite.client.matcher;

import com.calclab.emite.client.packet.Packet;

public interface Matcher {
	String getElementName();

	boolean matches(Packet stanza);
}
