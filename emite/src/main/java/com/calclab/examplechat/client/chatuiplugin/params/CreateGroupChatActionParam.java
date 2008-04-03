package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class CreateGroupChatActionParam {
    public final Chat groupChat;
    public final GroupChatUserType groupChatUserType;
    public final String userAlias;

    public CreateGroupChatActionParam(final Chat groupChat, final String userAlias,
	    final GroupChatUserType groupChatUserType) {
	this.groupChat = groupChat;
	this.userAlias = userAlias;
	this.groupChatUserType = groupChatUserType;
    }

    public Chat getGroupChat() {
	return groupChat;
    }

    public GroupChatUserType getGroupChatUserType() {
	return groupChatUserType;
    }

    public String getUserAlias() {
	return userAlias;
    }

}
