/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client.chat;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;

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
    protected boolean isOurChat(final Chat chat) {
	return chatJID.equalsNoResource(chat.getURI());
    }

}
