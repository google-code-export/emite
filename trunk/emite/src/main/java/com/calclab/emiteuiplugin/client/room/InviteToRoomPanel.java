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
package com.calclab.emiteuiplugin.client.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuiplugin.client.dialog.BasicDialogExtended;
import com.calclab.emiteuiplugin.client.dialog.BasicDialogListener;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPresenter;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class InviteToRoomPanel {

    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private BasicDialogExtended dialog;
    private FormPanel formPanel;
    private TextField jid;
    private TextField reason;

    public InviteToRoomPanel(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
    }

    public void reset() {
        formPanel.getForm().reset();
    }

    public void show() {
        if (dialog == null) {
            dialog = new BasicDialogExtended(i18n.t("Invite someone to this room"), false, false, 330, 160,
                    "chat-icon", i18n.tWithNT("Invite", "used in button"), i18n.tWithNT("Cancel", "used in button"),
                    new BasicDialogListener() {

                        public void onCancelButtonClick() {
                            dialog.hide();
                            reset();
                        }

                        public void onFirstButtonClick() {
                            jid.validate();
                            reason.validate();
                            if (formPanel.getForm().isValid()) {
                                presenter.inviteUserToRoom(jid.getValueAsString(), reason.getValueAsString());
                                dialog.hide();
                                reset();
                            }
                        }

                    });
            dialog.setResizable(false);
            createForm();

            // TODO define a UI Extension Point here
        }
        dialog.show();
        jid.focus();
    }

    private void createForm() {
        formPanel = new FormPanel();
        formPanel.setFrame(true);
        formPanel.setAutoScroll(false);

        formPanel.setWidth(333);
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10);

        jid = new TextField(i18n.t("Invite to (some Jabber Id)"), "jid", 150);
        jid.setAllowBlank(false);
        // jid.setVtype(VType.EMAIL);
        jid.setValidationEvent(false);
        ToolTip fieldToolTip = new ToolTip(i18n.t("Note that the 'Jabber Id' sometimes is the same as the email "
                + "(in gmail accounts for instance)."));
        fieldToolTip.applyTo(jid);
        jid.setValidateOnBlur(false);
        formPanel.add(jid);

        reason = new TextField(i18n.t("Invitation reason"), "jid", 150);
        reason.setAllowBlank(false);
        reason.setValidationEvent(false);
        reason.setValue(i18n.t("Join to our conversation"));
        formPanel.add(reason);

        dialog.add(formPanel);
    }
}
