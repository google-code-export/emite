package com.calclab.uimite.client;

import com.calclab.emite.client.EmiteModule;
import com.calclab.emite.client.container.Container;
import com.calclab.emite.client.container.BasicContainer;
import com.calclab.uimite.client.chat.ChatUIModule;
import com.calclab.uimite.client.config.XmppAutoConfigModule;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class UIMiteEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final Container container = new BasicContainer();
	EmiteModule.load(container);
	XmppAutoConfigModule.load(container);
	ChatUIModule.load(container);

	RootPanel.get().add(new Label("hola"));
    }

}
