package com.calclab.emite.client.core.bosh;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.calclab.emite.client.core.bosh.Bosh3Connection;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.IsPacketLike;
import com.calclab.suco.testing.signal.SignalTester;

public class ConnectionTestHelper {

    public final Connection connection;
    private SignalTester<IPacket> onStanzaSignal;
    private final TigaseXMLService xmler;

    public ConnectionTestHelper() {
	xmler = new TigaseXMLService();
	connection = mock(Bosh3Connection.class);
    }

    public Connection getConnection() {
	return connection;
    }

    public void simulateReception(final IPacket packet) {
	if (onStanzaSignal == null)
	    replaceOnStanzaSignal();
	onStanzaSignal.fire(packet);
    }

    public void simulateReception(final String received) {
	final IPacket packet = xmler.toXML(received);
	simulateReception(packet);
    }

    public void verifySentLike(final IPacket packet) {
	verify(connection).send(argThat(new IsPacketLike(packet)));
    }

    private void replaceOnStanzaSignal() {
	onStanzaSignal = new SignalTester<IPacket>();
	verify(connection).onStanzaReceived(argThat(onStanzaSignal));
    }
}
