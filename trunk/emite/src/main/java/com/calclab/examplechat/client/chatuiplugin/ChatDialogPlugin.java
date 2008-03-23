package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;

public class ChatDialogPlugin extends Plugin {

    private MultiChatPresenter extChatDialog;

    public ChatDialogPlugin() {
        super("chatplugin");
    }

    @Override
    protected void start() {
        extChatDialog = new MultiChatPresenter(new MultiChatListener() {
            public void onCloseGroupChat(final GroupChat groupChat) {
            }

            public void onSendMessage(final GroupChat groupChat, final String message) {
            }

            public void onStatusSelected(final int status) {
            }
        });
        MultiChatPanel multiChatPanel = new MultiChatPanel(new I18nTranslationServiceMocked(), extChatDialog);
        extChatDialog.init(multiChatPanel);
    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
