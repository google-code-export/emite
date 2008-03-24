package com.calclab.emite.client.matcher;

import com.calclab.emite.client.packet.Packet;

public class Matchers {

	public static final Matcher ANYTHING = new Matcher() {
		public String getElementName() {
			return null;
		}

		public boolean matches(Packet stanza) {
			return true;
		}
	};

}
