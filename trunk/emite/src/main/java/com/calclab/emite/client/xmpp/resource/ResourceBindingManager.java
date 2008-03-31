package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.bosh.SenderComponent;
import com.calclab.emite.client.core.dispatcher.Answer;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class ResourceBindingManager extends SenderComponent {
	public static class Events {
		public static final Event binded = new Event("resource:binded");
	}

	final Answer requestResourceBinding;
	final Answer resourceBinded;

	public ResourceBindingManager(final Dispatcher dispatcher, final Bosh bosh, final Globals globals) {
		super(dispatcher, bosh);
		requestResourceBinding = new Answer() {
			public Packet respondTo(final Packet cathced) {
				final IQ iq = new IQ("bindRequest", IQ.Type.set);
				iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText(
						globals.getResourceName());
				return iq;
			}
		};

		resourceBinded = new Answer() {
			public Packet respondTo(final Packet iq) {
				final String jid = iq.getFirstChild("bind").getFirstChild("jid").getText();
				globals.setJID(jid);
				return Events.binded;
			}
		};
	}

	@Override
	public void attach() {
		when(SASLManager.Events.authorized).Send(requestResourceBinding);
		when(new IQ("bindRequest", IQ.Type.result, null)).Publish(resourceBinded);
	}
}
