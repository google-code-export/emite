/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
