package com.calclab.emite.client.plugin.dsl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.Message;
import com.calclab.emite.client.plugin.dsl.FilterBuilder;
import com.calclab.emite.client.plugin.dsl.FilterBuilder.ActionBuilder;

public class FilterBuilderTest {

	@Test
	public void testCreateMatchers() {
		final FilterBuilder when = new FilterBuilder(null);

		final Event event = new Event("simple");
		assertWhen(when.Event(event), event);
		assertWhen(when.IQ("theID"), new IQ("theID", IQ.Type.set));
		assertWhen(when.MessageTo("recipient"), new Message("recipient", "theMessage"));

		assertWhen(when.Packet("stream:features"), new BasicPacket("stream:features", null));
	}

	private void assertWhen(final ActionBuilder actionBuilder, final Packet packet) {
		assertTrue(actionBuilder.getCurrentFilter().matches(packet));
	}
}
