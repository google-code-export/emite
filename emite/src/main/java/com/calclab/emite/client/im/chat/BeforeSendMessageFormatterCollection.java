package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;

public class BeforeSendMessageFormatterCollection extends ArrayList<BeforeSendMessageFormatter> {

    private static final long serialVersionUID = 1L;

    public void fireMessageFormat(final Message message) {
        for (final BeforeSendMessageFormatter formatter : this) {
            formatter.formatBeforeSend(message);
        }
    }
}
