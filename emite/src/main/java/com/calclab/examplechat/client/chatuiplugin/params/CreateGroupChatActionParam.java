package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class CreateGroupChatActionParam {
    public final String groupChatName;
    public final String userAlias;
    public final GroupChatUserType groupChatUserType;

    public CreateGroupChatActionParam(final String groupChatName, final String userAlias,
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
