package com.calclab.emite.j2se.swing.log;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class LogControl {

    public LogControl(final Connection connection, final LogPanel logPanel) {
	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket packet) {
		logPanel.showIncomingPacket(packet);
	    }
	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    public void onEvent(final IPacket packet) {
		logPanel.showOutcomingPacket(packet);
	    }
	});

	logPanel.onClear(new Listener0() {
	    public void onEvent() {
		logPanel.clear();
	    }
	});
    }

}
