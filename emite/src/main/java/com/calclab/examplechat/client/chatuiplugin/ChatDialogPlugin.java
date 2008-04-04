/**
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

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.params.CreateGroupChatActionParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatSubjectParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatUserAddActionParam;

public class ChatDialogPlugin extends Plugin {

    // Input events
    public static final String OPEN_CHAT_DIALOG = "emiteuiplugin.openchatdialog";
    public static final String SET_GROUPCHAT_SUBJECT = "emiteuiplugin.groupchatsubjectchanged";
    public static final String SET_STATUS = "emiteuiplugin.setstatus";
    public static final String ACTIVATE_CHAT = "emiteuiplugin.activatechat";
    public static final String ADD_PRESENCE_BUDDY = "emiteuiplugin.addpresencebuddy";
    public static final String ADD_USER_TO_GROUP_CHAT = "emiteuiplugin.addusertogroupchat";
    public static final String CLOSE_CHAT_DIALOG = "emiteuiplugin.closechatdialog";
    public static final String CREATE_GROUP_CHAT = "emiteuiplugin.creategroupchat";
    public static final String CREATE_PAIR_CHAT = "emiteuiplugin.createpairchat";
    public static final String MESSAGE_RECEIVED = "emiteuiplugin.messagereceived";
    // Output events
    public static final String ON_PAIR_CHAT_START = "emiteuiplugin.onpairchatstart";
    public static final String ON_GROUP_CHAT_CLOSED = "emiteuiplugin.ongroupchatclosed";
    public static final String ON_GROUP_CHAT_SUBJECT_CHANGED = "emiteuiplugin.ongroupchatsubjectchanged";
    public static final String ON_PAIR_CHAT_CLOSED = "emiteuiplugin.onpairchatclosed";
    public static final String ON_STATUS_SELECTED = "emiteuiplugin.onstatusselected";
    public static final String ON_USER_COLOR_SELECTED = "emiteuiplugin.onusercolorselected";

    private MultiChat multiChatDialog;

    public ChatDialogPlugin() {
        super("emiteuiplugin");
    }

    @Override
    protected void start() {
        final Dispatcher dispatcher = getDispatcher();
        dispatcher.subscribe(OPEN_CHAT_DIALOG, new Action<PairChatUser>() {
            public void execute(final PairChatUser user) {
                if (multiChatDialog == null) {
                    createChatDialog(user);
                }
                multiChatDialog.show();
            }

            private void createChatDialog(final PairChatUser user) {
                multiChatDialog = ChatDialogFactoryImpl.App.getInstance().createMultiChat(user,
                        new I18nTranslationServiceMocked(), new MultiChatListener() {

                            public void attachToExtPoint(final UIExtensionElement extensionElement) {
                                dispatcher.fire(PlatformEvents.ATTACH_TO_EXT_POINT, extensionElement);
                            }

                            public void doAction(final String eventId, final Object param) {
                                dispatcher.fire(eventId, param);
                            }

                            public void onCloseGroupChat(final GroupChat groupChat) {
                                dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_CLOSED, groupChat);
                            }

                            public void onClosePairChat(final PairChatPresenter pairChat) {
                                dispatcher.fire(ChatDialogPlugin.ON_PAIR_CHAT_CLOSED, pairChat);
                            }

                            public void onStatusSelected(final int status) {
                                dispatcher.fire(ChatDialogPlugin.ON_STATUS_SELECTED, new Integer(status));
                            }

                            public void onUserColorChanged(final String color) {
                                dispatcher.fire(ChatDialogPlugin.ON_USER_COLOR_SELECTED, color);
                            }

                            public void setGroupChatSubject(final Chat groupChat, final String subject) {
                                dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_SUBJECT_CHANGED,
                                        new GroupChatSubjectParam(groupChat, subject));
                            }
                        });
            }
        });

        dispatcher.subscribe(CLOSE_CHAT_DIALOG, new Action<Object>() {
            public void execute(final Object param) {
                multiChatDialog.closeAllChats(true);
            }
        });

        dispatcher.subscribe(SET_STATUS, new Action<Integer>() {
            public void execute(final Integer status) {
                if (multiChatDialog != null) {
                    multiChatDialog.setStatus(status);
                }
            }
        });

        dispatcher.subscribe(CREATE_GROUP_CHAT, new Action<CreateGroupChatActionParam>() {
            public void execute(final CreateGroupChatActionParam params) {
                multiChatDialog.createGroupChat(params.getGroupChat(), params.getUserAlias(), params
                        .getGroupChatUserType());
            }
        });

        dispatcher.subscribe(CREATE_PAIR_CHAT, new Action<Chat>() {
            public void execute(final Chat pairChat) {
                multiChatDialog.createPairChat(pairChat);
            }
        });

        dispatcher.subscribe(ACTIVATE_CHAT, new Action<Chat>() {
            public void execute(final Chat chat) {
                multiChatDialog.activateChat(chat);
            }
        });

        dispatcher.subscribe(MESSAGE_RECEIVED, new Action<ChatMessageParam>() {
            public void execute(final ChatMessageParam param) {
                multiChatDialog.messageReceived(param);
            }
        });

        dispatcher.subscribe(SET_GROUPCHAT_SUBJECT, new Action<GroupChatSubjectParam>() {
            public void execute(final GroupChatSubjectParam param) {
                multiChatDialog.groupChatSubjectChanged(param.getChat(), param.getSubject());
            }
        });

        dispatcher.subscribe(ADD_USER_TO_GROUP_CHAT, new Action<GroupChatUserAddActionParam>() {
            public void execute(final GroupChatUserAddActionParam param) {
                multiChatDialog.addUsetToGroupChat(param.getGroupChatId(), param.getGroupChatUser());
            }
        });

        dispatcher.subscribe(ADD_PRESENCE_BUDDY, new Action<PairChatUser>() {
            public void execute(final PairChatUser param) {
                multiChatDialog.addPresenceBuddy(param);
            }
        });
    }

    @Override
    protected void stop() {
        multiChatDialog.destroy();
    }

}
