package com.calclab.emite.widgets.client.habla;

import java.util.HashMap;

import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.listener.Listener;

public class ConversationsController {
    private final ChatManager manager;
    private final HashMap<String, ChatWidget> chats;
    private final Provider<ChatWidget> chatWidgetFactory;

    public ConversationsController(final ChatManager manager, final Provider<ChatWidget> chatWidgetFactory) {
	this.manager = manager;
	this.chatWidgetFactory = chatWidgetFactory;
	this.chats = new HashMap<String, ChatWidget>();
    }

    public void setWidget(final ConversationsWidget widget) {
	manager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		ChatWidget chatWidget = chats.get(conversation.getID());
		if (chatWidget == null) {
		    chatWidget = chatWidgetFactory.get();
		    chatWidget.getController().setChat(conversation);
		    chats.put(conversation.getID(), chatWidget);
		    widget.add(conversation.getOtherURI().toString(), chatWidget);
		}
	    }
	});
    }

}
