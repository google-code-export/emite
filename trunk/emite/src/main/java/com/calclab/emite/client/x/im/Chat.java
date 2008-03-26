package com.calclab.emite.client.x.im;

import java.util.ArrayList;

import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.dsl.PacketProducer;

public class Chat {

	public static class Events {
		public static Event send = new Event("chat:send");
	}

	public final PacketProducer send;
	private final Dispatcher dispatcher;
	private final ArrayList<MessageListener> listeners;

	public Chat(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.listeners = new ArrayList<MessageListener>();
		send = new PacketProducer() {

			public Packet respondTo(final Packet received) {
				return received.getFirstChildren("message");
			}
		};
	}

	public void addListener(final MessageListener listener) {
		listeners.add(listener);

	}

	public void onReceived(final Message message) {
		for (final MessageListener listener : listeners) {
			listener.onReceived(message);
		}
	}

	public void send(final String to, final String msg) {
		final Message message = new Message(to, msg);
		dispatcher.publish(new Event(Events.send).With(message));
	}
}
