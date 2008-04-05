package com.calclab.examplechat.client.chatuiplugin.users;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;

public class GroupChatUser extends AbstractChatUser {

	public static class GroupChatUserType {
		private GroupChatUserType() {
		}
	}

	public static final GroupChatUserType MODERADOR = new GroupChatUserType();
	public static final GroupChatUserType NONE = new GroupChatUserType();
	public static final GroupChatUserType PARTICIPANT = new GroupChatUserType();

	public static final GroupChatUserType VISITOR = new GroupChatUserType();

	private GroupChatUserType type;

	public GroupChatUser(final XmppURI jid, final String alias, final String color,
			final GroupChatUserType groupChatUserType) {
		super("images/person-def.gif", jid, alias, color);
		this.type = groupChatUserType;
	}

	public GroupChatUserType getUserType() {
		return type;
	}

	public void setUserType(final GroupChatUserType groupChatUserType) {
		this.type = groupChatUserType;
	}
}
