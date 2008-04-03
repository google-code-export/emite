package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;

public interface ChatListener {
	public void onMessageReceived(ChatDefault chatDefault, Message message);

	public void onMessageSent(ChatDefault chatDefault, Message message);
}
