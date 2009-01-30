package com.calclab.emite.hablar.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.suco.client.events.Listener;

public class PagesController {
    public PagesController(final Session session, final ChatManager chatManager, final PagesContainer view) {

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		if (state == State.ready) {
		    view.changePageTitle(0, session.getCurrentUser().getNode());
		    view.showPage(1);
		} else if (state == State.disconnected) {
		    view.changePageTitle(0, "login");
		    view.showPage(0);
		}
	    }
	});

	chatManager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
	    }
	});

    }

}
