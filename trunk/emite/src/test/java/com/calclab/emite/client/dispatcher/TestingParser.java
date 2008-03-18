package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

public class TestingParser implements Parser {
	private final SimpleParser parser;

	public TestingParser(final Logger logger) {
		parser = new SimpleParser();
	}

	public List<? extends Packet> extractStanzas(final String xml) {

		final DomBuilderHandler handler = new DomBuilderHandler();
		parser.parse(handler, xml.toCharArray(), 0, xml.length());
		final Queue<Element> parsedElements = handler.getParsedElements();

		final Element root = parsedElements.poll();
		// skip body node
		final List<Element> elements = root.getChildren(); // root.getChildren().get(0).getChildren();
		final ArrayList<Packet> stanzas = new ArrayList<Packet>();
		for (int index = 0; index < elements.size(); index++) {
			stanzas.add(new TigasePacket(elements.get(0)));
		}

		return stanzas;

	}
}
