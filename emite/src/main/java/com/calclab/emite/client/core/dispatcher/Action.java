package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.packet.Packet;

public interface Action {
	void handle(Packet received);

}
