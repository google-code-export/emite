package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.NoPacket;

public class IQTest {

    @Test
    public void shoudAddQuery() {
	final IQ iq = new IQ(IQ.Type.get);
	iq.addQuery("xmlns:query");
	final IPacket query = iq.getFirstChild("query");
	assertNotSame(NoPacket.INSTANCE, query);
	assertEquals("xmlns:query", query.getAttribute("xmlns"));
    }
}
