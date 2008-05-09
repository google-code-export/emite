package com.calclab.uimite.client;

import com.calclab.emite.client.Xmpp;
import com.calclab.uimite.client.chat.ChatUIModule;
import com.calclab.uimite.client.config.XmppAutoConfigModule;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class UIMiteEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final Xmpp xmpp = Xmpp.create();
	XmppAutoConfigModule.load(xmpp);
	ChatUIModule.load(xmpp);

	RootPanel.get().add(new Label("hola"));
    }

}
