package com.calclab.emite.client.action;

import java.util.ArrayList;

import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLPacket;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ActionDispatcher implements Dispatcher, BoshListener {
	private final ArrayList<Action> commands;
	private final Logger logger;

	public ActionDispatcher(final Logger logger) {
		this.logger = logger;
		this.commands = new ArrayList<Action>();
	}

	public void addListener(final Action listener) {
		commands.add(listener);
	}

	public void onError(final Throwable error) {
		logger.debug("Xmpp::onError {0}", error);
	}

	public void onRequest(final String request) {
	}

	public void onResponse(final String response) {
		logger.debug("RESPONSE: \n {0}", response);
		final ArrayList<Packet> stanzas = extractStanzas(response);
		for (final Packet stanza : stanzas) {
			fireStanza(stanza, commands);
		}
	}

	public void publish(final Packet stanza) {
		logger.debug("PUBLISHED: \n {0}", stanza);
		fireStanza(stanza, commands);
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

	private void fireStanza(final Packet stanza, final ArrayList<Action> listeners) {
		for (final Action listener : listeners) {
			listener.handle(stanza);
		}
	}

}
