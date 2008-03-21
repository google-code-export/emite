package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.examplechat.client.chatuiplugin.ui.MultiRoomPanel;

public class ChatDialogPlugin extends Plugin {

    private MultiRoomPresenter extChatDialog;

    public ChatDialogPlugin() {
        super("chatplugin");
    }

    @Override
    protected void start() {
        extChatDialog = new MultiRoomPresenter(new MultiRoomListener() {
            public void onCloseRoom(final Room room) {
            }

            public void onSendMessage(final Room room, final String message) {
            }

            public void onStatusSelected(final int status) {
            }
        });
        MultiRoomPanel multiRoomPanel = new MultiRoomPanel(new I18nTranslationServiceMocked(), extChatDialog);
        extChatDialog.init(multiRoomPanel);
    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
