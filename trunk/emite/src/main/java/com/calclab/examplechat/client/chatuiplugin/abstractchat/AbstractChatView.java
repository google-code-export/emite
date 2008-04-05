package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

public interface AbstractChatView extends View {

    void addMessage(String userAlias, String color, String message);

    void addInfoMessage(String message);

    void addDelimiter(String datetime);

    void scrollDown();

    void setChatTitle(String name);

    int getScrollPos();

    void restoreScrollPos(int position);

}
