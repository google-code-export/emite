package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;

public interface ChatListener {
	public void onMessageReceived(Chat chat, Message message);

	public void onMessageSent(Chat chat, Message message);
}
