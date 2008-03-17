package com.calclab.emite.client.modules;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.AbstractPacketSubscriber;
import com.calclab.emite.client.utils.Base64Coder;

public class SASLModule implements Plugin {
	public static final String SUCCESS = "sasl:success";
	private static final String SEP = new String(new char[] { 0 });

	public void start(final Engine xmpp) {
		xmpp.addListener(new AbstractPacketSubscriber(new BasicMatcher("stream:features")) {
			@Override
			public void handle(final Packet stanza) {
				final Packet auth = new BasicPacket("auth", "urn:ietf:params:xml:ns:xmpp-sasl").with("mechanism",
						"PLAIN");

				final String encoded = encode(xmpp.getGlobal(Engine.DOMAIN), xmpp.getGlobal(Engine.USER), xmpp
						.getGlobal(Engine.PASSWORD));

				auth.addText(encoded);
				xmpp.send(auth);
			}
		});

		xmpp.addListener(new AbstractPacketSubscriber(new BasicMatcher("success", "xmlns",
				"urn:ietf:params:xml:ns:xmpp-sasl")) {
			@Override
			public void handle(final Packet stanza) {
				xmpp.publish(new Event(SUCCESS));
			}
		});
	}

	protected String encode(final String domain, final String userName, final String password) {
		final String auth = userName + "@" + domain + SEP + userName + SEP + password;
		return Base64Coder.encodeString(auth);
	}
}
