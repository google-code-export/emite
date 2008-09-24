package com.calclab.emiteuimodule.client.subscription;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

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
