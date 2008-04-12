package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.View;

public interface ChatUIView extends View {

    void addDelimiter(String datetime);

    void addInfoMessage(String message);

    void addMessage(String userAlias, String color, String message);

    void destroy();

    void setChatTitle(String name);
}
