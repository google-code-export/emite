package com.calclab.uimite.client.chat;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.container.Container;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.uimite.client.UIView;
import com.calclab.uimite.client.chat.ChatView.ChatViewListener;
import com.calclab.uimite.client.chat.StatusView.StatusViewListener;

public class ChatUIModule {
    public static void load(final Container container) {
	final Xmpp xmpp = container.get(Xmpp.class);

	final ChatUIFactory factory = container.register(ChatUIFactory.class, new ChatUIFactory());
	container.register(ChatUIModule.class, new ChatUIModule(xmpp, factory));
    }

    private final Xmpp xmpp;
    private final ChatUIFactory factory;

    public ChatUIModule(final Xmpp xmpp, final ChatUIFactory factory) {
	this.xmpp = xmpp;
	this.factory = factory;
    }

    /**
     * Se supone que este es el método principal del módulo
     * 
     * @param me
     * @param password
     * @param other
     * @return
     */
    public UIView createChat(final XmppURI me, final String password, final XmppURI other) {
	final StatusView statusView = factory.createStatusView(new StatusViewListener() {
	    public void onStatusChanged(String status) {
		xmpp.login(me, password, null, null);
	    }
	});

	final Chat chat = xmpp.getChatManager().openChat(other);
	final ChatView chatView = factory.createChatView(chat, new ChatViewListener() {
	    public void onContentChanged() {
		// se usará para, en interfaces más grandes, mostrar ese icono
		// de "ha llegado algo..." o para hacer el sonido
	    }
	});
	chatView.extensions.addTop(statusView);

	xmpp.getSession().addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		statusView.setState(current);
	    }
	});
	statusView.setState(xmpp.getSession().getState());
	return chatView;
    }
}
