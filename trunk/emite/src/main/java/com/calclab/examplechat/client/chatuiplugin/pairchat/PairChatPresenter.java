package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;

public class PairChatPresenter extends AbstractChatPresenter implements PairChat {

    private final PairChatUser otherUser;
    final PairChatListener listener;

    public PairChatPresenter(final Chat chat, final PairChatListener listener, final PairChatUser currentSessionUser,
            final PairChatUser otherUser) {
        super(chat, currentSessionUser, TYPE_PAIR_CHAT);
        this.otherUser = otherUser;
        this.input = "";
        this.listener = listener;
    }

    public void addMessage(final XmppURI userUri, final String message) {
        String userColor;

        if (sessionUser.getUri().equals(userUri) || sessionUser.getUri().getJid().equals(userUri.getJid())) {
            userColor = sessionUser.getColor();
        } else if (otherUser.getUri().equals(userUri) || otherUser.getUri().getJid().equals(userUri.getJid())) {
            // FIXME Roster / Jids Problems...
            userColor = otherUser.getColor();
        } else {
            final String error = "Unexpected message from user '" + userUri + "' in " + "chat '" + otherUser.getUri();
            Log.error(error);
            throw new RuntimeException(error);
        }
        view.addMessage(userUri.getNode(), userColor, message);
        listener.onMessageAdded(this);
    }

    public PairChatUser getOtherUser() {
        return otherUser;
    }

    public void init(final PairChatView view) {
        this.view = view;
        closeConfirmed = false;
    }

    public void onActivated() {
        listener.onActivate(this);
    }

    public void onDeactivate() {
        listener.onDeactivate(this);
    }

}
