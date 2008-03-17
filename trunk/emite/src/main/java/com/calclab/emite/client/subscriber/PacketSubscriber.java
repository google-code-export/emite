package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public interface PacketSubscriber {
	Matcher getMatcher();

	void handle(Packet stanza);

}
