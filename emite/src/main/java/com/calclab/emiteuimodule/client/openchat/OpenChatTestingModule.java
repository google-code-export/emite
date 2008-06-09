package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;

public class OpenChatTestingModule implements Module {

    public Class<OpenChatTestingModule> getType() {
	return OpenChatTestingModule.class;
    }

    public void onLoad(ModuleBuilder builder) {
	ChatManager chatManager = builder.getInstance(ChatManager.class);
	StatusUI statusUI = builder.getInstance(StatusUI.class);
	I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
	OpenChatTestingPresenter presenter = new OpenChatTestingPresenter(chatManager, statusUI);
	OpenChatTestingPanel panel = new OpenChatTestingPanel(presenter, statusUI, i18n);
	presenter.init(panel);
    }

}
