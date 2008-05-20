/**
 *
 */
package com.calclab.emite.client.core.packet;

import org.junit.Assert;

import com.calclab.emite.client.core.packet.PacketTestSuite.Helper;

public abstract class AbstractHelper implements Helper {
    public void assertEquals(final Object expected, final Object actual) {
	Assert.assertEquals(expected, actual);
    }

    public void assertTrue(final String message, final boolean condition) {
	Assert.assertTrue(message, condition);
    }

    public void log(final String message) {

    }

}