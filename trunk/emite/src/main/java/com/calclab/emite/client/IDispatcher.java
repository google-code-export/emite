package com.calclab.emite.client;

import com.calclab.emite.client.action.Action;
import com.calclab.emite.client.packet.Packet;

public interface IDispatcher {

	public void addListener(Action action);

	public void publish(Packet packet);
}
