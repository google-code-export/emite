package com.calclab.examplechat.client.chatuiplugin.chat;

public interface ChatUIListener {

    void onMessageAdded(ChatUI chatUI);

    void onCurrentUserSend(String message);

    void onActivate(ChatUI chatUI);

    void onDeactivate(ChatUI chatUI);

    void onCloseConfirmed(ChatUI chatUI);

}
