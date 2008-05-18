package com.calclab.emite.client.im.chat;

import java.util.ArrayList;
import java.util.Iterator;

import com.calclab.emite.client.xmpp.stanzas.Message;

public class MessageFormatterCollection extends ArrayList<MessageFormatter> {

    private static final long serialVersionUID = 1L;

    public void fireMessageFormat(Message message) {
        for (Iterator<MessageFormatter> it = iterator(); it.hasNext();) {
            MessageFormatter formatter = it.next();
            message = formatter.format(message);
        }
    }
}
