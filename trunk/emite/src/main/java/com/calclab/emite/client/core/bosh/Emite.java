package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public interface Emite {
	Dispatcher getDispatcher();

	void publish(Event event);

	void send(Packet packet);
}
