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
package com.calclab.emiteuimodule.client.roster;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emiteuimodule.client.dialog.BasicDialogExtended;
import com.calclab.emiteuimodule.client.dialog.BasicDialogListener;
import com.calclab.emiteuimodule.client.dialog.MultiChatPresenter;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;

public class RosterItemPanel {

    private BasicDialog dialog;
    private FormPanel formPanel;
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private TextField name;
    private TextField jid;

    public RosterItemPanel(final I18nTranslationService i18n, final MultiChatPresenter presente) {
	this.i18n = i18n;
	this.presenter = presente;
    }

    public void reset() {
	formPanel.getForm().reset();
    }

    public void show() {
	if (dialog == null) {
	    dialog = new BasicDialogExtended(i18n.t("Add a new buddy"), false, false, 330, 220, "useradd-icon", i18n
		    .tWithNT("Add", "used in button"), i18n.tWithNT("Cancel", "used in button"),
		    new BasicDialogListener() {

			public void onCancelButtonClick() {
			    dialog.hide();
			    reset();
			}

			public void onFirstButtonClick() {
			    doAddItem();
			}

			private void doAddItem() {
			    // FIXME duplicate code
			    name.validate();
			    jid.validate();
			    if (formPanel.getForm().isValid()) {
				DeferredCommand.addCommand(new Command() {
				    public void execute() {
					presenter.addRosterItem(name.getValueAsString(), jid.getValueAsString());
					reset();
				    }
				});
				dialog.hide();
			    }
			}
		    });
	    dialog.setResizable(false);
	    createForm();

	    // TODO define a UI Extension Point here
	}
	dialog.show();
	name.focus();
    }

    private void createForm() {
	formPanel = new FormPanel();
	formPanel.setFrame(true);
	formPanel.setAutoScroll(false);

	formPanel.setWidth(333);
	formPanel.setLabelWidth(100);
	formPanel.setPaddings(10);

	final Label label = new Label();
	label.setHtml("<p>" + i18n.t("Please fill this form with the info of your new buddy:") + "</p><br/>");
	label.setWidth(270);
	label.setHeight(40);
	formPanel.add(label);

	name = new TextField(i18n.t("Buddy Nickname"), "name", 150);
	name.setAllowBlank(false);
	name.setValidationEvent(false);
	formPanel.add(name);

	jid = new TextField(i18n.t("Buddy Jabber Id"), "jid", 150);
	jid.setAllowBlank(false);
	jid.setValidationEvent(false);
	jid.setRegex(TextUtils.EMAIL_REGEXP);
	jid.setRegexText(i18n.t("A Jabber Id is something like 'someone@example.com'"));
	// jid.setVtype(VType.EMAIL);
	final ToolTip fieldToolTip = new ToolTip(i18n.t("Note that the 'Jabber Id' sometimes is the same as the email "
		+ "(in gmail accounts for instance)."));
	fieldToolTip.applyTo(jid);
	formPanel.add(jid);

	dialog.add(formPanel);
    }

}
