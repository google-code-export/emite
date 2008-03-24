package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.packet.Packet;

public interface Action {
	void handle(Packet stanza);

}
