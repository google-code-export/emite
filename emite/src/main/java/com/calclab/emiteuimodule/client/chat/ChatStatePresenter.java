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

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.suco.client.events.Listener;

public class ChatStatePresenter {

    private static final String E_NOTIF_PAUSE = "e-notif-pause";
    private static final String E_NOTIF_INACTIVE = "e-notif-inactive";
    private static final String E_NOTIF_COMPOSING = "e-notif-composing";
    private static final String E_NOTIF_GONE = "e-notif-gone";

    private static final int CANCEL_TIMER = -1;
    private static final int MILLISECONS_TO_PAUSE = 5000;
    private static final int MILLISECONS_TO_INACTIVE = 30000;

    public ChatStateManager chatStateManager;
    private final ChatStateTimer timer;

    public ChatStatePresenter(final I18nTranslationService i18n, final ChatStateManager chatStateManager,
	    final ChatUI chatUI) {
	this.chatStateManager = chatStateManager;
	timer = new ChatStateTimer(this);
	chatUI.addEventListener(new ChatUIEventListener() {
	    public void onClose() {
		setOwnState(ChatStateManager.ChatState.gone);
	    }

	    public void onComposing() {
		setOwnState(ChatStateManager.ChatState.composing);
	    }

	    public void onInputFocus() {
		setOwnState(ChatStateManager.ChatState.active);
	    }

	    public void onInputUnFocus() {
		setOwnState(ChatStateManager.ChatState.inactive);
	    }
	});

	chatStateManager.onChatStateChanged(new Listener<ChatState>() {
	    public void onEvent(final ChatState state) {
		final String otherAlias = chatUI.getOtherAlias();
		switch (state) {
		case active:
		    chatUI.setSavedChatNotification(formatNotification(otherAlias, "is active", E_NOTIF_PAUSE));
		    chatUI.setChatTitleTextCls(E_NOTIF_PAUSE);
		    chatUI.showMessageEventInfo();
		    break;

		case gone:
		    chatUI.setSavedChatNotification(formatNotification(otherAlias, "finished the conversation",
			    E_NOTIF_GONE));
		    chatUI.setChatTitleTextCls(E_NOTIF_GONE);
		    chatUI.showMessageEventInfo();
		    break;

		case composing:
		    chatUI.setSavedChatNotification(formatNotification(otherAlias, "is writing", E_NOTIF_COMPOSING));
		    chatUI.setChatTitleTextCls(E_NOTIF_COMPOSING);
		    chatUI.showMessageEventInfo();
		    break;

		case inactive:
		    chatUI.setSavedChatNotification(new ChatNotification());
		    chatUI.setChatTitleTextCls(E_NOTIF_INACTIVE);
		    chatUI.clearMessageEventInfo();
		    break;

		case pause:
		    chatUI.setSavedChatNotification(formatNotification(otherAlias, "stop to write", E_NOTIF_PAUSE));
		    chatUI.setChatTitleTextCls(E_NOTIF_PAUSE);
		    chatUI.showMessageEventInfo();
		    break;

		default:
		    break;
		}
	    }

	    private ChatNotification formatNotification(final String otherAlias, final String text, final String style) {
		return new ChatNotification(i18n.t("[%s] " + text, otherAlias), style);
	    }

	});

    }

    protected void onTime() {
	switch (chatStateManager.getOwnState()) {
	case composing:
	    setOwnState(ChatStateManager.ChatState.pause);
	    break;
	case active:
	    setOwnState(ChatStateManager.ChatState.inactive);
	    break;
	case pause:
	    setOwnState(ChatStateManager.ChatState.inactive);
	    break;
	default:
	    timer.cancel();
	    break;
	}
    }

    private void setOwnState(final ChatState chatState) {
	if (chatStateManager != null
		&& chatStateManager.getNegotiationStatus().equals(ChatStateManager.NegotiationStatus.accepted)) {
	    int time = CANCEL_TIMER;
	    switch (chatState) {
	    case composing:
		time = MILLISECONS_TO_PAUSE;
		break;
	    case active:
		time = MILLISECONS_TO_INACTIVE;
		break;
	    case inactive:
		time = CANCEL_TIMER;
		break;
	    case gone:
		time = CANCEL_TIMER;
		break;
	    case pause:
		time = MILLISECONS_TO_INACTIVE;
		break;
	    }
	    if (time > 0) {
		timer.schedule(time);
	    } else {
		timer.cancel();
	    }
	    chatStateManager.setOwnState(chatState);
	}
    }
}
