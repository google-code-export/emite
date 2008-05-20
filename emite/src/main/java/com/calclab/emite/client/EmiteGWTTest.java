package com.calclab.emite.client;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.PacketTestSuite;
import com.calclab.emite.client.core.services.gwt.GWTXMLService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

public class EmiteGWTTest extends GWTTestCase {

    @Override
    public String getModuleName() {
	return "com.calclab.emite.Emite";
    }

    public void testPacket() {
	PacketTestSuite.runPacketTests(new PacketTestSuite.Helper() {
	    public void assertEquals(final Object expected, final Object actual) {
		theEqualsMethod(expected, actual);
	    }

	    public void assertTrue(final String message, final boolean condition) {
		theTrueMethod(message, condition);
	    }

	    public IPacket createPacket(final String nodeName) {
		return GWTXMLService.toXML("<" + nodeName + "/>");
	    }

	    public void log(final String message) {
		GWT.log(message, null);
	    }
	});
    }

    public void theEqualsMethod(final Object expected, final Object actual) {
	assertEquals(expected, actual);
    }

    public void theTrueMethod(final String message, final boolean condition) {
	assertTrue(message, condition);
    }

}
