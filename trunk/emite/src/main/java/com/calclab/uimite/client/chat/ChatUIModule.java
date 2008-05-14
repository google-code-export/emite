package com.calclab.uimite.client.chat;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class ChatUIModule implements Module {

    public void onLoad(final Container container) {
	final Xmpp xmpp = container.getInstance(Xmpp.class);
	container.registerSingletonInstance(ChatWidgetFactory.class, new ChatWidgetFactory(xmpp));
    }
}
