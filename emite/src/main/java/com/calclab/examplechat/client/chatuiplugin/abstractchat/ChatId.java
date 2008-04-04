/**
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
