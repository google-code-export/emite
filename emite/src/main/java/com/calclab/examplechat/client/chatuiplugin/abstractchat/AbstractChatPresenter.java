package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.chat.Chat;

public class AbstractChatPresenter implements AbstractChat {

    protected static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };

    protected AbstractChatView view;
    protected String input;
    protected final AbstractChatUser sessionUser;
    protected boolean closeConfirmed;
    protected int chatType;
    private String chatTitle;
    private final Chat chat;

    public AbstractChatPresenter(final Chat chat, final AbstractChatUser sessionUser, final int chatType) {
        this.chat = chat;
        this.sessionUser = sessionUser;
        this.chatType = chatType;
    }

    public View getView() {
        return view;
    }

    public void setChatTitle(final String chatTitle) {
        this.chatTitle = chatTitle;
        view.setChatTitle(chatTitle);
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void addInfoMessage(final String message) {
        view.addInfoMessage(message);
    }

    public void addDelimiter(final String datetime) {
        view.addDelimiter(datetime);
    }

    public void clearSavedInput() {
        saveInput(null);
    }

    public String getSessionUserAlias() {
        return sessionUser.getAlias();
    }

    public void setSessionUserColor(final String color) {
        sessionUser.setColor(color);
    }

    public void saveInput(final String inputText) {
        input = inputText;
    }

    public void saveOtherProperties() {
        // Nothing currently
    }

    public String getSavedInput() {
        return input;
    }

    public void doClose() {
    }

    public void onCloseConfirmed() {
        closeConfirmed = true;
    }

    public void onCloseNotConfirmed() {
        closeConfirmed = false;
    }

    public boolean isCloseConfirmed() {
        return closeConfirmed;
    }

    public void activate() {
        // Nothing currently
    }

    public int getType() {
        return chatType;
    }

    public Chat getChat() {
        return chat;
    }

}