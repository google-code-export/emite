package com.calclab.emite.client.x.core;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin;

public class ResourceModule implements Plugin {

	public static class Events {
		public static final Event binded = new Event("resource:binded");
	}

	final BussinessLogic requestResourceBinding;
	final BussinessLogic resourceBinded;

	public ResourceModule(final Globals globals) {
		requestResourceBinding = new BussinessLogic() {
			public Packet logic(final Packet cathced) {
				final IQ iq = new IQ("bindRequest", IQ.Type.set);
				iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText("mensa");
				return iq;
			}
		};

		resourceBinded = new BussinessLogic() {
			public Packet logic(final Packet iq) {
				final String jid = iq.getFirstChildren("bind").getFirstChildren("jid").getText();
				globals.setJID(jid);
				return Events.binded;
			}
		};
	}

	public void start(final FilterBuilder when, final Components components) {
		when.Event(SASLModule.Events.authorized).send(requestResourceBinding);

		when.IQ("bindRequest").publish(resourceBinded);
	}

}
