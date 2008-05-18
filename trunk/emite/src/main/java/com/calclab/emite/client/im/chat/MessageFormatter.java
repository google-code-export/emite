package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;

public interface MessageFormatter {
    Message format(Message message);
}
