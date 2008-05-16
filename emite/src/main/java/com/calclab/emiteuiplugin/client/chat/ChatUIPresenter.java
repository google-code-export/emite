/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuiplugin.client.chat;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.roster.ChatIconDescriptor;

public class ChatUIPresenter implements ChatUI {

    // FIXME: this in Chat or new ChatState module, here only a listener
    public static enum ChatState {
        active, composing, pause, inactive, gone
    }
    private static final int MILLISECONS_TO_PAUSE = 5000;
    private static final int MILLISECONS_TO_INACTIVE = 30000;
    private static final int MILLISECONS_TO_GONE = 120000;

    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };
    public ChatState chatState;
    private ChatUIView view;
    private String savedInput;
    private String savedMessageEventInfo;
    private final ChatUIListener listener;
    private int oldColor;
    private final HashMap<String, String> userColors;
    private final String chatTitle;
    private final XmppURI otherURI;
    private boolean isActive;
    private boolean alreadyHightlighted;
    private final ChatIconDescriptor unhighIcon;
    private final ChatIconDescriptor highIcon;
    private ChatStateTimer timer;

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
            final ChatIconDescriptor unhighIcon, final ChatIconDescriptor highIcon, final ChatUIListener listener) {
        this.otherURI = otherURI;
        this.unhighIcon = unhighIcon;
        this.highIcon = highIcon;
        this.listener = listener;
        // Messages from server has no node
        final String node = otherURI.getNode();
        this.chatTitle = node != null ? node : otherURI.getHost();
        userColors = new HashMap<String, String>();
        userColors.put(currentUserAlias, currentUserColor);
        savedMessageEventInfo = "";
        chatState = ChatState.active;
    }

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
            final ChatUIListener listener) {
        // Def Constructor for chats
        this(otherURI, currentUserAlias, currentUserColor, ChatIconDescriptor.chatsmall,
                ChatIconDescriptor.chatnewmessagesmall, listener);
    }

    public void addDelimiter(final String date) {
        view.addDelimiter(date);
    }

    public void addInfoMessage(final String message) {
        checkIfHighlightNeeded();
        view.addInfoMessage(message);
    }

    public void addMessage(final String userAlias, final String message) {
        checkIfHighlightNeeded();
        view.addMessage(userAlias, getColor(userAlias), message);
        listener.onMessageAdded(this);
    }

    public void clearSavedInput() {
        saveInput(null);
    }

    public void destroy() {
        view.destroy();
    }

    public String getChatTitle() {
        return chatTitle;
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

    public String getSavedMessageEventInfo() {
        return savedMessageEventInfo;
    }

    public View getView() {
        return view;
    }

    public void highLightChatTitle() {
        view.setChatTitle(chatTitle, otherURI.toString(), highIcon);
        alreadyHightlighted = true;
        listener.onHighLight(this);
    }

    public void init(final ChatUIView view, final ChatStateTimer chatStateTimer) {
        this.view = view;
        timer = chatStateTimer;
        isActive = true;
        unHighLightChatTitle();
    }

    public void onClose() {
        listener.onClose(this);
    }

    public void onComposing() {
        if (!chatState.equals(ChatState.composing)) {
            Log.info("Chat new state: composing");
        }
        timer.schedule(MILLISECONS_TO_PAUSE);
        // FIXME: This in lib...
        chatState = ChatState.composing;
    }

    public void onCurrentUserSend(final String message) {
        listener.onCurrentUserSend(message);
    }

    public void onInputFocus() {
        unHightAndActive();
        timer.cancel();
        Log.info("Chat new state: active");
        // FIXME: this in lib
        chatState = ChatState.active;
    }

    public void onInputUnFocus() {
        isActive = false;
        timer.schedule(MILLISECONS_TO_INACTIVE);
        Log.info("Chat new state: pause");
        // FIXME: this in lib
        chatState = ChatState.pause;
    }

    public void onTime() {
        switch (chatState) {
        case composing:
        case active:
            Log.info("Chat new state: pause");
            timer.schedule(MILLISECONS_TO_INACTIVE);
            // FIXME: this in lib:
            chatState = ChatState.pause;
            break;
        case pause:
            Log.info("Chat new state: inactive");
            timer.schedule(MILLISECONS_TO_GONE);
            // FIXME: this in lib:
            chatState = ChatState.inactive;
            break;
        case inactive:
            Log.info("Chat new state: gone");
            timer.cancel();
            // FIXME: this in lib:
            chatState = ChatState.gone;
            break;
        default:
            timer.cancel();
            break;
        }
    }

    public void onUserDrop(final XmppURI userURI) {
        listener.onUserDrop(this, userURI);
    }

    public void saveInput(final String inputText) {
        savedInput = inputText;
    }

    public void setSavedMessageEventInfo(final String savedMessageEventInfo) {
        this.savedMessageEventInfo = savedMessageEventInfo;
    }

    public void setUserColor(final String userAlias, final String color) {
        userColors.put(userAlias, color);
    }

    public void unHighLightChatTitle() {
        view.setChatTitle(chatTitle, otherURI.toString(), unhighIcon);
        alreadyHightlighted = false;
        listener.onUnHighLight(this);
    }

    protected void onActivated() {
        unHightAndActive();
        listener.onActivate(this);
    }

    protected void onDeactivated() {
        isActive = false;
        listener.onDeactivate(this);
    }

    private void checkIfHighlightNeeded() {
        if (!isActive && !alreadyHightlighted) {
            highLightChatTitle();
        }
    }

    private String getNextColor() {
        final String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }

    private void unHightAndActive() {
        if (alreadyHightlighted) {
            unHighLightChatTitle();
        }
        isActive = true;
    }
}
