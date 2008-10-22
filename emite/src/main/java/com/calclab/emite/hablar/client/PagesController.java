package com.calclab.emite.hablar.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.hablar.client.pages.ConversationController;
import com.calclab.emite.hablar.client.pages.ConversationPage;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;

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

	chatManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		final ConversationPage conversationPage = new ConversationPage();
		final int pageId = view.addPage(conversation.getURI().toString(), conversationPage);
		new ConversationController(conversation, conversationPage);
		conversationPage.onClose(new Listener0() {
		    public void onEvent() {
			view.removePanel(conversationPage);
		    }
		});
		view.showPage(pageId);
	    }
	});

    }

}
