package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.View;

public class ChatUIPresenter implements ChatUI {

    private ChatUIView view;
    private String savedInput;
    private final ChatUIListener listener;

    public ChatUIPresenter(final ChatUIListener listener) {
        this.listener = listener;
    }

    public void init(final ChatUIView view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void addMesage(final String userAlias, final String message) {
        view.addMessage(userAlias, message);
        listener.onMessageAdded(this);
    }

    public void setUserColor(final String userAlias, final String color) {
        view.setUserColor(userAlias, color);
    }

    public void addDelimiter(final String date) {
        view.addDelimiter(date);
    }

    public void addInfoMessage(final String message) {
        view.addInfoMessage(message);
    }

    public void clearSavedInput() {
        saveInput(null);
    }

    public String getSavedInput() {
        return savedInput;
    }

    public void saveInput(final String inputText) {
        savedInput = inputText;
    }

    public void setChatTitle(final String chatTitle) {
        view.setChatTitle(chatTitle);
    }

    public void onActivated() {
        listener.onActivate(this);
    }

    public void onDeactivated() {
        listener.onDeactivate(this);
    }

    public void onCloseClick() {
        listener.onCloseClick(this);
    }

    public void close() {
        view.destroy();
    }

    public void onCurrentUserSend(final String message) {
        listener.onCurrentUserSend(message);
    }
}
