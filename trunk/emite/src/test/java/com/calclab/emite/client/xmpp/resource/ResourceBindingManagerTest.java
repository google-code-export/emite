package com.calclab.emite.client.xmpp.resource;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalledWith;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.bosh3.ConnectionTestHelper;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockSlot;

public class ResourceBindingManagerTest {
    private ResourceBindingManager manager;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTests() {
	helper = new ConnectionTestHelper();
	manager = new ResourceBindingManager(helper.getConnection());
    }

    @Test
    public void shouldPerformBinding() {
	manager.bindResource("resource");
	helper.verifySentLike(new IQ(IQ.Type.set).Includes("bind", "urn:ietf:params:xml:ns:xmpp-bind"));
    }

    @Test
    public void shouldSignalIfBindedSucceed() {
	final MockSlot<XmppURI> onBindedSlot = new MockSlot<XmppURI>();
	manager.onBinded(onBindedSlot);
	helper.simulateReception("<iq type='result' id='bind-resource'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>"
		+ "<jid>somenode@example.com/someresource</jid></bind></iq>");

	verifyCalledWith(onBindedSlot, uri("somenode@example.com/someresource"));

    }
}
