package com.calclab.emite.j2se.swing.chat;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class ChatControl {

    public ChatControl(final ChatManager manager, final Conversation conversation, final ChatPanel chatPanel) {
	chatPanel.onClose(new Listener0() {
	    public void onEvent() {
		manager.close(conversation);
	    }
	});
	chatPanel.onSend(new Listener<String>() {
	    public void onEvent(final String text) {
		conversation.send(new Message(text));
		chatPanel.clearMessage();
	    }
	});

	conversation.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
	    }
	});
	conversation.onMessageSent(new Listener<Message>() {
	    public void onEvent(final Message message) {
		chatPanel.showOutMessage(message.getBody());
	    }
	});
    }

}
