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
package com.calclab.emiteuimodule.client.room;

import java.util.Collection;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Occupant.Role;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.emiteuimodule.client.chat.ChatUIPresenter;
import com.calclab.emiteuimodule.client.users.RoomUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem.UserGridMenuItemListener;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Event2;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;

public class RoomUIPresenter extends ChatUIPresenter implements RoomUI {

    private RoomUIView view;
    private boolean isSubjectEditable;
    private RoomUserListUIPanel roomUserListUI;
    private final String currentUserAlias;
    private final I18nTranslationService i18n;
    private String lastInvitationReasonText;
    private final Event2<XmppURI, String> onInviteUserRequested;
    private final Event<String> onModifySubjectRequested;
    private boolean changeSubjectMessageDisplayed;

    public RoomUIPresenter(final I18nTranslationService i18n, final XmppURI otherURI, final String currentUserAlias,
	    final String currentUserColor) {
	super(otherURI, currentUserAlias, currentUserColor, "room-icon", "room-h-icon");
	this.i18n = i18n;
	this.currentUserAlias = currentUserAlias;
	this.lastInvitationReasonText = i18n.t("Join to our conversation");
	this.onInviteUserRequested = new Event2<XmppURI, String>("onInviteUserRequested");
	this.onModifySubjectRequested = new Event<String>("onModifySubjectRequested");
	changeSubjectMessageDisplayed = false;
    }

    @Override
    public void addMessage(final XmppURI fromURI, final String body) {
	super.addMessage(fromURI.getResource(), body);
    }

    public void askInvitation(final XmppURI userURI) {
	view.askInvitation(userURI, lastInvitationReasonText);
    }

    public String getReasonText() {
	return lastInvitationReasonText;
    }

    public String getSubject() {
	return view.getSubject();
    }

    @Override
    public View getView() {
	return view;
    }

    public void init(final RoomUIView view, final RoomUserListUIPanel roomUserListUI) {
	super.init(view);
	this.view = view;
	this.roomUserListUI = roomUserListUI;
	view.setChatTitleTextCls(ChatUI.STYLE_BLOCKED);
	addInfoMessage(i18n.t("Opening chat room..."));
    }

    public boolean isSubjectEditable() {
	return isSubjectEditable;
    }

    public void onInviteUserRequested(final Listener2<XmppURI, String> param) {
	onInviteUserRequested.add(param);
    }

    public void onInviteUserRequested(final XmppURI userJid, final String reasonText) {
	this.lastInvitationReasonText = reasonText;
	onInviteUserRequested.fire(userJid, reasonText);
    }

    public void onModifySubjectRequested(final Listener<String> listener) {
	onModifySubjectRequested.add(listener);
    }

    public void onOccupantModified(final Occupant occupant) {
	final RoomUserUI roomUserUI = genRoomUser(occupant);
	roomUserListUI.updateUser(roomUserUI, createUserMenu(roomUserUI));
    }

    public void onOccupantsChanged(final Collection<Occupant> users) {
	roomUserListUI.removeAllUsers();
	for (Occupant occupant : users) {
	    final RoomUserUI roomUserUI = genRoomUser(occupant);
	    roomUserListUI.addUser(roomUserUI, createUserMenu(roomUserUI));
	    if (occupant.getURI().getResource().equals(currentUserAlias)) {
		if (occupant.getRole().equals(Role.moderator)) {
		    view.setSubjectEditable(true);
		    isSubjectEditable = true;
		    if (!changeSubjectMessageDisplayed
			    && view.getSubject().equals(i18n.t(RoomUIView.DEFAULT_INITIAL_MESSAGE))) {
			view.addInfoMessage(i18n.t("To change the room subject just click on it."));
			changeSubjectMessageDisplayed = true;
		    }
		} else {
		    view.setSubjectEditable(false);
		    isSubjectEditable = false;
		}
	    }
	}
    }

    public void requestModifySubject(final String oldSubject, final String newSubject) {
	if (newSubject == null || newSubject.equals("")) {
	    Log.info("Subject empty");
	    view.setSubject(oldSubject);
	} else {
	    onModifySubjectRequested.fire(newSubject);
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
	final RoomUserUI roomUserUI = new RoomUserUI(occupant, super.getColor(occupant.getURI().getResource()));
	return roomUserUI;
    }
}
