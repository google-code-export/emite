package com.calclab.emite.client.im.session;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.modules.ResourceModule;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.IQType;
import com.calclab.emite.client.packet.stanza.Stanza;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;
import com.calclab.emite.client.subscriber.IQSubscriber;

/**
 * TODO: better plugin system!!!
 * 
 * @author dani
 * 
 */
public class SessionPlugin implements Plugin {
	public static final String SUCCESS = "session:success";
	private static final String COMPONENT_NAME = "session";

	public void start(final Engine xmpp) {
		xmpp.register(COMPONENT_NAME, this);
		xmpp.addListener(new EventSubscriber(ResourceModule.SUCCESS) {
			@Override
			protected void handleEvent(final Event event) {
				final Stanza iq = new IQ("sess_1", IQType.set);
				iq.setTo(xmpp.getGlobal(Engine.DOMAIN));
				iq.add("session", "urn:ietf:params:xml:ns:xmpp-session");
				xmpp.send(iq);
			}
		});

		xmpp.addListener(new IQSubscriber("sess_1") {
			@Override
			protected void handleIQ(final IQ iq) {
				final Event event = new Event(SUCCESS);
				xmpp.publish(event);
			}
		});

	}

}
