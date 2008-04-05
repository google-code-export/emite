package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.xmpp.stanzas.Presence;

public interface PresenceListener {
	void onPresenceReceived(Presence presence);

	void onSubscriptionRequest(Presence presence);
}
