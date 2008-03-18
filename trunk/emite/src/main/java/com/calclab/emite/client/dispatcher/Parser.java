package com.calclab.emite.client.dispatcher;

import java.util.List;

import com.calclab.emite.client.packet.Packet;

public interface Parser {
	List<? extends Packet> extractStanzas(String xml);
}
