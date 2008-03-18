package com.calclab.emite.client.plugin;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.Message;

public class FilterBuilderTest {

	@Test
	public void testCreateMatchers() {
		final FilterBuilder builder = new FilterBuilder(null, null);

		final Event event = new Event("simple");
		builder.Event(event);
		assertTrue(builder.filter.matches(event));

		builder.IQ("theID");
		assertTrue(builder.filter.matches(new IQ("theID", IQ.Type.set)));

		builder.MessageTo("recipient");
		assertTrue(builder.filter.matches(new Message("recipient", "theMessage")));
	}
}
