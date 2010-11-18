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
package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class RoomUICommonPanel implements RoomUICommonPanelView {
    public class JoinRoomToolbarButton extends ToolbarButton implements View {
	public JoinRoomToolbarButton(final String title) {
	    super(title);
	}
    }

    private static final String JRB_ID = "emite-ruicp-jrb";

    private final JoinRoomToolbarButton joinRoomButton;
    private final I18nTranslationService i18n;
    private final RoomUIManager presenter;

    public RoomUICommonPanel(final RoomUIManager presenter, StatusUI statusUI, final I18nTranslationService i18n) {
	this.presenter = presenter;
	this.i18n = i18n;
	joinRoomButton = new JoinRoomToolbarButton(i18n.t("Join a chat room"));
	joinRoomButton.setIcon("images/group-chat.gif");
	joinRoomButton.addListener(new ButtonListenerAdapter() {
	    @Override
	    public void onClick(final Button button, final EventObject e) {
		presenter.onJoinRoom();
	    }
	});
	joinRoomButton.setId(JRB_ID);
	statusUI.addButtonItem(joinRoomButton);
    }

    public void roomJoinConfirm(final XmppURI invitor, final XmppURI roomURI, final String reason) {
	XmppURI jid = invitor == null ? null : invitor.getJID();
	String escape = TextUtils.escape(reason);
	MessageBox.confirm(i18n.t("Join to chat room [%s]?", roomURI.getJID().toString()), (jid != null ? i18n.t(
		"[%s] are inviting you to join this room: ", jid.toString()) : i18n
		.t("Someone are inviting you to join this room: "))
		+ (escape == null ? "" : escape), new MessageBox.ConfirmCallback() {
	    public void execute(final String btnID) {
		if (btnID.equals("yes")) {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.joinRoom(roomURI.getNode(), roomURI.getHost());
			}
		    });
		}
	    }
	});
    }

    public void setJoinRoomEnabled(final boolean enabled) {
	joinRoomButton.setDisabled(!enabled);
    }

}
