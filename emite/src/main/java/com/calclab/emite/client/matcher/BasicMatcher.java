package com.calclab.emite.client.matcher;

import com.calclab.emite.client.packet.Packet;

public class BasicMatcher implements Matcher {
	private final String attName;
	private final String attValue;
	private final String name;

	public BasicMatcher(final String name) {
		this(name, null, null);
	}

	public BasicMatcher(final String name, final String attName,
			final String attValue) {
		this.name = name;
		this.attName = attName;
		this.attValue = attValue;
	}

	public String getElementName() {
		return name;
	}

	public boolean matches(final Packet stanza) {
		final boolean isCorrectName = stanza.getName().equals(name);
		if (isCorrectName) {
			if (attName != null) {
				return attValue.equals(stanza.getAttribute(attName));
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		final String name = attName == null ? "null" : attName;
		final String value = attValue == null ? "null" : attValue;
		return "[Matcher: " + name + " (" + name + "," + value + ")]";
	}
}
