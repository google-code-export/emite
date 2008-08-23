package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.roster.RosterUIPresenter;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scope.SingletonScope;

public class SoundModule extends AbstractModule {
    public SoundModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(SingletonScope.class, new Factory<SoundManager>(SoundManager.class) {
	    public SoundManager create() {
		// Waiting for RosterUIModule:
		final RosterUIPresenter rosterUIPresenter = null;
		final SoundManager soundManager = new SoundManager(rosterUIPresenter, $(RoomUIManager.class));
		final SoundPanel panel = new SoundPanel(soundManager, $(I18nTranslationService.class),
			$(StatusUI.class));
		soundManager.init(panel);
		return soundManager;
	    }
	});
    }
}
