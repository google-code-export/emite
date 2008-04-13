package com.calclab.examplechat.client.chatuiplugin.chat;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;

public class ChatUIPresenter implements ChatUI {

    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };
    private ChatUIView view;
    private String savedInput;

    private final ChatUIListener listener;

    private int oldColor;

    private final HashMap<String, String> userColors;
    private boolean closeConfirmed;

    public ChatUIPresenter(final ChatUIListener listener) {
        this.listener = listener;
        userColors = new HashMap<String, String>();
    }

    public void addDelimiter(final String date) {
        view.addDelimiter(date);
    }

    public void addInfoMessage(final String message) {
        view.addInfoMessage(message);
    }

    public void addMesage(final String userAlias, final String message) {
        view.addMessage(userAlias, getColor(userAlias), message);
        listener.onMessageAdded(this);
    }

    public void clearSavedInput() {
        saveInput(null);
    }

    public void close() {
        view.destroy();
    }

    public boolean getCloseConfirmed() {
        return closeConfirmed;
    }

    public String getColor(final String userAlias) {
        String color = userColors.get(userAlias);
        if (color == null) {
            color = getNextColor();
            setUserColor(userAlias, color);
        }
        return color;
    }

    public String getSavedInput() {
        return savedInput;
    }

    public View getView() {
        return view;
    }

    public void init(final ChatUIView view) {
        this.view = view;
    }

    public void onActivated() {
        listener.onActivate(this);
    }

    public void onCloseClick() {
        listener.onCloseClick(this);
    }

    public void onCurrentUserSend(final String message) {
        listener.onCurrentUserSend(message);
    }

    public void onDeactivated() {
        listener.onDeactivate(this);
    }

    public void saveInput(final String inputText) {
        savedInput = inputText;
    }

    public void setChatTitle(final String chatTitle) {
        view.setChatTitle(chatTitle);
    }

    public void setCloseConfirmed(final boolean confirmed) {
        this.closeConfirmed = confirmed;
    }

    public void setUserColor(final String userAlias, final String color) {
        userColors.put(userAlias, color);
    }

    private String getNextColor() {
        final String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }
}
