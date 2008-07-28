package com.calclab.emite.widgets.client.logger;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot0;

public class LoggerController {
    private final Connection connection;

    public LoggerController(final Connection connection) {
	this.connection = connection;
    }

    public void setWidget(final LoggerWidget widget) {
	widget.onClear.add(new Slot0() {
	    public void onEvent() {
		widget.clearContent();
	    }
	});

	connection.onStanzaReceived(new Slot<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.RECEIVED, stanza.toString());
	    }
	});

	connection.onStanzaSent(new Slot<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.SENT, stanza.toString());
	    }
	});

	connection.onError(new Slot<String>() {
	    public void onEvent(final String message) {
		Log.debug("ERROR: " + message);
		widget.write(LoggerWidget.ERROR, message);
	    }
	});
    }
}
