package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xep.muc.RoomInvitation;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class RoomUIManager {

    private final RoomManager roomManager;
    private final I18nTranslationService i18n;
    private JoinRoomPanel joinRoomPanel;
    private final StatusUI statusUI;
    private final Session session;
    private String roomHostDefault;
    private final Signal<String> onUserAlert;

    public RoomUIManager(final Session session, final RoomManager roomManager, final StatusUI statusUI,
	    final I18nTranslationService i18n) {
	this.session = session;
	this.roomManager = roomManager;
	this.statusUI = statusUI;
	this.i18n = i18n;
	this.roomHostDefault = "";
	this.onUserAlert = new Signal<String>("onUserAlert");
    }

    public void init(final RoomUICommonPanelView view) {
	view.setJoinRoomEnabled(false);
	statusUI.onAfterLogin(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setJoinRoomEnabled(true);
	    }
	});
	statusUI.onAfterLogout(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setJoinRoomEnabled(false);
	    }
	});
	roomManager.onInvitationReceived(new Slot<RoomInvitation>() {
	    public void onEvent(final RoomInvitation parameter) {
		onUserAlert.fire("");
		view.roomJoinConfirm(parameter.getInvitor(), parameter.getRoomURI(), parameter.getReason());
	    }
	});
    }

    public void joinRoom(final String roomName, final String serverName) {
	final XmppURI uri = XmppURI.uri(roomName, serverName, session.getCurrentUser().getNode());
	roomManager.openChat(uri, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void onJoinRoom() {
	if (joinRoomPanel == null) {
	    joinRoomPanel = new JoinRoomPanel(i18n, this, roomHostDefault);
	}
	joinRoomPanel.show();
    }

    public void onUserAlert(final Slot<String> slot) {
	onUserAlert.add(slot);
    }

    public void setRoomHostDefault(final String roomHostDefault) {
	this.roomHostDefault = roomHostDefault;
    }
}
