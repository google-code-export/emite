package com.calclab.emiteuimodule.client.openchat;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emiteuimodule.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.signal.Slot;

public class OpenChatTestingPresenter {
    private final ChatManager chatManager;
    private final StatusUI statusUI;

    public OpenChatTestingPresenter(ChatManager chatManager, StatusUI statusUI) {
	this.chatManager = chatManager;
	this.statusUI = statusUI;
    }

    public void init(final OpenChatTestingView view) {
	view.setMenuItemEnabled(false);
	statusUI.onAfterLogin(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(true);
	    }
	});
	statusUI.onAfterLogout(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(false);
	    }
	});
    }

    public void onOpenChat(XmppURI uri) {
	chatManager.openChat(uri, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

}
