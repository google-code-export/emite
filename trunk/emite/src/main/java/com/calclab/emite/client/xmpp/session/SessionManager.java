package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class SessionManager extends EmiteComponent {
	private final Globals globals;
	private final Session session;

	public SessionManager(final Emite emite, final Globals globals, final Session session) {
		super(emite);
		this.globals = globals;
		this.session = session;

	}

	@Override
	public void attach() {

		when(SASLManager.Events.authorized, new PacketListener() {
			public void handle(final Packet received) {
				emite.publish(BoshManager.Events.restart);
			}
		});

		when(SASLManager.Events.authorized, new PacketListener() {
			public void handle(final Packet received) {
				session.setState(Session.State.authorized);
			}
		});

		when(Session.Events.loggedOut, new PacketListener() {
			public void handle(final Packet received) {
				emite.publish(BoshManager.Events.stop);
			}
		});

		when(ResourceBindingManager.Events.binded, new PacketListener() {
			public void handle(final Packet received) {
				final IQ iq = new IQ("requestSession", IQ.Type.set).From(globals.getOwnURI()).To(globals.getDomain());
				iq.Include("session", "urn:ietf:params:xml:ns:xmpp-session");
				emite.send(iq);
			}

		});
		when(new IQ("requestSession", IQ.Type.result, null), new PacketListener() {
			public void handle(final Packet received) {
				session.setState(Session.State.connected);
				emite.publish(Session.Events.loggedIn);
			}
		});

	}
}
