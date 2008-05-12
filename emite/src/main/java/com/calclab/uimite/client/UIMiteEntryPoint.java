package com.calclab.uimite.client;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.uimite.client.chat.ChatUIModule;
import com.calclab.uimite.client.chat.ChatWidgetFactory;
import com.calclab.uimite.client.config.XmppAutoConfigModule;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class UIMiteEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final ModuleContainer container = EmiteModule.create();
	container.load(new XmppAutoConfigModule(), new ChatUIModule());

	final ChatWidgetFactory chatWidgetFactory = container.getInstance(ChatWidgetFactory.class);
	final Widget chat = chatWidgetFactory.createSimpleChat(uri("dani@mandarine"), "dani", uri("test1@mandarine"));
	RootPanel.get().add(chat);
    }

}
