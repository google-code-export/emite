package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatMessageParam {
	private final XmppURI from;
	private final String message;
	private final XmppURI to;

	public ChatMessageParam(final XmppURI from, final XmppURI to, final String message) {
		this.from = from;
		this.to = to;
		this.message = message;
	}

	public XmppURI getFrom() {
		return from;
	}

	public String getMessage() {
		return message;
	}

	public XmppURI getTo() {
		return to;
	}

}
