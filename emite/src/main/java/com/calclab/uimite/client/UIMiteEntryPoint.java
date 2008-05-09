package com.calclab.uimite.client;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.container.BasicContainer;
import com.calclab.emite.client.container.Container;
import com.calclab.uimite.client.chat.ChatUIModule;
import com.calclab.uimite.client.config.XmppAutoConfigModule;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIMiteEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final Container container = new BasicContainer();
	EmiteModule.load(container);
	XmppAutoConfigModule.load(container);
	ChatUIModule.load(container);

	final ChatUIModule chatUIModule = container.get(ChatUIModule.class);
	final UIView chat = chatUIModule.createChat(uri("dani@localhost"), "dani", uri("test1@localhost"));
	RootPanel.get().add((Widget) chat);
    }

}
