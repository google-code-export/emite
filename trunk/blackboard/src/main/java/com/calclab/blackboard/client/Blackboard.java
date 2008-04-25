package com.calclab.blackboard.client;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Blackboard extends VerticalPanel {

    public void add(XmppURI user, String message) {
	GWT.log(message, null);
	add(new Label(message));
    }

}
