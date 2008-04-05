package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Packet;

public interface Dispatcher {

	public void addListener(DispatcherStateListener listener);

	public void publish(Packet packet);

	public void subscribe(Matcher matcher, PacketListener packetListener);

}
