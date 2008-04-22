package com.calclab.emite.j2se.services;

import java.util.Queue;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;

import com.calclab.emite.client.core.packet.IPacket;

public class TigaseXMLService {
    private final SimpleParser parser;

    public TigaseXMLService() {
	parser = new SimpleParser();
    }

    public String toString(final IPacket iPacket) {
	return iPacket != null ? iPacket.toString() : "null";
    }

    public IPacket toXML(final String xml) {
	final DomBuilderHandler handler = new DomBuilderHandler();
	parser.parse(handler, xml.toCharArray(), 0, xml.length());
	final Queue<Element> parsedElements = handler.getParsedElements();

	final Element body = parsedElements.poll();
	// TODO: body es null si no ha parseado bien ¿qué hacemos?
	return new TigasePacket(body);
    }
}
