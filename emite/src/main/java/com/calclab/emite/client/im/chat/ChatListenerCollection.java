package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;

public class ChatListenerCollection extends ArrayList<ChatListener> implements ChatListener {
    private static final long serialVersionUID = 1L;

    public void onMessageReceived(final Chat chat, final Message message) {
	for (final ChatListener listener : this) {
	    listener.onMessageReceived(chat, message);
	}

    }

    public void onMessageSent(final Chat chat, final Message message) {
	for (final ChatListener listener : this) {
	    listener.onMessageSent(chat, message);
	}

    }

}
