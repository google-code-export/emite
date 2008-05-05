package com.calclab.emite.client.core.bosh;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.IQ;

import static com.calclab.emite.testing.TestMatchers.*;

public class EmiteTest {

    private EmiteBosh emite;
    private Dispatcher dispatcher;
    private Stream stream;

    @Before
    public void beforeTest() {
	stream = mock(Stream.class);
	dispatcher = mock(Dispatcher.class);
	emite = new EmiteBosh(dispatcher, stream);
    }

    @Test
    public void shouldChangeStreamOnSend() {
	final IPacket packet = new Packet("child");
	emite.onSend(EmiteBosh.Events.send(packet));
	verify(stream).addResponse(same(packet));
    }

    @Test
    public void shouldSend() {
	final IPacket packet = new Packet("something");
	emite.send(packet);
	verify(dispatcher).publish(packetLike(EmiteBosh.Events.onDoSend));
    }

    @Test
    public void shouldSendIQ() {
	final PacketListener callback = mock(PacketListener.class);
	final IPacket iq = new IQ(IQ.Type.set);
	final String id = emite.sendIQ("category", iq, callback);
	verify(dispatcher).publish(packetLike(EmiteBosh.Events.onDoSend.With(iq)));
	emite.onIQ(new IQ(IQ.Type.set).With("id", id));
	verify(callback).handle((IPacket) anyObject());
    }
}
