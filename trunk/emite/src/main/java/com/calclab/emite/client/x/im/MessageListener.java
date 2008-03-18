package com.calclab.emite.client.x.im;

import com.calclab.emite.client.packet.stanza.Message;

public interface MessageListener {
	void onReceived(Message message);
}
