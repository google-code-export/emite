package com.calclab.emite.client.x.im.chat;

import com.calclab.emite.client.core.packet.stanza.Message;

public interface MessageListener {
	void onReceived(Message message);
}
