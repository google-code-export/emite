package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.roster.RosterUIPresenter;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.DeprecatedModule;
import com.calclab.suco.client.modules.ModuleBuilder;

public class SoundModule extends DeprecatedModule {

    public Class<SoundModule> getType() {
	return SoundModule.class;
    }

    @Override
    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(SoundManager.class, new Provider<SoundManager>() {
	    public SoundManager get() {
		final I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
		// Waiting for RosterUIModule:
		final RosterUIPresenter rosterUIPresenter = null;
		final RoomUIManager roomUIManager = builder.getInstance(RoomUIManager.class);
		final StatusUI statusUI = builder.getInstance(StatusUI.class);
		final SoundManager soundManager = new SoundManager(rosterUIPresenter, roomUIManager);
		final SoundPanel panel = new SoundPanel(soundManager, i18n, statusUI);
		soundManager.init(panel);
		return soundManager;
	    }
	});
    }
}
