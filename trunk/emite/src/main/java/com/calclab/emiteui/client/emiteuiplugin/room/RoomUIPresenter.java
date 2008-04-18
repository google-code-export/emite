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
package com.calclab.emiteui.client.emiteuiplugin.room;

import java.util.Collection;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;

import com.calclab.emite.client.extra.muc.Occupant;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteui.client.emiteuiplugin.AbstractPresenter;
import com.calclab.emiteui.client.emiteuiplugin.EmiteUIPlugin;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIListener;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIPresenter;
import com.calclab.emiteui.client.emiteuiplugin.users.RoomUserUI;
import com.calclab.emiteui.client.emiteuiplugin.users.UserGridMenuItem;
import com.calclab.emiteui.client.emiteuiplugin.users.UserGridMenuItemList;
import com.calclab.emiteui.client.emiteuiplugin.users.RoomUserUI.RoomUserType;
import com.calclab.emiteui.client.emiteuiplugin.utils.XmppJID;

public class RoomUIPresenter extends ChatUIPresenter implements RoomUI, AbstractPresenter {

    private RoomUIView view;

    private String subject;

    private RoomUserListUIPanel roomUserListUI;

    public RoomUIPresenter(final String currentUserAlias, final String currentUserColor, final ChatUIListener listener) {
        super(currentUserAlias, currentUserColor, listener);
    }

    public void doAction(final String eventName, final Object param) {
        DefaultDispatcher.getInstance().fire(eventName, param);
    }

    public String getSubject() {
        return subject;
    }

    public View getUserListView() {
        return roomUserListUI.getView();
    }

    public View getView() {
        return view;
    }

    public void init(final RoomUIView view, final RoomUserListUIPanel roomUserListUI) {
        super.init(view);
        this.view = view;
        this.roomUserListUI = roomUserListUI;
    }

    public void setSubject(final String newSubject) {
        subject = newSubject;
    }

    public void setUsers(final Collection<Occupant> users) {
        roomUserListUI.removeAllUsers();
        for (Iterator<Occupant> iterator = users.iterator(); iterator.hasNext();) {
            Occupant occupant = iterator.next();
            XmppURI userUri = occupant.getUri();
            // FIXME real user alias
            String userAlias = userUri.getNode();
            RoomUserUI roomUserUI = new RoomUserUI(new XmppJID(userUri), userAlias, super.getColor(userAlias),
                    RoomUserType.participant);
            roomUserListUI.addUser(roomUserUI, createUserMenu(roomUserUI));
        }
    }

    private UserGridMenuItem<Object> createNoActionsMenuItem() {
        return new UserGridMenuItem<Object>("", "", EmiteUIPlugin.NO_ACTION, null);
    }

    private UserGridMenuItemList createUserMenu(final RoomUserUI roomUserUI) {
        final UserGridMenuItemList itemList = new UserGridMenuItemList();
        switch (roomUserUI.getUserType()) {
        default:
            itemList.addItem(createNoActionsMenuItem());
        }
        return itemList;
    }
}
