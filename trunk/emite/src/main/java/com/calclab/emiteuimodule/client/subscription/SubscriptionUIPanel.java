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
package com.calclab.emiteuimodule.client.subscription;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.widgets.MessageBox;

public class SubscriptionUIPanel implements SubscriptionUIView {

    private final I18nTranslationService i18n;
    private final SubscriptionUIPresenter presenter;

    public SubscriptionUIPanel(final SubscriptionUIPresenter presenter, final I18nTranslationService i18n) {
	this.presenter = presenter;
	this.i18n = i18n;
    }

    public void confirmSusbscriptionRequest(final XmppURI jid, final String nick) {
	MessageBox.confirm(i18n.t("Confirm"), i18n.t("[%s] want to add you as a buddy. Do you want to permit?", jid
		.getJID().toString()), new MessageBox.ConfirmCallback() {
	    public void execute(final String btnID) {
		if (btnID.equals("yes")) {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.onPresenceAccepted(jid, nick);
			}
		    });
		} else {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.onPresenceNotAccepted(jid);
			}
		    });
		}
	    }
	});
    }
}
