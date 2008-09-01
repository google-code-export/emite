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

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public interface ChatUI {

    public void onActivate(final Slot<ChatUI> slot);

    public void onChatNotificationClear(final Slot<ChatUI> slot);

    public void onClose(final Slot<ChatUI> slot);

    public void onCurrentUserSend(final Slot<String> slot);

    public void onDeactivate(final Slot<ChatUI> slot);

    public void onHighLight(final Slot<ChatUI> slot);

    public void onNewChatNotification(final Slot<ChatNotification> slot);

    public void onUnHighLight(final Slot<ChatUI> slot);

    public void onUserDrop(final Slot<XmppURI> slot);

    void addDelimiter(String date);

    void addEventListener(ChatUIEventListener listener);

    void addInfoMessage(String message);

    void addMessage(XmppURI fromURI, String body);

    void clearMessageEventInfo();

    void clearSavedChatNotification();

    void clearSavedInput();

    void destroy();

    String getChatTitle();

    String getColor(String userAlias);

    String getOtherAlias();

    ChatNotification getSavedChatNotification();

    String getSavedInput();

    View getView();

    void highLightChatTitle();

    boolean isDocked();

    void onClose();

    void onComposing();

    void onCurrentUserSend(String message);

    void onInputFocus();

    void onInputUnFocus();

    void onUserDrop(XmppURI userURI);

    void saveInput(String inputText);

    void setCurrentUserColor(String color);

    void setDocked(boolean docked);

    void setSavedChatNotification(ChatNotification savedChatNotification);

    void showMessageEventInfo();

    void unHighLightChatTitle();

}
