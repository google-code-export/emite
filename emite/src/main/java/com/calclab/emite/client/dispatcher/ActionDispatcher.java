package com.calclab.emite.client.dispatcher;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.action.Action;
import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

public class ActionDispatcher implements Dispatcher, BoshListener {
	private final ArrayList<Action> commands;
	private final Logger logger;
	private final Parser parser;

	public ActionDispatcher(final Parser parser, final Logger logger) {
		this.parser = parser;
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
		logger.debug("DISPATCHER LOOP BEGINS");
		final List<? extends Packet> stanzas = parser.extractStanzas(response);
		for (final Packet stanza : stanzas) {
			fireStanza(stanza, commands);
		}
		logger.debug("DISPATCHER LOOP ENDS");
	}

	public void publish(final Packet stanza) {
		logger.debug("PUBLISHED: \n {0}", stanza);
		fireStanza(stanza, commands);
	}

	private void fireStanza(final Packet stanza, final ArrayList<Action> listeners) {
		for (final Action listener : listeners) {
			listener.handle(stanza);
		}
	}

}
