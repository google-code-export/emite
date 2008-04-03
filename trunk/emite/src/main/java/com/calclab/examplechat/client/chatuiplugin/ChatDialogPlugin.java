package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.params.CreateGroupChatActionParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatSubjectParam;
import com.calclab.examplechat.client.chatuiplugin.params.GroupChatUserAddActionParam;

public class ChatDialogPlugin extends Plugin {

    // Input events
    public static final String OPEN_CHAT_DIALOG = "chatplugin.openchatdialog";
    public static final String CLOSE_CHAT_DIALOG = "chatplugin.closechatdialog";
    public static final String SET_STATUS = "chatplugin.setstatus";
    public static final String CREATE_GROUP_CHAT = "chatplugin.creategroupchat";
    public static final String CREATE_PAIR_CHAT = "chatplugin.createpairchat";
    public static final String ACTIVATE_CHAT = "chatplugin.activatechat";
    public static final String MESSAGE_RECEIVED = "chatplugin.messagereceived";
    public static final String SET_GROUPCHAT_SUBJECT = "chatplugin.groupchatsubjectchanged";
    public static final String ADD_USER_TO_GROUP_CHAT = "chatplugin.addusertogroupchat";
    public static final String ADD_PRESENCE_BUDDY = "chatplugin.addpresencebuddy";

    // Output events
    public static final String ON_STATUS_SELECTED = "chatplugin.onstatusselected";
    public static final String ON_MESSAGE_SENDED = "chatplugin.onmessagesended";
    public static final String ON_PAIR_CHAT_CLOSED = "chatplugin.onpairchatclosed";
    public static final String ON_GROUP_CHAT_CLOSED = "chatplugin.ongroupchatclosed";
    public static final String ON_GROUP_CHAT_SUBJECT_CHANGED = "chatplugin.ongroupchatsubjectchanged";
    public static final String ON_USER_COLOR_SELECTED = "chatplugin.onusercolorselected";

    private MultiChatPresenter extChatDialog;

    public ChatDialogPlugin() {
        super("chatplugin");
    }

    @Override
    protected void start() {
        final Dispatcher dispatcher = getDispatcher();
        dispatcher.subscribe(OPEN_CHAT_DIALOG, new Action<PairChatUser>() {
            public void execute(final PairChatUser user) {
                if (extChatDialog == null) {
                    createChatDialog(user);
                }
                extChatDialog.show();
            }

            private void createChatDialog(final PairChatUser user) {
                ChatDialogFactoryImpl.App.getInstance().createMultiChat(user, new I18nTranslationServiceMocked(),
                        new MultiChatListener() {

                            public void onStatusSelected(final int status) {
                                dispatcher.fire(ChatDialogPlugin.ON_STATUS_SELECTED, new Integer(status));
                            }

                            public void onSendMessage(final ChatMessageParam message) {
                                dispatcher.fire(ChatDialogPlugin.ON_MESSAGE_SENDED, message);
                            }

                            public void onClosePairChat(final PairChatPresenter pairChat) {
                                dispatcher.fire(ChatDialogPlugin.ON_PAIR_CHAT_CLOSED, pairChat);
                            }

                            public void onCloseGroupChat(final GroupChat groupChat) {
                                dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_CLOSED, groupChat);
                            }

                            public void setGroupChatSubject(final Chat groupChat, final String subject) {
                                dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_SUBJECT_CHANGED,
                                        new GroupChatSubjectParam(groupChat, subject));
                            }

                            public void onUserColorChanged(final String color) {
                                dispatcher.fire(ChatDialogPlugin.ON_USER_COLOR_SELECTED, color);
                            }

                            public void attachToExtPoint(final UIExtensionElement extensionElement) {
                                dispatcher.fire(PlatformEvents.ATTACH_TO_EXT_POINT, extensionElement);
                            }

                            public void doAction(final String eventId, final Object param) {
                                dispatcher.fire(eventId, param);
                            }
                        });
            }
        });

        dispatcher.subscribe(CLOSE_CHAT_DIALOG, new Action<Object>() {
            public void execute(final Object param) {
                extChatDialog.closeAllChats(true);
            }
        });

        dispatcher.subscribe(SET_STATUS, new Action<Integer>() {
            public void execute(final Integer status) {
                if (extChatDialog != null) {
                    extChatDialog.setStatus(status);
                }
            }
        });

        dispatcher.subscribe(CREATE_GROUP_CHAT, new Action<CreateGroupChatActionParam>() {
            public void execute(final CreateGroupChatActionParam params) {
                extChatDialog.createGroupChat(params.getGroupChat(), params.getUserAlias(), params
                        .getGroupChatUserType());
            }
        });

        dispatcher.subscribe(CREATE_PAIR_CHAT, new Action<Chat>() {
            public void execute(final Chat pairChat) {
                extChatDialog.createPairChat(pairChat);
            }
        });

        dispatcher.subscribe(ACTIVATE_CHAT, new Action<Chat>() {
            public void execute(final Chat chat) {
                extChatDialog.activateChat(chat);
            }
        });

        dispatcher.subscribe(MESSAGE_RECEIVED, new Action<ChatMessageParam>() {
            public void execute(final ChatMessageParam param) {
                extChatDialog.messageReceived(param);
            }
        });

        dispatcher.subscribe(SET_GROUPCHAT_SUBJECT, new Action<GroupChatSubjectParam>() {
            public void execute(final GroupChatSubjectParam param) {
                extChatDialog.groupChatSubjectChanged(param.getChat(), param.getSubject());
            }
        });

        dispatcher.subscribe(ADD_USER_TO_GROUP_CHAT, new Action<GroupChatUserAddActionParam>() {
            public void execute(final GroupChatUserAddActionParam param) {
                extChatDialog.addUsetToGroupChat(param.getGroupChatId(), param.getGroupChatUser());
            }
        });

        dispatcher.subscribe(ADD_PRESENCE_BUDDY, new Action<PairChatUser>() {
            public void execute(final PairChatUser param) {
                extChatDialog.addPresenceBuddy(param);
            }
        });
    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
