package com.calclab.examplechat.client.chatuiplugin.users;


public interface GroupChatUserList {

    public GroupChatUserListView getView();

    void add(GroupChatUser user);

    void remove(GroupChatUser user);

    public GroupChatUser get(String userAlias);

}
