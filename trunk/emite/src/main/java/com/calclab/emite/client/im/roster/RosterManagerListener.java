package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.Presence;

public interface RosterManagerListener {

    void onSubscriptionRequest(Presence presence, SubscriptionMode currentMode);

    void onUnsubscribedReceived(Presence presence, SubscriptionMode currentMode);
}
