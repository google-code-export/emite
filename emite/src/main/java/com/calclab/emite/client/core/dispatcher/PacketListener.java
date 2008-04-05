package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.core.packet.Packet;

public interface PacketListener {
	void handle(Packet received);

}
