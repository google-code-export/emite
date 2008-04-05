package com.calclab.examplechat.client.chatuiplugin.abstractchat;

public interface AbstractChatListener {

    void onMessageAdded(AbstractChat chat);

    void onActivate(AbstractChat chat);

    void onDeactivate(AbstractChat chat);

}
