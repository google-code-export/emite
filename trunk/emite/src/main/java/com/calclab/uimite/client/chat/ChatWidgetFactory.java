package com.calclab.uimite.client.chat;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.uimite.client.UIView;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatWidgetFactory {

    private final Xmpp xmpp;

    public ChatWidgetFactory(final Xmpp xmpp) {
	this.xmpp = xmpp;
    }

    public UIView createChatWidget(final XmppURI other) {
	final Chat chat = xmpp.getChatManager().openChat(other);
	final ChatView chatView = new ChatView();
	new ChatPresenter(chat, chatView);
	return chatView;
    }

    public Widget createSimpleChat(final XmppURI me, final String password, final XmppURI other) {
	final DockPanel panel = new DockPanel();
	panel.add((Widget) createStatusWidget(me, password), DockPanel.NORTH);
	panel.add((Widget) createChatWidget(other), DockPanel.CENTER);
	return panel;
    }

    public UIView createStatusWidget(final XmppURI me, final String password) {
	final StatusView view = new StatusView();
	new StatusPresenter(me, password, xmpp.getSession(), view);
	return view;
    }

}
