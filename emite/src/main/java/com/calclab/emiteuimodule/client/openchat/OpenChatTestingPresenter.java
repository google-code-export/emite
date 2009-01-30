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
package com.calclab.emiteuimodule.client.openchat;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.events.Listener;

public class OpenChatTestingPresenter {
    private final ChatManager chatManager;
    private final StatusUI statusUI;

    public OpenChatTestingPresenter(final ChatManager chatManager, final StatusUI statusUI) {
	this.chatManager = chatManager;
	this.statusUI = statusUI;
    }

    public void init(final OpenChatTestingView view) {
	view.setMenuItemEnabled(false);
	statusUI.onAfterLogin(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(true);
	    }
	});
	statusUI.onAfterLogout(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		view.setMenuItemEnabled(false);
	    }
	});
    }

    public void onOpenChat(final XmppURI uri) {
	chatManager.open(uri);
    }

}
