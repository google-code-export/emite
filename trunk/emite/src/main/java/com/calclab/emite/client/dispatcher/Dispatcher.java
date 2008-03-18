package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.action.Action;
import com.calclab.emite.client.packet.Packet;

public interface Dispatcher {

	public void addListener(Action action);

	public void publish(Packet packet);
}
