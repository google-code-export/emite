package com.calclab.emite.client.mock;

import java.util.ArrayList;

import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

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

	public void subscribe(final Matcher matcher, final Action action) {
	}

}
