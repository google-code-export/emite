package com.calclab.examplechat.client.chatuiplugin.dialog;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;

public interface MultiChatView {

    // FIXME: emite constants?
    public static final int STATUS_ONLINE = 0;
    public static final int STATUS_OFFLINE = 1;
    public static final int STATUS_BUSY = 2;
    public static final int STATUS_INVISIBLE = 3;
    public static final int STATUS_XA = 4;
    public static final int STATUS_AWAY = 5;
    public static final int STATUS_MESSAGE = 6;

    public static final String DEF_USER_COLOR = "blue";

    void addChat(AbstractChat chat);

    void addGroupChatUsersPanel(GroupChatUserListView view);

    void clearInputText();

    void destroy();

    String getInputText();

    void highlightChat(AbstractChat chat);

    void removePresenceBuddy(PairChatUser user);

    void setInputEditable(boolean editable);

    void setInputText(String savedInput);

    void setSendEnabled(boolean enabled);

    void setStatus(int statusOnline);

    void setSubject(String subject);

    void setSubjectEditable(boolean editable);

    void show();

    void showUserList(GroupChatUserListView usersListView);

    void clearSubject();

    void setGroupChatUsersPanelVisible(boolean visible);

    void setInviteToGroupChatButtonEnabled(boolean enabled);

    void closeAllChats();

    void setCloseAllOptionEnabled(boolean enabled);

    void confirmCloseAll();

    void activateChat(AbstractChat chat);

    void addPresenceBuddy(PairChatUser user);

    void setEmoticonButton(boolean enabled);

}
