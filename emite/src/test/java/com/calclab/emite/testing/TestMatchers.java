package com.calclab.emite.testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;

@SuppressWarnings("unchecked")
public class TestMatchers {

    private static class IsCollectionLike<T extends Collection> extends ArgumentMatcher<T> {
	private final T expected;

	public IsCollectionLike(final T list) {
	    this.expected = list;
	}

	@Override
	public boolean matches(final Object argument) {
	    final T actual = (T) argument;
	    final Object[] actualArray = actual.toArray();
	    final Object[] expectedArray = expected.toArray();
	    if (actualArray.length != expectedArray.length) {
		return false;
	    }

	    for (int index = 0; index < expectedArray.length; index++) {
		if (!expectedArray[index].equals(actualArray[index])) {
		    return false;
		}
	    }
	    return true;
	}

    }

    private static class IsCollectionOfSize<T> extends ArgumentMatcher<T> {
	private final int size;

	public IsCollectionOfSize(final int size) {
	    this.size = size;
	}

	@Override
	public boolean matches(final Object list) {
	    return ((Collection) list).size() == size;
	}
    }

    private static final TigaseXMLService xmler = new TigaseXMLService();

    public static List hasSame(final List list) {
	return argThat(new IsCollectionLike<List>(list));
    }

    public static Collection isCollectionOfSize(final int size) {
	return argThat(new IsCollectionOfSize<Collection>(size));
    }

    public static List isListOfSize(final int size) {
	return argThat(new IsCollectionOfSize<List>(size));
    }

    public static IPacket packetLike(final IPacket packet) {
	return argThat(new IsPacketLike(packet));
    }

    public static IPacket packetLike(final String xml) {
	return packetLike(xmler.toXML(xml));
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
