package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.components.Answer;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.bosh.SenderComponent;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;

public class SASLManager extends SenderComponent {
	public static class Events {
		public static final Event authorized = new Event("sasl:authorized");
	}

	private static final String SEP = new String(new char[] { 0 });
	private final Globals globals;

	final Answer restartAndAuthorize;

	public SASLManager(final Dispatcher dispatcher, final Connection connection, final Globals globals) {
		super(dispatcher, connection);
		this.globals = globals;

		restartAndAuthorize = new Answer() {
			public Packet respondTo(final Packet received) {
				return Events.authorized;
			}

		};
	}

	@Override
	public void attach() {
		when(new BasicPacket("stream:features")).Send(new Answer() {
			public Packet respondTo(final Packet cathced) {
				return createPlainAuthorization(globals);
			}
		});
		when(new BasicPacket("success", "urn:ietf:params:xml:ns:xmpp-sasl")).Publish(Events.authorized);
	}

	protected String encode(final String domain, final String userName, final String password) {
		final String auth = userName + "@" + domain + SEP + userName + SEP + password;
		return Base64Coder.encodeString(auth);
	}

	private Packet createPlainAuthorization(final Globals globals) {
		final Packet auth = new BasicPacket("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
		final String encoded = encode(globals.getDomain(), globals.getUserName(), globals.getPassword());
		auth.addText(encoded);
		return auth;
	}
}
