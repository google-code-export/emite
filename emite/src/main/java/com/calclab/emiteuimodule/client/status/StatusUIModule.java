package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class StatusUIModule extends AbstractModule {

    public StatusUIModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(Singleton.class, new Factory<StatusUI>(StatusUI.class) {
	    @Override
	    public StatusUI create() {
		final StatusUIPresenter presenter = new StatusUIPresenter($(Session.class), $(PresenceManager.class),
			$(Roster.class), $$(ChatManager.class), $$(RoomManager.class), $(SubscriptionUI.class),
			$(I18nTranslationService.class));
		final StatusUIPanel panel = new StatusUIPanel(presenter, $(I18nTranslationService.class));
		presenter.init(panel);
		return presenter;
	    }
	});

    }

}
