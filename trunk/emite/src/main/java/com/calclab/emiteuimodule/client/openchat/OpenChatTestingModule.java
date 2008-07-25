package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.modules.DeprecatedModule;
import com.calclab.suco.client.modules.ModuleBuilder;

public class OpenChatTestingModule extends DeprecatedModule {

    public Class<OpenChatTestingModule> getType() {
	return OpenChatTestingModule.class;
    }

    @Override
    public void onLoad(final ModuleBuilder builder) {
	// FIXME: this is an error
	// ChatManager is in SessionContext: that means tou can only retrieve
	// the instance if the session is created... not this case!!!
	final ChatManager chatManager = builder.getInstance(ChatManager.class);
	final StatusUI statusUI = builder.getInstance(StatusUI.class);
	final I18nTranslationService i18n = builder.getInstance(I18nTranslationService.class);
	final OpenChatTestingPresenter presenter = new OpenChatTestingPresenter(chatManager, statusUI);
	final OpenChatTestingPanel panel = new OpenChatTestingPanel(presenter, statusUI, i18n);
	presenter.init(panel);
    }

}
