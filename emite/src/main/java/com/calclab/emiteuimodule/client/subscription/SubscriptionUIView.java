package com.calclab.emiteuimodule.client.subscription;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public interface SubscriptionUIView extends View {

    public void confirmSusbscriptionRequest(final XmppURI presence, String nick);

}
