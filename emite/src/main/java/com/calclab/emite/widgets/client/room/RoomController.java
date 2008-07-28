package com.calclab.emite.widgets.client.room;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.widgets.client.chat.AbstractChatController;

public class RoomController extends AbstractChatController {
    private XmppURI chatJID;
    private String nick;

    public RoomController(final Session session, final RoomManager manager) {
	super(session, manager);
	this.nick = "user-" + new Date().getTime();
    }

    public void setNick(final String nick) {
	Log.debug("Nick: " + nick);
	this.nick = nick;
    }

    public void setWidget(final RoomWidget widget) {
	widget.setController(this);
	super.setWidget(widget);
    }

    @Override
    protected XmppURI getChatURI() {
	return new XmppURI(chatJID.getNode(), chatJID.getHost(), nick);
    }

    @Override
    protected String getFromUserName(final Message message) {
	return message.getFrom().getResource();
    }

    @Override
    protected boolean isOurChat(final Chat chat) {
	return chatJID.equalsNoResource(chat.getOtherURI());
    }

    void setRoomJID(final String roomName) {
	assert this.chatJID == null;
	this.chatJID = XmppURI.uri(roomName);
	showWaitingStatus();
    }

}
