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

import com.calclab.emite.client.extra.chatstate.ChatState;
import com.calclab.emite.client.extra.chatstate.ChatStateListener;
import com.calclab.emite.client.extra.chatstate.ChatState.Type;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.roster.ChatIconDescriptor;

public class ChatUIPresenter implements ChatUI {

    // FIXME: this in Chat or new ChatState module, here only a listener

    private static final int CANCEL_TIMER = -1;
    private static final int MILLISECONS_TO_PAUSE = 5000;
    private static final int MILLISECONS_TO_INACTIVE = 30000;

    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };
    public ChatState chatState;
    private ChatUIView view;
    private String savedInput;
    private final ChatNotification savedChatNotification;
    private final ChatUIListener listener;
    private int oldColor;
    private final HashMap<String, String> userColors;
    private final String chatTitle;
    private final XmppURI otherURI;
    // isActive is maintained because Rooms have no ChatState support currently
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
        savedChatNotification = new ChatNotification();
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

    public ChatNotification getSavedChatNotification() {
        return savedChatNotification;
    }

    public String getSavedInput() {
        return savedInput;
    }

    public View getView() {
        return view;
    }

    public void highLightChatTitle() {
        view.setChatTitle(chatTitle, otherURI.toString(), highIcon);
        alreadyHightlighted = true;
        listener.onHighLight(this);
    }

    public void init(final ChatUIView view) {
        this.view = view;
        isActive = true;
        unHighLightChatTitle();
    }

    public void onClose() {
        unHightAndActive();
        setOwnState(ChatState.Type.gone, CANCEL_TIMER);
        listener.onClose(this);
    }

    public void onComposing() {
        setOwnState(ChatState.Type.composing, MILLISECONS_TO_PAUSE);
    }

    public void onCurrentUserSend(final String message) {
        listener.onCurrentUserSend(message);
    }

    public void onInputFocus() {
        unHightAndActive();
        setOwnState(ChatState.Type.active, MILLISECONS_TO_INACTIVE);
    }

    public void onInputUnFocus() {
        isActive = false;
        setOwnState(ChatState.Type.inactive, CANCEL_TIMER);
    }

    public void onTime() {
        switch (chatState.getOwnState()) {
        case composing:
            setOwnState(ChatState.Type.pause, MILLISECONS_TO_INACTIVE);
            break;
        case active:
            setOwnState(ChatState.Type.inactive, CANCEL_TIMER);
            break;
        case pause:
            setOwnState(ChatState.Type.inactive, CANCEL_TIMER);
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

    public void setChatState(final ChatState chatState) {
        this.chatState = chatState;
        chatState.addOtherStateListener(new ChatStateListener() {
            String alias = otherURI.getNode() != null ? otherURI.getNode() : otherURI.getHost();

            public void onActive() {
                savedChatNotification.setNotification("");
                clearMessageEventInfo();
            }

            public void onComposing() {
                // i18n
                savedChatNotification.setNotification(alias + " is writing");
                savedChatNotification.setStyle("e-notif-composing");
                showMessageEventInfo();
            }

            public void onGone() {
                savedChatNotification.setNotification(alias + " finished the conversation");
                savedChatNotification.setStyle("e-notif-gone");
                showMessageEventInfo();
            }

            public void onInactive() {
                // FIXME: this kind of messages only for tests ...
                savedChatNotification.setNotification(alias + " is inactive");
                savedChatNotification.setStyle("e-notif-inactive");
                showMessageEventInfo();
            }

            public void onPause() {
                savedChatNotification.setNotification(alias + " stop to write");
                savedChatNotification.setStyle("e-notif-pause");
                showMessageEventInfo();
            }

        });
        timer = new ChatStateTimer(this);
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

    private void clearMessageEventInfo() {
        listener.onChatNotificationClear(this);
    }

    private String getNextColor() {
        final String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }

    private void setOwnState(final Type type, final int time) {
        // We don't support chatState in rooms, then we let chatState = null in
        // rooms
        if (chatState != null && chatState.getNegotiationStatus().equals(ChatState.NegotiationStatus.accepted)) {
            if (time > 0) {
                timer.schedule(time);
            } else {
                timer.cancel();
            }
            chatState.setOwnState(type);
        }
    }

    private void showMessageEventInfo() {
        listener.onNewChatNotification(this, savedChatNotification);
    }

    private void unHightAndActive() {
        if (alreadyHightlighted) {
            unHighLightChatTitle();
        }
        isActive = true;
    }
}
