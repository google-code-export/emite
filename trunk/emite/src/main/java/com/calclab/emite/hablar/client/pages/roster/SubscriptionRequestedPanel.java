package com.calclab.emite.hablar.client.pages.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubscriptionRequestedPanel extends VerticalPanel {

    public static interface SubscriptionRequestedListener {
	void accepted(SubscriptionRequestedPanel panel, XmppURI jid, String nick);

	void rejected(SubscriptionRequestedPanel panel, XmppURI jid);
    }

    public SubscriptionRequestedPanel(final XmppURI jid, final String nick, final SubscriptionRequestedListener listener) {
	setStyleName("notification");

	add(new Label(nick + " (" + jid.toString() + ") wants to subscribe to your presence."));
	final FlowPanel flow = new FlowPanel();
	final Button btnAccept = new Button("Accept", new ClickListener() {
	    public void onClick(final Widget arg0) {
		listener.accepted(SubscriptionRequestedPanel.this, jid, nick);
	    }
	});
	flow.add(btnAccept);
	final Button btnReject = new Button("Reject", new ClickListener() {
	    public void onClick(final Widget arg0) {
		listener.rejected(SubscriptionRequestedPanel.this, jid);
	    }
	});
	flow.add(btnReject);
	add(flow);
    }

}
