package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;
import com.calclab.modular.client.scopes.SingletonScope;

public class RoomUIModule implements Module {
    public Class<RoomUIModule> getType() {
	return RoomUIModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(RoomUIManager.class, new Provider<RoomUIManager>() {
	    public RoomUIManager get() {
		final I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
		final RoomManager roomManager = builder.getInstance(RoomManager.class);
		final StatusUI statusUI = builder.getInstance(StatusUI.class);
		final Session session = builder.getInstance(Session.class);
		final RoomUIManager manager = new RoomUIManager(session, roomManager, statusUI, i18n);
		final RoomUICommonPanel roomUICommonPanel = new RoomUICommonPanel(manager, statusUI, i18n);
		manager.init(roomUICommonPanel);
		return manager;
	    }
	}, SingletonScope.class);

    }

}
