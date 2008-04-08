package com.calclab.emite.client.packet;

import java.util.Queue;

import com.calclab.emite.client.core.packet.APacket;
import com.calclab.emite.client.core.services.XMLService;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

public class TigaseXMLService implements XMLService {
	private final SimpleParser parser;

	public TigaseXMLService() {
		parser = new SimpleParser();
	}

	public String toString(final APacket aPacket) {
		return aPacket.toString();
	}

	public APacket toXML(final String xml) {
		final DomBuilderHandler handler = new DomBuilderHandler();
		parser.parse(handler, xml.toCharArray(), 0, xml.length());
		final Queue<Element> parsedElements = handler.getParsedElements();

		final Element body = parsedElements.poll();
		return new TigasePacket(body);
	}
}
