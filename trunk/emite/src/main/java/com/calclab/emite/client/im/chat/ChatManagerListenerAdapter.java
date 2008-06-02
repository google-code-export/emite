package com.calclab.emite.client.im.chat;

import com.calclab.modular.client.signal.Slot;

public class ChatManagerListenerAdapter {

    public ChatManagerListenerAdapter(final ChatManager manager, final ChatManagerListener listener) {
	manager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		listener.onChatClosed(chat);
	    }
	});

	manager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		listener.onChatCreated(chat);
	    }

	});
    }

}
