package com.calclab.emite.client.dispatcher.parser;

import com.calclab.emite.client.packet.Packet;

public interface Builder {
	void addAttribute(String name, String value);

	void closeElement(String name);

	Packet endDocument();

	void openElement(String name);

	void startDocument();
}
