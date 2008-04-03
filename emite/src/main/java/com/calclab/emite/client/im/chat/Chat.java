package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;

public class Chat {
	private final ArrayList<ChatListener> listeners;

	public Chat() {
		this.listeners = new ArrayList<ChatListener>();
	}

	public void addListener(final ChatListener listener) {
		listeners.add(listener);
	}

	void process(final Message message) {

	}

}
