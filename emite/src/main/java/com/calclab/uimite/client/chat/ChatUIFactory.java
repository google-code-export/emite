package com.calclab.uimite.client.chat;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.uimite.client.chat.ChatView.ChatViewListener;
import com.calclab.uimite.client.chat.StatusView.StatusViewListener;

/**
 * externalizamos esto para hacer más fácil las pruebas (luego se sustituye por
 * mock+stub y listo!)
 * 
 * @author dani
 * 
 */
public class ChatUIFactory {
    public ChatView createChatView(final Chat chat, final ChatViewListener listener) {
	return new ChatView(chat, listener);
    }

    public StatusView createStatusView(final StatusViewListener listener) {
	return new StatusView(listener);
    }

}
