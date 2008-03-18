package com.calclab.emite.client.dispatcher.parser;

import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Packet;

public class SimpleBuilder implements Builder {
	private Packet current;
	private Packet root;

	public SimpleBuilder() {
	}

	public void addAttribute(final String name, final String value) {
		current.setAttribute(name, value);
	}

	public void closeElement(final String name) {
		current = current.getParent();
	}

	public Packet endDocument() {
		return root;
	}

	public void openElement(final String name) {
		if (current == null) {
			root = current = new BasicPacket(name, null);
		} else {
			current = current.add(name, null);
		}
	}

	public void startDocument() {
		root = current = null;
	}

}