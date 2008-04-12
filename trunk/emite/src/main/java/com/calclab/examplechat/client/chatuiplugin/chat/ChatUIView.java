package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.View;

public interface ChatUIView extends View {

    void addDelimiter(String datetime);

    void addMessage(String userAlias, String message);

    void addInfoMessage(String message);

    void setChatTitle(String name);

    void setUserColor(String userAlias, String color);

    void destroy();
}
