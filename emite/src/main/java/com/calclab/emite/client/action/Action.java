package com.calclab.emite.client.action;

import com.calclab.emite.client.packet.Packet;

public interface Action {
	void handle(Packet stanza);

}
