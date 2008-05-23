package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.Message;

/**
 * Allow to modify a message before send or inform the receptors
 * 
 */
public interface MessageInterceptor {
    void onBeforeReceive(Message message);

    void onBeforeSend(Message message);
}
