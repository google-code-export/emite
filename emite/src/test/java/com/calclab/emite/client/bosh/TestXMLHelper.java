package com.calclab.emite.client.bosh;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.bosh.XMLHelper;

public class TestXMLHelper {

	@Test
	public void testExtraction() {
		final String response = "<body xmlns=\"http://jabber.org/protocol/httpbind\" "
				+ "xmlns:stream=\"http://etherx.jabber.org/streams\" authid=\"40794c6\" sid=\"40794c6\" "
				+ "secure=\"true\" requests=\"2\" inactivity=\"30\" polling=\"5\" wait=\"60\" ver=\"1.6\">";
		final String sid = XMLHelper.extractAttribute("sid", response);
		assertEquals("40794c6", sid);
		final int inactivity = XMLHelper.extractIntegerAttribute("inactivity", response);
		assertEquals(30, inactivity);
	}
}
