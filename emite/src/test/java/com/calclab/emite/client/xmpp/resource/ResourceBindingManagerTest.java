package com.calclab.emite.client.xmpp.resource;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteStub;

public class ResourceBindingManagerTest {

    private EmiteStub emite;

    @Before
    public void aaCreate() {
	emite = new EmiteStub();
	final ResourceBindingManager manager = new ResourceBindingManager(emite);
	manager.install();
    }

    @Test
    public void shouldPerfomrBinding() {
	emite.receives(SessionManager.Events.login(XmppURI.parse("name@domain/someresource"), "password"));
	emite.receives(SessionManager.Events.onAuthorized);
	emite.verifySendCallback(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind"));
	emite.answer("<iq type=\"result\" id=\"bind_2\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");
	emite.verifyPublished(SessionManager.Events.onBinded.Params("uri", "somenode@example.com/someresource"));
    }
}
