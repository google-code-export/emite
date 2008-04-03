package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserList;

public interface ChatDialogFactory {

    public MultiChat createMultiChat(final PairChatUser currentSessionUser, final I18nTranslationService i18n,
            final MultiChatListener listener);

    public GroupChatUserList createGroupChatUserList();

    public GroupChat createGroupChat(final ChatId chatId, final GroupChatListener listener,
            final GroupChatUser currentSessionUser);

    public PairChat createPairChat(final ChatId chatId, final PairChatListener listener,
            final PairChatUser currentSessionUser, final PairChatUser otherUser);

}