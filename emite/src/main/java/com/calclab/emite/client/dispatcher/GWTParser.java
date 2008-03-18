package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;

import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLPacket;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class GWTParser implements Parser {

	public ArrayList<Packet> extractStanzas(final String xml) {
		Node child;

		final Document parsed = XMLParser.parse(xml);
		// skip body node
		final NodeList childNodes = parsed.getChildNodes().item(0).getChildNodes();
		final ArrayList<Packet> stanzas = new ArrayList<Packet>();
		for (int index = 0; index < childNodes.getLength(); index++) {
			child = childNodes.item(index);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				stanzas.add(new XMLPacket((Element) child));
			}
		}
		return stanzas;
	}

}
