package com.calclab.emite.client.packet.gwt;

import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLService;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class GWTXMLService implements XMLService {

    // TODO?
    public String toString(final Packet packet) {
        return packet.toString();
    }

    public Packet toXML(final String xml) {
        final Document parsed = XMLParser.parse(xml);
        final Node body = parsed.getChildNodes().item(0);
        return new GWTPacket((Element) body);
    }

}
