/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteui.client.emiteuiplugin.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteui.client.emiteuiplugin.dialog.BasicDialogExtended;
import com.calclab.emiteui.client.emiteuiplugin.dialog.BasicDialogListener;
import com.calclab.emiteui.client.emiteuiplugin.dialog.MultiChatPresenter;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class JoinRoomDialogPanel {

    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private BasicDialogExtended dialog;
    private FormPanel formPanel;
    private TextField roomName;
    private TextField serverName;

    public JoinRoomDialogPanel(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
    }

    public void reset() {
        formPanel.getForm().reset();
    }

    public void show() {
        if (dialog == null) {
            dialog = new BasicDialogExtended(i18n.t("Join a chat room"), false, false, 330, 160, "chat-icon", i18n
                    .tWithNT("Join", "used in button"), i18n.tWithNT("Cancel", "used in button"),
                    new BasicDialogListener() {

                        public void onCancelButtonClick() {
                            dialog.hide();
                            reset();
                        }

                        public void onFirstButtonClick() {
                            presenter.joinRoom(roomName.getValueAsString(), serverName.getValueAsString());
                            dialog.hide();
                            reset();
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
        formPanel.add(roomName);

        serverName = new TextField(i18n.t("Room Server Name"), "jid", 150);
        serverName.setAllowBlank(false);
        // FIXME (get from options)
        serverName.setValue("rooms.localhost");
        ToolTip fieldToolTip = new ToolTip(i18n.t("Something like 'conference.jabber.org'."));
        fieldToolTip.applyTo(serverName);
        formPanel.add(serverName);

        dialog.add(formPanel);
    }
}
