package com.calclab.uimite.client.chat;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.uimite.client.chat.ChatView.ChatViewListener;

public class ChatPresenter {

    public ChatPresenter(final Chat chat, final ChatView view) {

	chat.addListener(new ChatListener() {
	    public void onMessageReceived(final Chat chat, final Message message) {
		view.show(message.getFromURI().toString(), message.getBody());
	    }

	    public void onMessageSent(final Chat chat, final Message message) {
		view.show("me", message.getBody());
	    }
	});

	view.addListener(new ChatViewListener() {
	    public void onContentChanged() {
	    }

	    public void onSend(final String text) {
		chat.send(text);
	    }
	});
    }

}
