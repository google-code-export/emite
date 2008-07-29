package com.calclab.emite.widgets.client.roster;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.widgets.client.base.EmiteWidget;

public interface RosterWidget extends EmiteWidget {

    void addItem(XmppURI jid);

    void clearItems();

    void setDisconnected();

}
