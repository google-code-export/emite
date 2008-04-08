package com.calclab.emite.client.core.bosh;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;

class IDManager {
    private int id;
    private final HashMap<String, PacketListener> listeners;

    public IDManager() {
	id = 0;
	this.listeners = new HashMap<String, PacketListener>();
    }

    public void handle(final IPacket received) {
	final String key = received.getAttribute("id");
	final PacketListener listener = listeners.remove(key);
	if (listener != null) {
	    Log.debug("ID LISTENER FOUNDED : " + key);
	    listener.handle(received);
	}
    }

    public String register(final String category, final PacketListener listener) {
	id++;
	final String key = category + "_" + id;
	listeners.put(key, listener);
	return key;
    }

}
