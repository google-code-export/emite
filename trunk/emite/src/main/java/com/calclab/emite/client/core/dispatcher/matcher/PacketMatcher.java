package com.calclab.emite.client.core.dispatcher.matcher;

import java.util.HashMap;

import com.calclab.emite.client.core.packet.Packet;

public class PacketMatcher implements Matcher {
	private final HashMap<String, String> attributes;
	private final String name;

	public PacketMatcher(final Packet packet) {
		this(packet.getName(), packet.getAttributes());
	}

	public PacketMatcher(final String packetName) {
		this(packetName, null);
	}

	public PacketMatcher(final String name, final HashMap<String, String> attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public String getElementName() {
		return name;
	}

	public boolean matches(final Packet stanza) {
		if (attributes == null) {
			return true;
		}
		for (final String name : attributes.keySet()) {
			final String value = stanza.getAttribute(name);
			final String expected = attributes.get(name);
			// Log.debug("MATCHER " + name + " - expecting: " + expected + " |
			// value: " + value);
			if (!expected.equals(value)) {
				return false;
			}
		}
		return true;
	}

}
