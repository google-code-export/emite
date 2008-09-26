package com.calclab.emite.core.client.bosh;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.IsPacketLike;
import com.calclab.suco.testing.listener.EventTester;

public class ConnectionTestHelper {

    public final Connection connection;
    private EventTester<IPacket> onStanzaEvent;
    private final TigaseXMLService xmler;

    public ConnectionTestHelper() {
	xmler = new TigaseXMLService();
	connection = mock(BoshConnection.class);
    }

    public Connection getConnection() {
	return connection;
    }

    public void simulateReception(final IPacket packet) {
	if (onStanzaEvent == null) {
	    replaceOnStanzaEvent();
	}
	onStanzaEvent.fire(packet);
    }

    public void simulateReception(final String received) {
	final IPacket packet = xmler.toXML(received);
	simulateReception(packet);
    }

    public void verifySentLike(final IPacket packet) {
	verify(connection).send(argThat(new IsPacketLike(packet)));
    }

    private void replaceOnStanzaEvent() {
	onStanzaEvent = new EventTester<IPacket>();
	verify(connection).onStanzaReceived(argThat(onStanzaEvent));
    }
}
