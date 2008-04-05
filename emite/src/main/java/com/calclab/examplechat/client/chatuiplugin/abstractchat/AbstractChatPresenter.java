package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.chat.Chat;

public class AbstractChatPresenter implements AbstractChat {

    protected static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
	    "purple", "fuchsia", "maroon", "red" };

    protected int chatType;
    protected boolean closeConfirmed;
    protected String input;
    protected final AbstractChatUser sessionUser;
    protected AbstractChatView view;
    private final Chat chat;
    private String chatTitle;

    public AbstractChatPresenter(final Chat chat, final AbstractChatUser sessionUser, final int chatType) {
	this.chat = chat;
	this.sessionUser = sessionUser;
	this.chatType = chatType;
    }

    public void activate() {
	// Nothing currently
    }

    public void addDelimiter(final String datetime) {
	view.addDelimiter(datetime);
    }

    public void addInfoMessage(final String message) {
	view.addInfoMessage(message);
    }

    public void clearSavedInput() {
	saveInput(null);
    }

    public void doClose() {
    }

    public Chat getChat() {
	return chat;
    }

    public String getChatTitle() {
	return chatTitle;
    }

    public String getSavedInput() {
	return input;
    }

    public String getSessionUserAlias() {
	return sessionUser.getAlias();
    }

    public int getType() {
	return chatType;
    }

    public View getView() {
	return view;
    }

    public boolean isCloseConfirmed() {
	return closeConfirmed;
    }

    public void onCloseConfirmed() {
	closeConfirmed = true;
    }

    public void onCloseNotConfirmed() {
	closeConfirmed = false;
    }

    public void saveInput(final String inputText) {
	input = inputText;
    }

    public void saveOtherProperties() {
	// Nothing currently
    }

    public void setChatTitle(final String chatTitle) {
	this.chatTitle = chatTitle;
	view.setChatTitle(chatTitle);
    }

    public void setSessionUserColor(final String color) {
	sessionUser.setColor(color);
    }

}