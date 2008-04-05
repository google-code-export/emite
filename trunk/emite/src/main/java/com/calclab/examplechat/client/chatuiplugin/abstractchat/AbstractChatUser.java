package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class AbstractChatUser {
	private String alias;
	private String color;
	private final String iconUrl;
	private final XmppURI jid;

	public AbstractChatUser(final String iconUrl, final XmppURI jid, final String alias, final String color) {
		this.iconUrl = iconUrl;
		this.jid = jid;
		this.alias = alias;
		this.color = color;
	}

	public String getAlias() {
		return alias;
	}

	public String getColor() {
		return color;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public XmppURI getUri() {
		return jid;
	}

	public void setAlias(final String alias) {
		this.alias = alias;
	}

	public void setColor(final String color) {
		this.color = color;
	}

}
