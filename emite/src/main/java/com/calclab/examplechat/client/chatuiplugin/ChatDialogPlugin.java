package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPanel;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

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

            public void onStatusSelected(final int status) {
            }

            public void onSendMessage(final GroupChat groupChat, final String message) {
            }

            public void onSendMessage(final PairChatUser toUserChat, final String message) {
            }

            public void onClosePairChat(final PairChatPresenter pairChat) {
            }

            public void setGroupChatSubject(final GroupChat groupChat, final String subject) {
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
