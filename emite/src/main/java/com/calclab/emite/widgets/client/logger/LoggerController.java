package com.calclab.emite.widgets.client.logger;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;

public class LoggerController {
    private final Connection connection;

    public LoggerController(final Connection connection) {
	this.connection = connection;
    }

    public void setWidget(final LoggerWidget widget) {
	widget.onClear.add(new Listener0() {
	    public void onEvent() {
		widget.clearContent();
	    }
	});

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.RECEIVED, stanza.toString());
	    }
	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.SENT, stanza.toString());
	    }
	});

	connection.onError(new Listener<String>() {
	    public void onEvent(final String message) {
		Log.debug("ERROR: " + message);
		widget.write(LoggerWidget.ERROR, message);
	    }
	});
    }
}
