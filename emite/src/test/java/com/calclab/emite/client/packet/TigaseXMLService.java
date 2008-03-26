package com.calclab.emite.client.packet;

import java.util.Queue;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

import com.calclab.emite.client.packet.Packet;

public class TigaseXMLService implements XMLService {
	private final SimpleParser parser;

	public TigaseXMLService() {
		parser = new SimpleParser();
	}

	public String toString(final Packet packet) {
		return packet.toString();
	}

	public Packet toXML(final String xml) {
		final DomBuilderHandler handler = new DomBuilderHandler();
		parser.parse(handler, xml.toCharArray(), 0, xml.length());
		final Queue<Element> parsedElements = handler.getParsedElements();

		final Element body = parsedElements.poll();
		return new TigasePacket(body);
	}
}
