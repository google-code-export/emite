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
package com.calclab.emiteuimodule.client.chat;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.roster.ChatIconDescriptor;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class ChatUIPresenter implements ChatUI {

    // FIXME: this in Chat or new ChatState module, here only a listener

    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
	    "purple", "fuchsia", "maroon", "red" };
    final Signal<String> onCurrentUserSend;
    final Signal<ChatNotification> onNewChatNotification;
    final Signal<XmppURI> onUserDrop;
    final Signal<ChatUI> onActivate;
    final Signal<ChatUI> onDeactivate;
    final Signal<ChatUI> onUnHighLight;
    final Signal<ChatUI> onChatNotificationClear;
    final Signal<ChatUI> onClose;
    final Signal<ChatUI> onHighLight;
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
    private final ChatIconDescriptor unhighIcon;
    private final ChatIconDescriptor highIcon;
    private boolean docked;
    private ChatUIEventListenerCollection eventListenerCollection;
    private final String currentUserAlias;
    private String otherAlias;

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor) {
	// Def Constructor for chats
	this(otherURI, currentUserAlias, currentUserColor, ChatIconDescriptor.chatsmall,
		ChatIconDescriptor.chatnewmessagesmall);
    }

    public ChatUIPresenter(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
	    final ChatIconDescriptor unhighIcon, final ChatIconDescriptor highIcon) {
	this.otherURI = otherURI;
	this.currentUserAlias = currentUserAlias;
	this.unhighIcon = unhighIcon;
	this.highIcon = highIcon;
	this.chatTitle = getOtherAlias();
	userColors = new HashMap<String, String>();
	userColors.put(currentUserAlias, currentUserColor);
	clearSavedChatNotification();
	docked = false;
	this.onDeactivate = new Signal<ChatUI>("onDeactivate");
	this.onActivate = new Signal<ChatUI>("onActivate");
	this.onChatNotificationClear = new Signal<ChatUI>("onChatNotificationClear");
	this.onClose = new Signal<ChatUI>("onClose");
	this.onCurrentUserSend = new Signal<String>("onCurrentUserSend");
	this.onHighLight = new Signal<ChatUI>("onHighLight");
	this.onNewChatNotification = new Signal<ChatNotification>("onNewChatNotification");
	this.onUnHighLight = new Signal<ChatUI>("onUnHighLight");
	this.onUserDrop = new Signal<XmppURI>("onUserDrop");
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
	final String alias = fromURI.equals(otherURI) ? getOtherAlias() : node;
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
	onHighLight.fire(this);
    }

    public void init(final ChatUIView view) {
	this.view = view;
	isActive = true;
	unHighLightChatTitle();
    }

    public boolean isDocked() {
	return docked;
    }

    public void onActivate(final Slot<ChatUI> slot) {
	onActivate.add(slot);
    }

    public void onChatNotificationClear(final Slot<ChatUI> slot) {
	onChatNotificationClear.add(slot);
    }

    public void onClose() {
	unHightAndActive();
	if (eventListenerCollection != null) {
	    eventListenerCollection.onClose();
	}
	onClose.fire(this);
    }

    public void onClose(final Slot<ChatUI> slot) {
	onClose.add(slot);
    }

    public void onComposing() {
	if (eventListenerCollection != null) {
	    eventListenerCollection.onComposing();
	}
    }

    public void onCurrentUserSend(final Slot<String> slot) {
	onCurrentUserSend.add(slot);
    }

    public void onCurrentUserSend(final String message) {
	onCurrentUserSend.fire(message);
    }

    public void onDeactivate(final Slot<ChatUI> slot) {
	onDeactivate.add(slot);
    }

    public void onHighLight(final Slot<ChatUI> slot) {
	onHighLight.add(slot);
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

    public void onNewChatNotification(final Slot<ChatNotification> slot) {
	onNewChatNotification.add(slot);
    }

    public void onUnHighLight(final Slot<ChatUI> slot) {
	onUnHighLight.add(slot);
    }

    public void onUserDrop(final Slot<XmppURI> slot) {
	onUserDrop.add(slot);
    }

    public void onUserDrop(final XmppURI userURI) {
	onUserDrop.fire(userURI);
    }

    public void saveInput(final String inputText) {
	savedInput = inputText;
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
	view.setChatTitle(chatTitle, otherURI.toString(), unhighIcon);
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
