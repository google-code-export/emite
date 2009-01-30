package com.calclab.emiteuimodule.client.openchat;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.events.Listener;

public class OpenChatTestingPresenter {
    private final ChatManager chatManager;
    private final StatusUI statusUI;

    public OpenChatTestingPresenter(final ChatManager chatManager, final StatusUI statusUI) {
	this.chatManager = chatManager;
	this.statusUI = statusUI;
    }

    public void init(final OpenChatTestingView view) {
	view.setMenuItemEnabled(false);
	statusUI.onAfterLogin(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(true);
	    }
	});
	statusUI.onAfterLogout(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(false);
	    }
	});
    }

    public void onOpenChat(final XmppURI uri) {
	chatManager.openChat(uri);
    }

}
