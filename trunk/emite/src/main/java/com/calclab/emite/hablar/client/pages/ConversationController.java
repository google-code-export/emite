package com.calclab.emite.hablar.client.pages;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.suco.client.events.Listener;

public class ConversationController {

    public ConversationController(final Conversation conversation, final ConversationPage conversationPage) {
	conversation.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		conversationPage.writeln(message.getFrom().getNode(), message.getBody());
	    }
	});
	conversationPage.onSendMessage(new Listener<String>() {
	    public void onEvent(final String body) {
		conversation.send(new Message(body));
		conversationPage.writeln("me", body);
	    }
	});
    }

}
