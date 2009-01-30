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
package com.calclab.emite.widgets.client.room;

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.widgets.client.chat.AbstractChatController;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.ioc.Provider;

public class RoomController extends AbstractChatController {
    private XmppURI chatJID;
    private String nick;
    private final Provider<RoomPresenceWidget> presenceWidgetProvider;
    private RoomPresenceWidget presenceWidget;

    public RoomController(final Session session, final RoomManager manager,
	    final Provider<RoomPresenceWidget> presenceWidgetProvider) {
	super(session, manager);
	this.presenceWidgetProvider = presenceWidgetProvider;
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

    public void showPresence(final boolean showPresence) {
	if (presenceWidget == null && showPresence == true) {
	    presenceWidget = presenceWidgetProvider.get();
	    if (chatJID != null) {
		presenceWidget.setParam(RoomPresenceWidget.PARAM_ROOM, chatJID.toString());
	    }
	    widget.dock(RoomWidget.EXT_RIGHT, presenceWidget);
	} else if (presenceWidget != null && showPresence == false) {
	    widget.unDock(presenceWidget);
	    presenceWidget = null;
	}
    }

    @Override
    protected XmppURI getChatURI() {
	return XmppURI.uri(chatJID.getNode(), chatJID.getHost(), nick);
    }

    @Override
    protected String getFromUserName(final Message message) {
	return message.getFrom().getResource();
    }

    @Override
    protected boolean isOurChat(final Chat chat) {
	return chatJID.equalsNoResource(chat.getURI());
    }

    void setRoomJID(final String roomName) {
	assert this.chatJID == null;
	this.chatJID = XmppURI.uri(roomName);
	if (presenceWidget != null) {
	    presenceWidget.setParam(RoomPresenceWidget.PARAM_ROOM, roomName);
	}
    }

}
