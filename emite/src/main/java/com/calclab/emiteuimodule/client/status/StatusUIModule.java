package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.core.client.Xmpp;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.RosterManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;

public class StatusUIModule extends AbstractModule {

    public StatusUIModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<StatusUI>(StatusUI.class) {
	    public StatusUI create() {
		final StatusUIPresenter presenter = new StatusUIPresenter($$(Xmpp.class), $(Session.class),
			$(PresenceManager.class), $$(RosterManager.class), $$(ChatManager.class),
			$$(RoomManager.class), $(I18nTranslationService.class));
		final StatusUIPanel panel = new StatusUIPanel(presenter, $(I18nTranslationService.class));
		presenter.init(panel);
		return presenter;
	    }
	});

    }

}
