package com.calclab.examplechat.client.chatuiplugin.groupchat;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public interface GroupChat extends AbstractChat {

    void addMessage(String userAlias, String body);

    void addUser(GroupChatUser user);

    String getSubject();

    GroupChatUserListView getUsersListView();

    GroupChatUserType getSessionUserType();

    void removeUser(String alias);

    void setSessionUserType(GroupChatUserType groupChatUserType);

    void setSubject(String subject);

}
