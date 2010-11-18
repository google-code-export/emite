/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.chat;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class ChatUIPresenter implements ChatUI {
    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
	    "purple", "fuchsia", "maroon", "red" };
    final Event<String> onCurrentUserSend;
    final Event<ChatNotification> onNewChatNotification;
    final Event<XmppURI> onUserDrop;
    final Event<ChatUI> onActivate;
    final Event<ChatUI> onDeactivate;
    final Event<ChatUI> onUnHighLight;
    final Event<ChatUI> onChatNotificationClear;
    final Event<ChatUI> onClose;
    final Event<ChatUI> onHighLight;
    private ChatUIView view;
    private String savedInput;
    private ChatNotification savedChatNotification;
    private int oldColor;
    private final HashMap<String, String> userColors;
    private final String chatTitle;
    private final XmppURI otherURI;
    // isActive is maintained because Rooms have no ChatState support currently
    private boolean isActive;
    private boolean alreadyHightlighted;
    private final String unhighIcon;
    private final String highIcon;
    private boolean docked;
    private ChatUIEventListenerCollection eventListenerCollection;
    private final String currentUserAlias;
    private String otherAlias;

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor) {
	// Def Constructor for chats
	this(otherURI, currentUserAlias, currentUserColor, "chat-icon", "chat-h-icon");
    }

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
	    final String unhighIcon, final String highIcon) {
	this.otherURI = otherURI;
	this.currentUserAlias = currentUserAlias;
	this.unhighIcon = unhighIcon;
	this.highIcon = highIcon;
	this.chatTitle = getOtherAlias();
	userColors = new HashMap<String, String>();
	userColors.put(currentUserAlias, currentUserColor);
	clearSavedChatNotification();
	docked = false;
	this.onDeactivate = new Event<ChatUI>("onDeactivate");
	this.onActivate = new Event<ChatUI>("onActivate");
	this.onChatNotificationClear = new Event<ChatUI>("onChatNotificationClear");
	this.onClose = new Event<ChatUI>("onClose");
	this.onCurrentUserSend = new Event<String>("onCurrentUserSend");
	this.onHighLight = new Event<ChatUI>("onHighLight");
	this.onNewChatNotification = new Event<ChatNotification>("onNewChatNotification");
	this.onUnHighLight = new Event<ChatUI>("onUnHighLight");
	this.onUserDrop = new Event<XmppURI>("onUserDrop");
    }

    public void addDelimiter(final String date) {
	view.addDelimiter(date);
    }

    public void addEventListener(final ChatUIEventListener listener) {
	if (eventListenerCollection == null) {
	    eventListenerCollection = new ChatUIEventListenerCollection();
	}
	eventListenerCollection.add(listener);
    }

    public void addInfoMessage(final String message) {
	checkIfHighlightNeeded();
	view.addInfoMessage(message);
    }

    public void addMessage(final XmppURI fromURI, final String body) {
	final String node = fromURI.getNode() != null ? fromURI.getNode() : fromURI.toString();
	final String alias = fromURI.equalsNoResource(otherURI) ? getOtherAlias() : node;
	addMessage(alias, body);
    }

    public void clearMessageEventInfo() {
	onChatNotificationClear.fire(this);
    }

    public void clearSavedChatNotification() {
	savedChatNotification = new ChatNotification();
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

    public String getOtherAlias() {
	// Messages from server has no node
	if (otherAlias == null) {
	    otherAlias = otherURI.getNode() != null ? otherURI.getNode() : otherURI.getHost();
	    final boolean likeMyAlias = currentUserAlias.equals(otherAlias);
	    otherAlias = likeMyAlias ? otherURI.getJID().toString() : otherAlias;
	}
	return otherAlias;
    }

    public XmppURI getOtherURI() {
	return otherURI;
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
	view.setChatIconCls(highIcon);
	alreadyHightlighted = true;
	onHighLight.fire(this);
    }

    public void init(final ChatUIView view) {
	this.view = view;
	isActive = true;
	view.setChatTitle(chatTitle, otherURI.toString());
	unHighLightChatTitle();
    }

    public boolean isDocked() {
	return docked;
    }

    public void onActivate(final Listener<ChatUI> listener) {
	onActivate.add(listener);
    }

    public void onChatNotificationClear(final Listener<ChatUI> listener) {
	onChatNotificationClear.add(listener);
    }

    public void onClose() {
	unHightAndActive();
	if (eventListenerCollection != null) {
	    eventListenerCollection.onClose();
	}
	onClose.fire(this);
    }

    public void onClose(final Listener<ChatUI> listener) {
	onClose.add(listener);
    }

    public void onComposing() {
	if (eventListenerCollection != null) {
	    eventListenerCollection.onComposing();
	}
    }

    public void onCurrentUserSend(final Listener<String> listener) {
	onCurrentUserSend.add(listener);
    }

    public void onCurrentUserSend(final String message) {
	onCurrentUserSend.fire(message);
    }

    public void onDeactivate(final Listener<ChatUI> listener) {
	onDeactivate.add(listener);
    }

    public void onHighLight(final Listener<ChatUI> listener) {
	onHighLight.add(listener);
    }

    public void onInputFocus() {
	unHightAndActive();
	if (eventListenerCollection != null) {
	    eventListenerCollection.onInputFocus();
	}
    }

    public void onInputUnFocus() {
	isActive = false;
	if (eventListenerCollection != null) {
	    eventListenerCollection.onInputUnFocus();
	}
    }

    public void onNewChatNotification(final Listener<ChatNotification> listener) {
	onNewChatNotification.add(listener);
    }

    public void onUnHighLight(final Listener<ChatUI> listener) {
	onUnHighLight.add(listener);
    }

    public void onUserDrop(final Listener<XmppURI> listener) {
	onUserDrop.add(listener);
    }

    public void onUserDrop(final XmppURI userURI) {
	onUserDrop.fire(userURI);
    }

    public void saveInput(final String inputText) {
	savedInput = inputText;
    }

    public void setChatTitleTextCls(final String textCls) {
	view.setChatTitleTextCls(textCls);
    }

    public void setCurrentUserColor(final String color) {
	setUserColor(currentUserAlias, color);
    }

    public void setDocked(final boolean docked) {
	this.docked = docked;
    }

    public void setSavedChatNotification(final ChatNotification savedChatNotification) {
	this.savedChatNotification = savedChatNotification;
    }

    public void showMessageEventInfo() {
	onNewChatNotification.fire(savedChatNotification);
    }

    public void unHighLightChatTitle() {
	view.setChatIconCls(unhighIcon);
	alreadyHightlighted = false;
	onUnHighLight.fire(this);
    }

    protected void addMessage(final String userAlias, final String message) {
	checkIfHighlightNeeded();
	view.addMessage(userAlias, getColor(userAlias), message);
    }

    protected void onActivated() {
	unHightAndActive();
	onActivate.fire(this);
    }

    protected void onDeactivated() {
	isActive = false;
	onDeactivate.fire(this);
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

    private void setUserColor(final String userAlias, final String color) {
	userColors.put(userAlias, color);
    }

    private void unHightAndActive() {
	if (alreadyHightlighted) {
	    unHighLightChatTitle();
	}
	isActive = true;
    }
}
