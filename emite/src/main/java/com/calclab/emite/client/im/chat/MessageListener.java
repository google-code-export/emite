package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;

public interface MessageListener {
	void onReceived(Message message);
}
