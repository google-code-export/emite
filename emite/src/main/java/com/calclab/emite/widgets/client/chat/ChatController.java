package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;

public class ChatController extends AbstractChatController {

    private XmppURI chatJID;
    private String userName;

    public ChatController(final Session session, final ChatManager manager) {
	super(session, manager);
    }

    public void setChatJID(final String jid) {
	this.chatJID = XmppURI.uri(jid);
	this.userName = null;
	widget.write(null, "Chat with: " + jid);
    }

    public void setWidget(final ChatWidget widget) {
	widget.setController(this);
	super.setWidget(widget);
    }

    @Override
    protected XmppURI getChatURI() {
	return chatJID;
    }

    @Override
    protected String getFromUserName(final Message message) {
	if (userName == null) {
	    userName = message.getFrom().getNode();
	}
	return userName;
    }

    @Override
    protected boolean isOurChat(final Conversation conversation) {
	return chatJID.equalsNoResource(conversation.getURI());
    }

}
