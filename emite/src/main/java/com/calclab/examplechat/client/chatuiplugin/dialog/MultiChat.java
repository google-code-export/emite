package com.calclab.examplechat.client.chatuiplugin.dialog;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public interface MultiChat {

    public void closeAllChats(final boolean withConfirmation);

    public void setStatus(int status);

    public void show();

    GroupChat createGroupChat(Chat chat, String userAlias, GroupChatUserType groupChatUserType);

    PairChat createPairChat(Chat chat);

    void groupChatSubjectChanged(final Chat groupChat, String newSubject);

    void messageReceived(final ChatMessageParam param);

    public void addUsetToGroupChat(String groupChatId, GroupChatUser groupChatUser);

    public void addPresenceBuddy(PairChatUser param);

    public void activateChat(Chat chat);

    public void destroy();

}
