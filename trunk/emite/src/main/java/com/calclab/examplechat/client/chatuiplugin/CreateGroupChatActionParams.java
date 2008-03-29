package com.calclab.examplechat.client.chatuiplugin;

import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser.GroupChatUserType;

public class CreateGroupChatActionParams {
    public final String groupChatName;
    public final String userAlias;
    public final GroupChatUserType groupChatUserType;

    public CreateGroupChatActionParams(final String groupChatName, final String userAlias,
            final GroupChatUserType groupChatUserType) {
        this.groupChatName = groupChatName;
        this.userAlias = userAlias;
        this.groupChatUserType = groupChatUserType;
    }

    public String getGroupChatName() {
        return groupChatName;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public GroupChatUserType getGroupChatUserType() {
        return groupChatUserType;
    }

}
