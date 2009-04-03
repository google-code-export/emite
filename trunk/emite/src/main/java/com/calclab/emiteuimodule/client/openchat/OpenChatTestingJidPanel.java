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

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.dialog.BasicDialogExtended;
import com.calclab.emiteuimodule.client.dialog.BasicDialogListener;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class OpenChatTestingJidPanel {
    private final I18nTranslationService i18n;
    private final OpenChatTestingPresenter presenter;
    private BasicDialogExtended dialog;
    private FormPanel formPanel;
    private TextField jid;

    public OpenChatTestingJidPanel(final I18nTranslationService i18n, final OpenChatTestingPresenter presenter) {
	this.i18n = i18n;
	this.presenter = presenter;
    }

    public void reset() {
	formPanel.getForm().reset();
    }

    public void show() {
	if (dialog == null) {
	    dialog = new BasicDialogExtended(i18n.t("Chat with other person"), false, false, 330, 160, "chat-icon",
		    i18n.tWithNT("Chat", "used in button"), i18n.tWithNT("Cancel", "used in button"),
		    new BasicDialogListener() {

			public void onCancelButtonClick() {
			    dialog.hide();
			    reset();
			}

			public void onFirstButtonClick() {
			    jid.validate();
			    if (formPanel.getForm().isValid()) {
				DeferredCommand.addCommand(new Command() {
				    public void execute() {
					presenter.onOpenChat(XmppURI.jid(jid.getValueAsString()));
					reset();
				    }
				});
				dialog.hide();
			    }
			}

		    });
	    dialog.setResizable(false);
	    createForm();
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

	jid = new TextField(i18n.t("Chat with (some Jabber Id)"), "jid", 150);
	jid.setAllowBlank(false);
	jid.setRegex(TextUtils.EMAIL_REGEXP);
	jid.setRegexText(i18n.t("A Jabber Id is something like 'someone@example.com'"));
	jid.setValidationEvent(false);
	final ToolTip fieldToolTip = new ToolTip(i18n.t("Note that the 'Jabber Id' sometimes is the same as the email "
		+ "(in gmail accounts for instance)."));
	fieldToolTip.applyTo(jid);
	jid.setValidateOnBlur(false);
	formPanel.add(jid);

	dialog.add(formPanel);
    }
}
