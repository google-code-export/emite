package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;

public class MessageInterceptorCollection extends ArrayList<MessageInterceptor> implements MessageInterceptor {

    private static final long serialVersionUID = 1L;

    public void onBeforeReceive(final Message message) {
	for (final MessageInterceptor formatter : this) {
	    formatter.onBeforeReceive(message);
	}
    }

    public void onBeforeSend(final Message message) {
	for (final MessageInterceptor formatter : this) {
	    formatter.onBeforeSend(message);
	}
    }
}
