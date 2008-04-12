package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.View;

public interface ChatUI {

    void addDelimiter(String date);

    void addInfoMessage(String message);

    void clearSavedInput();

    String getSavedInput();

    View getView();

    void saveInput(String inputText);

    void setChatTitle(String chatTitle);

    void addMesage(String userAlias, String message);

    void setUserColor(String userAlias, String color);

    void onCurrentUserSend(String message);

    void onCloseClick();

    void close();

}