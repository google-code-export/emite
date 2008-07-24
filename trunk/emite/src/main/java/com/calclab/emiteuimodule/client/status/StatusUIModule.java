package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.ISession;
import com.calclab.emite.client.xmpp.session.SessionImpl;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;
import com.calclab.suco.client.scopes.SingletonScope;

public class StatusUIModule implements Module {

    public Class<StatusUIModule> getType() {
	return StatusUIModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(StatusUI.class, new Provider<StatusUI>() {
	    public StatusUI get() {
		final I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
		final PresenceManager presenceManager = builder.getInstance(PresenceManager.class);
		final RosterManager rosterManager = builder.getInstance(RosterManager.class);
		final ChatManager chatManager = builder.getInstance(ChatManager.class);
		final RoomManager roomManager = builder.getInstance(RoomManager.class);
		final ISession sessionImpl = builder.getInstance(SessionImpl.class);
		final Xmpp xmpp = builder.getInstance(Xmpp.class);
		final StatusUIPresenter presenter = new StatusUIPresenter(xmpp, sessionImpl, presenceManager,
			rosterManager, chatManager, roomManager, i18n);
		final StatusUIPanel panel = new StatusUIPanel(presenter, i18n);
		presenter.init(panel);
		return presenter;
	    }
	}, SingletonScope.class);

    }

}
