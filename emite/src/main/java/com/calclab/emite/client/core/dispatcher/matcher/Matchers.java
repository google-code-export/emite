package com.calclab.emite.client.core.dispatcher.matcher;

import com.calclab.emite.client.core.packet.Packet;

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
