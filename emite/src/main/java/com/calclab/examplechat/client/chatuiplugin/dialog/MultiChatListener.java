package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.extend.UIExtensionElement;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;

public interface MultiChatListener {

    void onCloseGroupChat(GroupChat groupChat);

    void onStatusSelected(final int status);

    void onClosePairChat(PairChatPresenter pairChat);

    void setGroupChatSubject(Chat chat, String subject);

    void onUserColorChanged(String color);

    void attachToExtPoint(UIExtensionElement extensionElement);

    void doAction(String eventId, Object param);

}
