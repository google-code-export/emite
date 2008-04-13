package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.View;

public interface ChatUI {

    void addDelimiter(String date);

    void addInfoMessage(String message);

    void addMesage(String userAlias, String message);

    void clearSavedInput();

    void destroy();

    boolean getCloseConfirmed();

    String getColor(String userAlias);

    String getSavedInput();

    View getView();

    void onCloseCloseConfirmed();

    void onCurrentUserSend(String message);

    void saveInput(String inputText);

    void setChatTitle(String chatTitle);

    void setCloseConfirmed(boolean confirmed);

    void setUserColor(String userAlias, String color);

}