package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.core.packet.Packet;

public interface Action {
	void handle(Packet received);

}
