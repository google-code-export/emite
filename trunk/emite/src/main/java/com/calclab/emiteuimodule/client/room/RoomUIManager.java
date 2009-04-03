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

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.RoomInvitation;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class RoomUIManager {

    private final RoomManager roomManager;
    private final I18nTranslationService i18n;
    private JoinRoomPanel joinRoomPanel;
    private final StatusUI statusUI;
    private final Session session;
    private String roomHostDefault;
    private final Event<String> onUserAlert;

    public RoomUIManager(final Session session, final RoomManager roomManager, final StatusUI statusUI,
	    final I18nTranslationService i18n) {
	this.session = session;
	this.roomManager = roomManager;
	this.statusUI = statusUI;
	this.i18n = i18n;
	this.roomHostDefault = "";
	this.onUserAlert = new Event<String>("onUserAlert");
    }

    public void init(final RoomUICommonPanelView view) {
	view.setJoinRoomEnabled(false);
	statusUI.onAfterLogin(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setJoinRoomEnabled(true);
	    }
	});
	statusUI.onAfterLogout(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setJoinRoomEnabled(false);
	    }
	});
	roomManager.onInvitationReceived(new Listener<RoomInvitation>() {
	    public void onEvent(final RoomInvitation parameter) {
		onUserAlert.fire("");
		view.roomJoinConfirm(parameter.getInvitor(), parameter.getRoomURI(), parameter.getReason());
	    }
	});
    }

    public void joinRoom(final String roomName, final String serverName) {
	final XmppURI uri = XmppURI.uri(roomName, serverName, session.getCurrentUser().getNode());
	roomManager.open(uri);
    }

    public void onJoinRoom() {
	if (joinRoomPanel == null) {
	    joinRoomPanel = new JoinRoomPanel(i18n, this, roomHostDefault);
	}
	joinRoomPanel.show();
    }

    public void onUserAlert(final Listener<String> listener) {
	onUserAlert.add(listener);
    }

    public void setRoomHostDefault(final String roomHostDefault) {
	this.roomHostDefault = roomHostDefault;
    }
}
