package com.calclab.emite.client.xmpp.session;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.suco.client.signal.Slot;

public class IQManager {
    private int id;
    private final HashMap<String, Slot<IPacket>> listeners;

    public IQManager() {
	id = 0;
	this.listeners = new HashMap<String, Slot<IPacket>>();
    }

    public boolean handle(final IPacket received) {
	final String key = received.getAttribute("id");
	final Slot<IPacket> slot = listeners.remove(key);
	final boolean isHandled = slot != null;
	if (isHandled) {
	    Log.debug("ID LISTENER FOUNDED : " + key);
	    slot.onEvent(received);
	}
	return isHandled;
    }

    public String register(final String category, final Slot<IPacket> slot) {
	id++;
	final String key = category + "_" + id;
	listeners.put(key, slot);
	return key;
    }
}
