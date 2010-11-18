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
package com.calclab.emite.hablar.client.pages.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubscriptionRequestedPanel extends VerticalPanel {

    public static interface SubscriptionRequestedListener {
	void accepted(SubscriptionRequestedPanel panel, XmppURI jid, String nick);

	void rejected(SubscriptionRequestedPanel panel, XmppURI jid);
    }

    public SubscriptionRequestedPanel(final XmppURI jid, final String nick, final SubscriptionRequestedListener listener) {
	setStyleName("notification");

	add(new Label(nick + " (" + jid.toString() + ") wants to subscribe to your presence."));
	final FlowPanel flow = new FlowPanel();
	final Button btnAccept = new Button("Accept", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		listener.accepted(SubscriptionRequestedPanel.this, jid, nick);
	    }
	});
	flow.add(btnAccept);
	final Button btnReject = new Button("Reject", new ClickHandler() {
	    public void onClick(final ClickEvent event) {
		listener.rejected(SubscriptionRequestedPanel.this, jid);
	    }
	});
	flow.add(btnReject);
	add(flow);
    }

}
