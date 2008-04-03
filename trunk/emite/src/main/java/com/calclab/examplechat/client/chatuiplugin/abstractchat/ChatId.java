package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatId {

	public final XmppURI jid;
	private final String thread;

	public ChatId(final XmppURI jid) {
		this(jid, "");
	}

	public ChatId(final XmppURI jid, final String thread) {
		this.jid = jid;
		this.thread = thread;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		final ChatId other = (ChatId) obj;
		if (jid == null) {
			if (other.jid != null) {
				return false;
			}
		} else if (!jid.equals(other.jid)) {
			return false;
		}
		if (thread == null) {
			if (other.thread != null) {
				return false;
			}
		} else if (!thread.equals(other.thread)) {
			return false;
		}
		return true;
	}

	public XmppURI getJid() {
		return jid;
	}

	public String getThread() {
		return thread;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (jid == null ? 0 : jid.hashCode());
		result = prime * result + (thread == null ? 0 : thread.hashCode());
		return result;
	}

}
