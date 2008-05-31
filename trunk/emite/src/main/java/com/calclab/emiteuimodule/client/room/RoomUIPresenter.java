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
package com.calclab.emiteuimodule.client.room;

import java.util.Collection;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.chat.ChatUIPresenter;
import com.calclab.emiteuimodule.client.roster.ChatIconDescriptor;
import com.calclab.emiteuimodule.client.users.RoomUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem.UserGridMenuItemListener;

public class RoomUIPresenter extends ChatUIPresenter implements RoomUI {

    private RoomUIView view;

    private boolean isSubjectEditable;

    private RoomUserListUIPanel roomUserListUI;

    private final RoomUIListener listener;

    private final String currentUserAlias;

    private final I18nTranslationService i18n;

    private String lastInvitationReasonText;

    public RoomUIPresenter(final I18nTranslationService i18n, final XmppURI otherURI, final String currentUserAlias,
            final String currentUserColor, final RoomUIListener listener) {
        super(otherURI, currentUserAlias, currentUserColor, ChatIconDescriptor.roomsmall,
                ChatIconDescriptor.roomnewmessagesmall, listener);
        this.i18n = i18n;
        this.currentUserAlias = currentUserAlias;
        this.listener = listener;
        this.lastInvitationReasonText = i18n.t("Join to our conversation");
    }

    public void askInvitation(final XmppURI userURI) {
        view.askInvitation(userURI, lastInvitationReasonText);
    }

    public String getReasonText() {
        return lastInvitationReasonText;
    }

    public View getView() {
        return view;
    }

    public void init(final RoomUIView view, final RoomUserListUIPanel roomUserListUI) {
        super.init(view);
        this.view = view;
        this.roomUserListUI = roomUserListUI;
        listener.onCreated(this);
    }

    public boolean isSubjectEditable() {
        return isSubjectEditable;
    }

    public void onInviteUserRequested(final XmppURI userJid, final String reasonText) {
        this.lastInvitationReasonText = reasonText;
        listener.onInviteUserRequested(userJid, reasonText);
    }

    public void onModifySubjectRequested(final String newSubject) {
        listener.onModifySubjectRequested(newSubject);
    }

    public void onOccupantModified(final Occupant occupant) {
        final RoomUserUI roomUserUI = genRoomUser(occupant);
        roomUserListUI.updateUser(roomUserUI, createUserMenu(roomUserUI));
    }

    public void onOccupantsChanged(final Collection<Occupant> users) {
        roomUserListUI.removeAllUsers();
        for (final Iterator<Occupant> iterator = users.iterator(); iterator.hasNext();) {
            final Occupant occupant = iterator.next();
            final RoomUserUI roomUserUI = genRoomUser(occupant);
            roomUserListUI.addUser(roomUserUI, createUserMenu(roomUserUI));
            if (occupant.getUri().getResource().equals(currentUserAlias)) {
                if (occupant.getRole().equals(Role.moderator)) {
                    view.setSubjectEditable(true);
                    isSubjectEditable = true;
                } else {
                    view.setSubjectEditable(false);
                    isSubjectEditable = false;
                }
            }
        }
    }

    public void setSubject(final String newSubject) {
        view.setSubject(newSubject);
    }

    public void setUserListVisible(final boolean visible) {
        roomUserListUI.setVisible(visible);
    }

    private UserGridMenuItem<Object> createNoActionsMenuItem() {
        return new UserGridMenuItem<Object>("", i18n.t("No options"), new UserGridMenuItemListener() {
            public void onAction() {
            }
        });
    }

    private UserGridMenuItemList createUserMenu(final RoomUserUI roomUserUI) {
        final UserGridMenuItemList itemList = new UserGridMenuItemList();
        switch (roomUserUI.getRole()) {
        default:
            itemList.addItem(createNoActionsMenuItem());
        }
        return itemList;
    }

    private RoomUserUI genRoomUser(final Occupant occupant) {
        final RoomUserUI roomUserUI = new RoomUserUI(occupant, super.getColor(occupant.getUri().getResource()));
        return roomUserUI;
    }
}
