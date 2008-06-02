package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.modular.client.signal.Slot;

public class PresenceManagerListenerAdapter {

    public PresenceManagerListenerAdapter(final PresenceManager manager, final PresenceListener listener) {
	manager.onPresenceReceived(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		listener.onPresenceReceived(presence);
	    }
	});
    }

}
