package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

public class RoomUIModule extends AbstractModule {
    public RoomUIModule() {
	super(RoomUIModule.class);
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<RoomUIManager>(RoomUIManager.class) {
	    public RoomUIManager create() {
		final RoomUIManager manager = new RoomUIManager($(Session.class), $(RoomManager.class), $(StatusUI.class), $(I18nTranslationService.class));
		final RoomUICommonPanel roomUICommonPanel = new RoomUICommonPanel(manager, $(StatusUI.class), $(I18nTranslationService.class));
		manager.init(roomUICommonPanel);
		return manager;
	    }
	});
    }

}
