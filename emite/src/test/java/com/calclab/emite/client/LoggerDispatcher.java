package com.calclab.emite.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.APacket;

public class LoggerDispatcher implements Dispatcher {

	private final Dispatcher dispatcher;

	public LoggerDispatcher(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void addListener(final DispatcherStateListener listener) {
		dispatcher.addListener(listener);
	}

	public void publish(final APacket aPacket) {
		Log.debug("PUBLISHED: " + aPacket.toString());
		dispatcher.publish(aPacket);
	}

	public void subscribe(final Matcher matcher, final PacketListener packetListener) {
		dispatcher.subscribe(matcher, packetListener);
	}

}
