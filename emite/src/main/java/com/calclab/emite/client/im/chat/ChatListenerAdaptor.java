package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.suco.client.signal.Slot;

/**
 * Using it before convert to signals
 */
public class ChatListenerAdaptor {

    public ChatListenerAdaptor(final Chat chat, final ChatListener listener) {
	chat.onMessageReceived(new Slot<Message>() {
	    public void onEvent(final Message message) {
		listener.onMessageReceived(chat, message);
	    }
	});

	chat.onMessageSent(new Slot<Message>() {
	    public void onEvent(final Message message) {
		listener.onMessageSent(chat, message);
	    }
	});
    }

}
