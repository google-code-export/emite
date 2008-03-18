package com.calclab.emite.client.x.core;

import com.calclab.emite.client.IContainer;
import com.calclab.emite.client.IGlobals;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin2;
import com.calclab.emite.client.utils.Base64Coder;

public class SASLModule implements Plugin2 {
	public static class Events {
		public static final Event authorized = new Event("sasl:authorized");
	}

	private static final String SEP = new String(new char[] { 0 });

	private final BussinessLogic authorization;

	public SASLModule(final IGlobals globals) {
		authorization = new BussinessLogic() {
			public Packet logic(final Packet cathced) {
				final Packet auth = createPlainAuthorization(globals);
				return auth;
			}

			protected String encode(final String domain, final String userName, final String password) {
				final String auth = userName + "@" + domain + SEP + userName + SEP + password;
				return Base64Coder.encodeString(auth);
			}

			private Packet createPlainAuthorization(final IGlobals globals) {
				final Packet auth = new BasicPacket("auth", "urn:ietf:params:xml:ns:xmpp-sasl").with("mechanism",
						"PLAIN");
				final String encoded = encode(globals.getDomain(), globals.getUserName(), globals.getPassword());
				auth.addText(encoded);
				return auth;
			}
		};
	}

	public void start(final FilterBuilder when, final IContainer components) {
		when.Packet("stream:features").send(authorization);
		when.Packet("success", "xmlns", "urn:ietf:params:xml:ns:xmpp-sasl").publish(Events.authorized);
	}
}
