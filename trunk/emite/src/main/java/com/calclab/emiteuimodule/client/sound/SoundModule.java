package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.roster.RosterPresenter;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;

public class SoundModule implements Module {

    public Class<SoundModule> getType() {
        return SoundModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
        builder.registerProvider(SoundManager.class, new Provider<SoundManager>() {
            public SoundManager get() {
                I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
                // Waiting for RosterUIModule:
                RosterPresenter rosterPresenter = null;
                RoomUIManager roomUIManager = builder.getInstance(RoomUIManager.class);
                StatusUI statusUI = builder.getInstance(StatusUI.class);
                SoundManager soundManager = new SoundManager(rosterPresenter, roomUIManager);
                SoundPanel panel = new SoundPanel(soundManager, i18n, statusUI);
                soundManager.init(panel);
                return soundManager;
            }
        });
    }
}
