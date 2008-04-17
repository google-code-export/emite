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
package com.calclab.emiteui.client.chatuiplugin.chat;

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

    public ChatUIPresenter(final String currentUserAlias, final String currentUserColor, final ChatUIListener listener) {
        this.listener = listener;
        userColors = new HashMap<String, String>();
        userColors.put(currentUserAlias, currentUserColor);
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

    public void destroy() {
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

    public void onCloseCloseConfirmed() {
        listener.onCloseConfirmed(this);
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
