package com.calclab.emite.widgets.client.roster;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GWTRosterWidget extends VerticalPanel implements RosterWidget {

    public GWTRosterWidget() {
    }

    public void addItem(final XmppURI jid) {
	final Label label = new Label(jid.toString());
	add(label);
    }

    public void clearItems() {
	this.clear();
    }

    public String[] getParamNames() {
	return null;
    }

    public void setDisconnected() {
	clear();
    }

    public void setParam(final String name, final String value) {
    }

}
