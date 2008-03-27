package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.View;

public class AbstractChatPresenter implements AbstractChat {

    protected static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };

    protected AbstractChatView view;
    protected String input;
    protected final AbstractChatUser sessionUser;
    protected boolean closeConfirmed;
    protected int chatType;
    private String chatTitle;

    private int scrollPos;

    public AbstractChatPresenter(final AbstractChatUser sessionUser, final int chatType) {
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
        view.showInfoMessage(message);
    }

    public void addDelimiter(final String datetime) {
        view.showDelimiter(datetime);
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
        saveScrollPos();
    }

    protected void saveScrollPos() {
        scrollPos = view.getScrollPos();
    }

    public String getSavedInput() {
        return input;
    }

    public void doClose() {
        // handler.logout();
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
        view.restoreScrollPos(scrollPos);
        // view.scrollDown();
    }

    public int getType() {
        return chatType;
    }

}