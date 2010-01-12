package com.calclab.hablar.client.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Listener;

public class ChatLogic {
    private final Chat chat;
    private final ChatView view;

    public ChatLogic(Chat chat, final ChatView view) {
	this.chat = chat;
	this.view = view;

	final String name = chat.getURI().getNode();
	view.setHeaderTitle(name);
	chat.onMessageReceived(new Listener<Message>() {
	    @Override
	    public void onEvent(Message message) {
		view.showMessage(name, message.getBody(), ChatPage.MessageType.incoming);
		view.setStatus("Incoming message from " + name);
	    }
	});
    }

    public void onTalk(String text) {
	view.showMessage("me", text, ChatPage.MessageType.sent);
	chat.send(new Message(text));
	view.clearAndFocus();
    }

}