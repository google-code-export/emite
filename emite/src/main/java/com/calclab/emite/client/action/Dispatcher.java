package com.calclab.emite.client.action;

import com.calclab.emite.client.packet.Packet;

public interface Dispatcher {

	public void addListener(Action action);

	public void publish(Packet packet);
}
