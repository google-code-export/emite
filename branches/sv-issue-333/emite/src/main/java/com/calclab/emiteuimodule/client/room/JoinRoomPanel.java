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

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emiteuimodule.client.dialog.BasicDialogExtended;
import com.calclab.emiteuimodule.client.dialog.BasicDialogListener;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class JoinRoomPanel {

    private static final String ROOM_HOST_NAME_ID = "emite-jrp-rhm";
    private static final String ROOM_NAME_ID = "emite-jrp-rm";
    private static final String CANCEL_ID = "emite-jrp-cb";
    private static final String JOIN_ID = "emite-jrp-jb";
    private final I18nTranslationService i18n;
    private final RoomUIManager presenter;
    private BasicDialogExtended dialog;
    private FormPanel formPanel;
    private TextField roomName;
    private TextField roomHostName;
    private final String roomHost;

    public JoinRoomPanel(final I18nTranslationService i18n, final RoomUIManager roomUIManager, final String roomHost) {
	this.i18n = i18n;
	this.presenter = roomUIManager;
	this.roomHost = roomHost;
    }

    public void reset() {
	formPanel.getForm().reset();
    }

    public void show() {
	if (dialog == null) {
	    dialog = new BasicDialogExtended(i18n.t("Join a chat room"), false, false, 330, 180, "chat-icon", i18n
		    .tWithNT("Join", "used in button"), JOIN_ID, i18n.tWithNT("Cancel", "used in button"), CANCEL_ID,
		    new BasicDialogListener() {
			public void onCancelButtonClick() {
			    dialog.hide();
			    reset();
			}

			public void onFirstButtonClick() {
			    doJoin();
			}
		    });
	    dialog.setResizable(false);
	    createForm();

	    // TODO define a UI Extension Point here
	}
	dialog.show();
	roomName.focus();
    }

    private void createForm() {
	formPanel = new FormPanel();
	formPanel.setFrame(true);
	formPanel.setAutoScroll(false);

	formPanel.setWidth(333);
	formPanel.setLabelWidth(100);
	formPanel.setPaddings(10);

	roomName = new TextField(i18n.t("Room Name"), "name", 150);
	roomName.setAllowBlank(false);
	roomName.setValidationEvent(false);
	roomName.setRegex("^[a-z0-9_\\-]+$");
	roomName.setRegexText(i18n.t("Can only contain characters, numbers, and dashes"));
	roomName.setId(ROOM_NAME_ID);
	formPanel.add(roomName);

	roomHostName = new TextField(i18n.t("Room Server Name"), "jid", 150);
	roomHostName.setAllowBlank(false);
	roomHostName.setValidationEvent(false);
	// FIXME (get from options)
	roomHostName.setValue(roomHost);
	final ToolTip fieldToolTip = new ToolTip(i18n.t("Something like 'conference.jabber.org'."));
	fieldToolTip.applyTo(roomHostName);
	roomHostName.setId(ROOM_HOST_NAME_ID);
	formPanel.add(roomHostName);

	dialog.add(formPanel);
	roomName.addListener(new FieldListenerAdapter() {
	    @Override
	    public void onSpecialKey(final Field field, final EventObject e) {
		if (e.getKey() == 13) {
		    doJoin();
		}
	    }
	});
    }

    private void doJoin() {
	roomName.validate();
	roomHostName.validate();
	if (formPanel.getForm().isValid()) {
	    DeferredCommand.addCommand(new Command() {
		public void execute() {
		    presenter.joinRoom(roomName.getValueAsString(), roomHostName.getValueAsString());
		    reset();
		}
	    });
	    dialog.hide();
	}
    }
}
