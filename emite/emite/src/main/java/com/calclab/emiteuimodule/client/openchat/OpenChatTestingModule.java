package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.module.AbstractModule;

public class OpenChatTestingModule extends AbstractModule {

    public OpenChatTestingModule() {
	super();
    }

    @Override
    public void onLoad() {
	final OpenChatTestingPresenter presenter = new OpenChatTestingPresenter($(ChatManager.class), $(StatusUI.class));
	final OpenChatTestingPanel panel = new OpenChatTestingPanel(presenter, $(StatusUI.class),
		$(I18nTranslationService.class));
	presenter.init(panel);
    }

}
