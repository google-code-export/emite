/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUI;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserList;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public interface ChatDialogFactory {

    public GroupChat createGroupChat(final Chat chat, final GroupChatListener listener,
            final GroupChatUserType sessionGroupChatUserType);

    public GroupChatUserList createGroupChatUserList();

    public MultiChat createMultiChat(final AbstractXmpp xmpp, MultiChatCreationParam param,
            final I18nTranslationService i18n, final MultiChatListener listener);

    public PairChat createPairChat(final Chat chat, final PairChatListener listener, String currentUserJid,
            final PairChatUser otherUser);

    public RosterUI createrRosterUI(final AbstractXmpp xmpp, final I18nTranslationService i18n);

}