package com.calclab.emite.client.xmpp.resource;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalledWith;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.MockSlot;

public class ResourceBindingManagerTest {

    private EmiteTestHelper emite;
    private ResourceBindingManager manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = new ResourceBindingManager(emite);
    }

    @Test
    public void shouldPerformBinding() {
	final MockSlot<XmppURI> listener = new MockSlot<XmppURI>();
	manager.onBinded(listener);
	manager.bindResource("resource");

	emite.verifyIQSent(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind"));
	emite.answer("<iq type=\"result\" id=\"bind_2\"><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\">"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

	verifyCalledWith(listener, uri("somenode@example.com/someresource"));
    }
}
