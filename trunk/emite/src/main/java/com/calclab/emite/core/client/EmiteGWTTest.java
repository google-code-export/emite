/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.PacketTestSuite;
import com.calclab.emite.core.client.services.gwt.GWTXMLService;
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
