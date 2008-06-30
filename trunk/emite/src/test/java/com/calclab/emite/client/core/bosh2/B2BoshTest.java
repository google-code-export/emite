package com.calclab.emite.client.core.bosh2;

import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static com.calclab.emite.testing.MockSlot.verifyCalledWithSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.testing.MockSlot;

public class B2BoshTest {

    private Services services;
    private B2Bosh bosh;

    @Before
    public void beforeTest() {
	services = mock(Services.class);
	bosh = new B2Bosh(services);
    }

    @Test
    public void shouldOnlyAcceptBodys() {
	final MockSlot<B2Error> error = new MockSlot<B2Error>();
	bosh.onError(error);

	final Packet body = new Packet("no-body");
	stub(services.toXML(anyString())).toReturn(body);

	bosh.start(new B2BoshOptions("base", "domain"));
	bosh.handleResponse(200, "");
	verifyCalled(error);
    }

    @Test
    public void shouldSendPacketWhenRunning() throws ConnectorException {
	final Packet body = new Packet("body");
	bosh.start(new B2BoshOptions("base", "domain"));
	bosh.send(body);
	verify(services).toString(same(body));
	verify(services).send(eq("base"), (String) anyObject(), (ConnectorCallback) anyObject());
    }

    @Test
    public void shouldSignalBadStateErrors() {
	final MockSlot<B2Error> error = new MockSlot<B2Error>();
	bosh.onError(error);

	bosh.start(new B2BoshOptions("base", "domain"));
	bosh.handleResponse(400, "");
	verifyCalled(error);
    }

    @Test
    public void shouldSignalBodys() {
	final MockSlot<IPacket> slot = new MockSlot<IPacket>();
	bosh.onBody(slot);

	final Packet body = new Packet("body");
	stub(services.toXML(anyString())).toReturn(body);

	bosh.start(new B2BoshOptions("base", "domain"));
	bosh.handleResponse(200, "");
	verifyCalledWithSame(slot, body);
    }

    @Test
    public void shouldSignalStart() {
	final MockSlot<B2BoshOptions> slot = new MockSlot<B2BoshOptions>();
	bosh.onStart(slot);

	final B2BoshOptions options = new B2BoshOptions("base", "domain");
	bosh.start(options);
	verifyCalledWithSame(slot, options);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenSendAndStop() {
	bosh.send(new Packet("body"));
    }
}
