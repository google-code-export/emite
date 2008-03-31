package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;

public class GroupChatUserAddActionParam {

    private final GroupChatUser groupChatUser;
    private final String groupChatId;

    public GroupChatUserAddActionParam(final String groupChatId, final GroupChatUser groupChatUser) {
        this.groupChatId = groupChatId;
        this.groupChatUser = groupChatUser;
    }

    public String getGroupChatId() {
        return groupChatId;
    }

    public GroupChatUser getGroupChatUser() {
        return groupChatUser;
    }

}
