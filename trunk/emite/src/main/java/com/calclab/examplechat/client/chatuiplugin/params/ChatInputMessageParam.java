package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.xmpp.stanzas.XmppJID;

public class ChatInputMessageParam {
    private final String message;
    private final XmppJID from;
    private final XmppJID to;

    public ChatInputMessageParam(final XmppJID from, final XmppJID to, final String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public XmppJID getFrom() {
        return from;
    }

    public XmppJID getTo() {
        return to;
    }

}
