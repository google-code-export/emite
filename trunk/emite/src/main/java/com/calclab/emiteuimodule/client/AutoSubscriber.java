package com.calclab.emiteuimodule.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.suco.client.listener.Listener2;

public class AutoSubscriber {

    private SubscriptionMode mode;

    public AutoSubscriber(final SubscriptionManager manager) {

	manager.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    public void onEvent(final XmppURI jid, final String name) {
		if (mode == SubscriptionMode.autoAcceptAll) {
		    manager.approveSubscriptionRequest(jid, name);
		} else if (mode == SubscriptionMode.autoRejectAll) {
		    manager.refuseSubscriptionRequest(jid);
		} else {
		    // no se... depende de tu código
		}
	    }
	});
    }

    public void setSubscriptionMode(final SubscriptionMode mode) {
	this.mode = mode;
    }

}
