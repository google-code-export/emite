package com.calclab.emite.client.xmpp.resource;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.TestMatchers;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.j2se.services.TigaseXMLService;

public class ResourceBindingManagerTest {

    private Emite emite;
    private ResourceBindingManager manager;
    private TigaseXMLService xmler;

    @Before
    public void aaCreate() {
	xmler = new TigaseXMLService();
	emite = mock(Emite.class);
	manager = new ResourceBindingManager(emite);
    }

    @Test
    public void shouldInforSessionWhenBinded() {
	final String received = "<iq type=\"result\" id=\"bind_2\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>";
	manager.eventBinded(xmler.toXML(received));
	verify(emite).publish(
		TestMatchers.isPacket(SessionManager.Events.binded.Params("uri", "somenode@example.com/someresource")));
    }

    @Test
    public void shouldRequestBindingWhenAuthorized() {
	manager.eventAuthorized();
	// TODO: which packet?
	verify(emite).send(anyString(), (IPacket) anyObject(), (PacketListener) anyObject());
    }
}
