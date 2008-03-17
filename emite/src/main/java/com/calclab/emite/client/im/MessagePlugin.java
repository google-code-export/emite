package com.calclab.emite.client.im;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.im.session.SessionPlugin;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;
import com.calclab.emite.client.subscriber.MessageSubscriber;

public class MessagePlugin implements Plugin {
	private static final String COMPONENT_NAME = "messager";

	public static Messager getMessager(final Engine engine) {
		return (Messager) engine.getComponent(COMPONENT_NAME);
	}

	public void start(final Engine engine) {
		final Messager messager = new Messager(engine);
		engine.register(COMPONENT_NAME, messager);

		engine.addListener(new EventSubscriber(SessionPlugin.SUCCESS) {
			@Override
			protected void handleEvent(final Event event) {
				engine.addListener(new MessageSubscriber(engine.getGlobal(Engine.JID)) {
					@Override
					protected void handleMessage(final Message message) {
						messager.onReceived(message);
					}
				});
			}
		});
	}

}
