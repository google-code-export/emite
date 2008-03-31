package com.calclab.emite.client.components;

import com.calclab.emite.client.packet.Packet;

public interface Answer {

	Packet respondTo(Packet received);

}
