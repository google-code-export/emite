package com.calclab.emite.client.mock;

import java.util.ArrayList;

import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Packet;

public class MockDispatcher implements Dispatcher {

	private final ArrayList<Packet> published;

	public MockDispatcher() {
		this.published = new ArrayList<Packet>();
	}

	public void addListener(final DispatcherStateListener listener) {
	}

	public int getLength() {
		return published.size();
	}

	public Packet getPublished(final int index) {
		return published.get(index);
	}

	public void publish(final Packet packet) {
		published.add(packet);
	}

	public void subscribe(final Matcher matcher, final PacketListener packetListener) {
	}

}
