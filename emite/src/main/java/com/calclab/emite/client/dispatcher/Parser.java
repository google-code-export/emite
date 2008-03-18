package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;

import com.calclab.emite.client.packet.Packet;

public interface Parser {
	ArrayList<Packet> extractStanzas(String xml);
}
