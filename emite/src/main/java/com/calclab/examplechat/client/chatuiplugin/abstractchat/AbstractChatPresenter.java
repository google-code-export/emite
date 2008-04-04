/**
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