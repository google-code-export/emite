package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh.EmiteBosh;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class Chat extends DispatcherComponent {

	private final Dispatcher dispatcher;
	private final ArrayList<MessageListener> listeners;

	public Chat(final Dispatcher dispatcher) {
		super(dispatcher);
		this.dispatcher = dispatcher;
		this.listeners = new ArrayList<MessageListener>();
	}

	public void addListener(final MessageListener listener) {
		listeners.add(listener);

	}

	@Override
	public void attach() {
		when(new BasicPacket("message", null), new PacketListener() {
			public void handle(final Packet received) {
				onReceived(new Message(received));
			}
		});
	}

	public void onReceived(final Message message) {
		for (final MessageListener listener : listeners) {
			listener.onReceived(message);
		}
	}

	public void send(final String to, final String msg) {
		final Message message = new Message(to, msg);
		dispatcher.publish(new Event(EmiteBosh.Events.send).With(message));
	}

}
