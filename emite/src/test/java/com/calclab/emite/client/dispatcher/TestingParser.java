package com.calclab.emite.client.dispatcher;

import java.util.List;

import com.calclab.emite.client.dispatcher.parser.ParserException;
import com.calclab.emite.client.dispatcher.parser.SimpleBuilder;
import com.calclab.emite.client.dispatcher.parser.SimpleParser;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

public class TestingParser implements Parser {
	private final SimpleParser parser;

	public TestingParser(final Logger logger) {
		final SimpleBuilder builder = new SimpleBuilder();
		parser = new SimpleParser(builder, logger);
	}

	public List<? extends Packet> extractStanzas(final String xml) {

		Packet parsed;
		try {
			parsed = parser.parse(xml);
			return parsed.getChildren();
		} catch (final ParserException e) {
			e.printStackTrace();
			return null;
		}

		// skip body node

		// final NodeList childNodes =
		// parsed.getChildNodes().item(0).getChildNodes();
		// final ArrayList<Packet> stanzas = new ArrayList<Packet>();
		// for (int index = 0; index < childNodes.getLength(); index++) {
		// child = childNodes.item(index);
		// if (child.getNodeType() == Node.ELEMENT_NODE) {
		// stanzas.add(new XMLPacket((Element) child));
		// }
		// }
		// return stanzas;

	}
}
