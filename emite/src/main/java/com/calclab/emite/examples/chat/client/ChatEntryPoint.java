package com.calclab.emite.examples.chat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ChatEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	final RootPanel root = RootPanel.get();
	root.add(new Label("hola"));
    }

}
