package com.calclab.emite.client.matcher;

import com.calclab.emite.client.packet.Packet;

public class BasicMatcher implements Matcher {
	private final String attName;
	private final String attValue;
	private final String regex;

	public BasicMatcher(final String regex) {
		this(regex, null, null);
	}

	public BasicMatcher(final String regex, final String attName, final String attValue) {
		this.regex = regex;
		this.attName = attName;
		this.attValue = attValue;
	}

	public boolean matches(final Packet stanza) {
		final boolean isCorrectName = stanza.getName().matches(regex);
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
		return "[Matcher: " + regex + " (" + name + "," + value + ")]";
	}
}
