/**
 *
 */
package com.calclab.emite.testing;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;

import org.mockito.ArgumentMatcher;

import com.calclab.emite.client.core.packet.IPacket;

class IsPacketLike extends ArgumentMatcher<IPacket> {
    private final IPacket original;

    public IsPacketLike(final IPacket expected) {
	this.original = expected;
    }

    @Override
    public boolean matches(final Object argument) {
	return areEquals(original, (IPacket) argument);
    }

    private boolean areEquals(final IPacket expected, final IPacket actual) {
	if (actual == null) {
	    return false;
	}
	if (expected.getName().equals(actual.getName())) {
	    final HashMap<String, String> atts = expected.getAttributes();
	    for (final String name : atts.keySet()) {
		if (!expected.hasAttribute(name) || !actual.hasAttribute(name, expected.getAttribute(name))) {

		    assertTrue("Expected attribute " + name + ": " + expected.getAttribute(name) + " but was "
			    + actual.getAttribute(name), false);
		    return false;
		}
	    }
	} else {
	    return false;
	}

	final List<? extends IPacket> expChildren = expected.getChildren();
	final List<? extends IPacket> actChildren = actual.getChildren();
	final int total = expChildren.size();
	final int max = actChildren.size();

	for (int index = 0; index < total; index++) {
	    if (index == max) {
		return false;
	    } else if (!areEquals(expChildren.get(index), actChildren.get(index))) {
		return false;
	    }
	}
	return true;
    }
}