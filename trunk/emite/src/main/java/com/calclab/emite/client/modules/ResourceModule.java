package com.calclab.emite.client.modules;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.BasicStanza;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.IQType;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;
import com.calclab.emite.client.subscriber.IQSubscriber;

public class ResourceModule implements Plugin {

	public static final String SUCCESS = "resource:success";

	public void start(final Engine xmpp) {

		xmpp.addListener(new EventSubscriber(SASLModule.SUCCESS) {
			@Override
			protected void handleEvent(final Event event) {
				onSASLSuccess(xmpp);
			}
		});

		xmpp.addListener(new IQSubscriber("bind_1") {
			@Override
			protected void handleIQ(final IQ iq) {
				final String jid = iq.getFirst("bind").getFirst("jid").getText();
				xmpp.setGlobal(Engine.JID, jid);
				xmpp.publish(new Event(SUCCESS));
			}

		});

	}

	private void onSASLSuccess(final Engine xmpp) {
		final BasicStanza iq = new IQ("bind_1", IQType.set);
		iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText("mensa");
		xmpp.send(iq);
	}
}
