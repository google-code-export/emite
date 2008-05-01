package com.calclab.emite.testing;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;

public class EmiteStubTest {

    private EmiteTestHelper emite;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
    }

    @Test
    public void testPublished() {
	emite.publish(new Packet("one"));
	emite.publish(new Packet("two"));
	emite.verifyPublished(new Packet("one"));
	emite.verifyPublished(new Packet("two"));
	emite.verifyNotPublished(new Packet("threw"));
	emite.verifyPublishedTimes(2);
	emite.clearPublished();
	emite.verifyPublishedTimes(0);
    }
}
