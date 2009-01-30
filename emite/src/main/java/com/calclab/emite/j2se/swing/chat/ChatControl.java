package com.calclab.emite.j2se.swing.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class ChatControl {

    public ChatControl(final ChatManager manager, final Chat chat, final ChatPanel chatPanel) {
	chatPanel.onClose(new Listener0() {
	    public void onEvent() {
		manager.close(chat);
	    }
	});
	chatPanel.onSend(new Listener<String>() {
	    public void onEvent(final String text) {
		chat.send(new Message(text));
		chatPanel.clearMessage();
	    }
	});

	chat.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
	    }
	});
	chat.onMessageSent(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showOutMessage(message.getBody());
	    }
	});
    }

}
