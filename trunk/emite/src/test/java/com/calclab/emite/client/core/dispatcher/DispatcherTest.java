package com.calclab.emite.client.core.dispatcher;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.*;
import static com.calclab.emite.testing.MockitoEmiteHelper.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.dispatcher.Dispatcher.Events;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.dispatcher.matcher.Matchers;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class DispatcherTest {

    private Dispatcher dispatcher;

    @Before
    public void aaCreate() {
	dispatcher = new DispatcherDefault();
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
	verify(listener, times(1)).dispatchingBegins();
	verify(listener, times(1)).dispatchingEnds();
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

    @Test(expected = RuntimeException.class)
    public void shouldFireErrorEventWhenException() {
	final PacketListener errorProducer = mock(PacketListener.class);
	stubVoid(errorProducer).toThrow(new RuntimeException("the message")).on().handle((IPacket) anyObject());
	dispatcher.subscribe(Matchers.ANYTHING, errorProducer);

	final PacketListener wrongErrorListener = mock(PacketListener.class);
	stubVoid(wrongErrorListener).toThrow(new RuntimeException("something here")).on().handle((IPacket) anyObject());
	dispatcher.subscribe(when(Events.onError), wrongErrorListener);

	final PacketListener errorListener = mock(PacketListener.class);
	dispatcher.subscribe(when(Events.onError), errorListener);
	dispatcher.publish(new Packet("anything"));
	verify(wrongErrorListener, times(1)).handle(packetLike(Events.onError));
	verify(errorListener, times(1)).handle(packetLike(Events.onError.Params("info", "the message")));
    }
}
