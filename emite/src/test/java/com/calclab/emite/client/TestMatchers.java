package com.calclab.emite.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

@SuppressWarnings("unchecked")
public class TestMatchers {

    public static class IsListOfTwoElements<T> extends ArgumentMatcher<List<T>> {
	private final int size;

	public IsListOfTwoElements(final int size) {
	    this.size = size;
	}

	@Override
	public boolean matches(final Object list) {
	    return ((List) list).size() == size;
	}
    }

    public static class IsPacketLike extends ArgumentMatcher<IPacket> {
	private final IPacket original;

	public IsPacketLike(final IPacket expected) {
	    this.original = expected;
	}

	@Override
	public boolean matches(final Object argument) {
	    return areEquals(original, (IPacket) argument);
	}

	private boolean areEquals(final IPacket expected, final IPacket actual) {
	    if (expected.getName().equals(actual.getName())) {
		final HashMap<String, String> atts = expected.getAttributes();
		for (final String name : atts.keySet()) {
		    if (!expected.hasAttribute(name) || !actual.getAttribute(name).equals(expected.getAttribute(name))) {
			return false;
		    }
		}
	    } else {
		return false;
	    }

	    final List<? extends IPacket> expChildren = expected.getChildren();
	    final List<? extends IPacket> actChildren = actual.getChildren();
	    if (expChildren.size() != actChildren.size()) {
		return false;
	    }
	    final int total = expChildren.size();
	    for (int index = 0; index < total; index++) {
		if (!areEquals(expChildren.get(index), actChildren.get(index))) {
		    return false;
		}
	    }
	    return true;
	}

    }

    public static List<RosterItem> isListOfSize(final int size) {
	return argThat(new IsListOfTwoElements<RosterItem>(size));
    }

    public static IPacket isPacket(final IPacket packet) {
	return argThat(new IsPacketLike(packet));
    }

    @Test
    public void testIsPacketLike() {
	final IPacket expected = new IQ(Type.result).With("name", "value").With(new BasicStanza("query", "xmlns"));
	final IsPacketLike matcher = new IsPacketLike(expected);

	assertFalse(matcher
		.matches(new BasicStanza("iq", "jabber:client").With("type", "result").With("name", "value")));
	assertTrue(matcher.matches(new BasicStanza("iq", "jabber:client").With("type", "result").With("name", "value")
		.With(new BasicStanza("query", "xmlns"))));
    }
}
