package com.calclab.emite.hablar.client.pages;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class LoggerController {

    public LoggerController(final Connection connection, final LoggerPage view) {
	view.onClear(new Listener0() {
	    public void onEvent() {
		view.clearStanzas();
	    }
	});

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		view.showReceived(stanza.toString());
	    }
	});
	connection.onStanzaSent(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		view.showSent(stanza.toString());
	    }
	});
    }

}
