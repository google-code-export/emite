package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.chat.Chat;

public interface AbstractChat {

    final int TYPE_GROUP_CHAT = 2;

    final int TYPE_PAIR_CHAT = 1;

    void activate();

    void addDelimiter(String date);

    void addInfoMessage(String message);

    void clearSavedInput();

    void doClose();

    Chat getChat();

    String getChatTitle();

    String getSavedInput();

    String getSessionUserAlias();

    int getType();

    View getView();

    void saveInput(String inputText);

    void saveOtherProperties();

    void setChatTitle(String chatTitle);

    void setSessionUserColor(String color);

}
