package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class ResourceBindingManager extends EmiteComponent {
	public static class Events {
		public static final Event binded = new Event("resource:binded");
	}

	private final Globals globals;

	public ResourceBindingManager(final Emite emite, final Globals globals) {
		super(emite);
		this.globals = globals;

	}

	@Override
	public void attach() {
		when(SASLManager.Events.authorized, new PacketListener() {
			public void handle(final Packet received) {
				final IQ iq = new IQ("bindRequest", IQ.Type.set);
				iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText(
						globals.getResourceName());

				emite.send(iq);
			}
		});
		when(new IQ("bindRequest", IQ.Type.result, null), new PacketListener() {
			public void handle(final Packet iq) {
				final String jid = iq.getFirstChild("bind").getFirstChild("jid").getText();
				globals.setXmppURI(jid);
				emite.publish(Events.binded);
			}
		});
	}
}
