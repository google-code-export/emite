package com.calclab.emite.client.core.dispatcher;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class DispatcherTest {

    private Dispatcher dispatcher;

    @Before
    public void aaCreate() {
	dispatcher = new DispatcherDefault(new DispatcherMonitor() {
	    public void publishing(final IPacket packet) {
	    }
	});
    }

    @Test
    public void dispacherCanRegisterUnnamedMatchers() {
	final PacketListener listener = mock(PacketListener.class);
	dispatcher.subscribe(Matcher.ANYTHING, listener);
	final int times = 5;
	for (int index = 0; index < times; index++) {
	    dispatcher.publish(new Packet(String.valueOf(1000 * Math.random())));
	}
	verify(listener, times(times)).handle((IPacket) anyObject());
    }

    @Test
    public void dispatcherShouldCallStateListener() {
	final DispatcherStateListener listener = Mockito.mock(DispatcherStateListener.class);
	dispatcher.addListener(listener);
	dispatcher.publish(new Packet("name"));
	verify(listener, times(1)).beforeDispatching();
	verify(listener, times(1)).afterDispatching();
    }

    @Test
    public void dispatcherShouldFireToSubscriptors() {
	final PacketListener listener = Mockito.mock(PacketListener.class);
	final Matcher matcher = new PacketMatcher("name");
	dispatcher.subscribe(matcher, listener);

	final Packet packet = new Packet("name");
	dispatcher.publish(packet);
	verify(listener, times(1)).handle(packet);

	final Packet other = new Packet("other name");
	dispatcher.publish(other);
	verify(listener, times(0)).handle(other);
    }
}
