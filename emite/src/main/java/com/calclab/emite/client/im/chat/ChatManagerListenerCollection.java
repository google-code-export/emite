package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

public class ChatManagerListenerCollection extends ArrayList<ChatManagerListener> implements ChatManagerListener {
    private static final long serialVersionUID = 1L;

    public void onChatClosed(final Chat chat) {
	for (final ChatManagerListener listener : this) {
	    listener.onChatClosed(chat);
	}
    }

    public void onChatCreated(final Chat chat) {
	for (final ChatManagerListener listener : this) {
	    listener.onChatCreated(chat);
	}

    }

}
