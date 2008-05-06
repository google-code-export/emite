/**
 *
 */
package com.calclab.emite.testing;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;

import org.mockito.ArgumentMatcher;

import com.calclab.emite.client.core.packet.IPacket;

public class IsPacketLike extends ArgumentMatcher<IPacket> {
    private final IPacket original;

    public IsPacketLike(final IPacket expected) {
	this.original = expected;
    }

    public boolean matches(final IPacket actual, final PrintStream out) {
	final String result = areEquals(original, actual);
	out.print(result);
	return result == null;
    }

    @Override
    public boolean matches(final Object argument) {
	return areEquals(original, (IPacket) argument) == null;
    }

    private String areEquals(final IPacket expected, final IPacket actual) {
	if (actual == null) {
	    return fail("element", expected.toString(), "[null]");
	}
	if (expected.getName().equals(actual.getName())) {
	    final HashMap<String, String> atts = expected.getAttributes();
	    for (final String name : atts.keySet()) {
		if (!expected.hasAttribute(name) || !actual.hasAttribute(name, expected.getAttribute(name))) {
		    return fail("attribute " + name, expected.getAttribute(name), actual.getAttribute(name));
		}
	    }
	} else {
	    return fail("element name", expected.getName(), actual.getName());
	}

	final String expectedText = expected.getText();
	if (expectedText != null && !expectedText.equals(actual.getText())) {
	    return fail("text value", expectedText, actual.getText());
	}

	final List<? extends IPacket> expChildren = expected.getChildren();
	final List<? extends IPacket> actChildren = actual.getChildren();
	final int total = expChildren.size();
	final int max = actChildren.size();

	for (int index = 0; index < total; index++) {
	    if (index == max) {
		return fail("number of childrens for " + expected.getName(), Integer.toString(total), Integer
			.toString(max));
	    } else {
		final String result = areEquals(expChildren.get(index), actChildren.get(index));
		if (result != null)
		    return result;
	    }
	}
	return null;
    }

    private String fail(final String target, final String expected, final String actual) {
	return "failed on " + target + ". expected: " + expected + " but was " + actual;
    }
}
