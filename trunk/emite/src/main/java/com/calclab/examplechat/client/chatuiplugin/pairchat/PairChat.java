package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.calclab.emite.client.xmpp.stanzas.XmppJID;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;

public interface PairChat extends AbstractChat {

    void addMessage(XmppJID userJid, String message);

    PairChatUser getOtherUser();

}
