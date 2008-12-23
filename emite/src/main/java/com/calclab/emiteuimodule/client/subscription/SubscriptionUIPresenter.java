package com.calclab.emiteuimodule.client.subscription;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emiteuimodule.client.SubscriptionMode;
import com.calclab.suco.client.events.Event0;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

public class SubscriptionUIPresenter implements SubscriptionUI {

    private SubscriptionUIView view;
    private final SubscriptionManager subscriptionManager;
    private SubscriptionMode mode;
    private final Event0 onUserAlert;

    public SubscriptionUIPresenter(final SubscriptionManager subscriptionManager) {
	this.subscriptionManager = subscriptionManager;
	this.onUserAlert = new Event0("onUserAlert");

	subscriptionManager.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    public void onEvent(final XmppURI jid, final String nick) {
		switch (mode) {
		case autoAcceptAll:
		    subscriptionManager.approveSubscriptionRequest(jid, nick);
		    break;
		case autoRejectAll:
		    subscriptionManager.refuseSubscriptionRequest(jid);
		default:
		    onUserAlert.fire();
		    view.confirmSusbscriptionRequest(jid, nick);
		    break;
		}
	    }
	});
    }

    public View getView() {
	return view;
    }

    public void init(final SubscriptionUIView view) {
	this.view = view;
    }

    public void onPresenceAccepted(final XmppURI jid, final String nick) {
	subscriptionManager.approveSubscriptionRequest(jid, nick);
    }

    public void onPresenceNotAccepted(final XmppURI jid) {
	subscriptionManager.refuseSubscriptionRequest(jid);
    }

    public void onUserAlert(final Listener0 listener) {
	onUserAlert.add(listener);
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
	mode = subscriptionMode;
    }

}
