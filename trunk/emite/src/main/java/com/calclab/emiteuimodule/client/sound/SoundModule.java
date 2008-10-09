package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.suco.client.ioc.decorator.Singleton;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;

public class SoundModule extends AbstractModule {
    public SoundModule() {
	super();
    }

    @Override
    public void onInstall() {
	register(Singleton.class, new Factory<SoundManager>(SoundManager.class) {
	    @Override
	    public SoundManager create() {
		final SoundManager soundManager = new SoundManager($(SubscriptionUI.class), $(RoomUIManager.class));
		final SoundPanel panel = new SoundPanel(soundManager, $(I18nTranslationService.class),
			$(StatusUI.class));
		soundManager.init(panel);
		return soundManager;
	    }
	});
    }
}
