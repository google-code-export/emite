package com.calclab.emite.core.client.xmpp.session;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.listener.Listener;

/**
 * Handles IQ listeners and generates uniqe ids based on category strings. Used
 * by XmppSession and not intended to be used outside
 * 
 */
class IQManager {
    private int id;
    private final HashMap<String, Listener<IPacket>> listeners;

    public IQManager() {
	id = 0;
	this.listeners = new HashMap<String, Listener<IPacket>>();
    }

    public boolean handle(final IPacket received) {
	final String key = received.getAttribute("id");
	final Listener<IPacket> listener = listeners.remove(key);
	final boolean isHandled = listener != null;
	if (isHandled) {
	    Log.debug("ID LISTENER FOUNDED : " + key);
	    listener.onEvent(received);
	}
	return isHandled;
    }

    public String register(final String category, final Listener<IPacket> listener) {
	id++;
	final String key = category + "_" + id;
	listeners.put(key, listener);
	return key;
    }
}
