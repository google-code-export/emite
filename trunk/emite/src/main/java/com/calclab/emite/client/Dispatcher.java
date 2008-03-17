package com.calclab.emite.client;

import java.util.ArrayList;

import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLPacket;
import com.calclab.emite.client.subscriber.PacketSubscriber;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class Dispatcher implements BoshListener {
	private final Logger logger;
	private final ArrayList<PacketSubscriber> requestListeners;
	private final ArrayList<PacketSubscriber> responseListeners;

	public Dispatcher(final Logger logger) {
		this.logger = logger;
		this.responseListeners = new ArrayList<PacketSubscriber>();
		this.requestListeners = new ArrayList<PacketSubscriber>();
	}

	public void addResponseListener(final PacketSubscriber listener) {
		responseListeners.add(listener);
	}

	public void onError(final Throwable error) {
		logger.debug("Xmpp::onError {0}", error);
	}

	public void onRequest(final String request) {
		logger.debug("REQUEST === {0} ===", request);
		fireStanzas(extractStanzas(request), requestListeners);
	}

	public void onResponse(final String response) {
		logger.debug("RESPONSE === {0} ===", response);
		fireStanzas(extractStanzas(response), responseListeners);
	}

	public void publish(final Packet stanza) {
		logger.debug("EVENT === {0} ===", stanza);
		fireStanza(stanza, responseListeners);
	}

	private ArrayList<Packet> extractStanzas(final String request) {
		Node child;

		final Document parsed = XMLParser.parse(request);
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

	private void fireStanza(final Packet stanza, final ArrayList<PacketSubscriber> listeners) {
		for (final PacketSubscriber listener : listeners) {
			if (listener.getMatcher().matches(stanza)) {
				listener.handle(stanza);
			}
		}
	}

	private void fireStanzas(final ArrayList<Packet> stanzas, final ArrayList<PacketSubscriber> listeners) {
		for (final Packet stanza : stanzas) {
			fireStanza(stanza, listeners);
		}
	}

}
