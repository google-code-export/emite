package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPanel;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatInputMessageParam;
import com.calclab.examplechat.client.chatuiplugin.params.ChatOutputMessageParam;
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
        dispatcher.subscribe(OPEN_CHAT_DIALOG, new Action<AbstractChatUser>() {
            public void execute(final AbstractChatUser user) {
                if (extChatDialog == null) {
                    createChatDialog(user);
                }
                extChatDialog.show();
            }

            private void createChatDialog(final AbstractChatUser user) {
                extChatDialog = new MultiChatPresenter(user, new MultiChatListener() {

                    public void onStatusSelected(final int status) {
                        dispatcher.fire(ChatDialogPlugin.ON_STATUS_SELECTED, new Integer(status));
                    }

                    public void onSendMessage(final AbstractChat chat, final String message) {
                        dispatcher.fire(ChatDialogPlugin.ON_MESSAGE_SENDED, new ChatOutputMessageParam(chat, message));
                        extChatDialog.messageReceived(chat.getId(), chat.getSessionUserAlias(), message);
                    }

                    public void onClosePairChat(final PairChatPresenter pairChat) {
                        dispatcher.fire(ChatDialogPlugin.ON_PAIR_CHAT_CLOSED, pairChat);
                    }

                    public void onCloseGroupChat(final GroupChat groupChat) {
                        dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_CLOSED, groupChat);
                    }

                    public void setGroupChatSubject(final GroupChat groupChat, final String subject) {
                        dispatcher.fire(ChatDialogPlugin.ON_GROUP_CHAT_SUBJECT_CHANGED, new GroupChatSubjectParam(
                                groupChat.getChatTitle(), subject));
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
                MultiChatPanel multiChatPanel = new MultiChatPanel(new I18nTranslationServiceMocked(), extChatDialog);
                extChatDialog.init(multiChatPanel);
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
                extChatDialog.createGroupChat(params.getGroupChatName(), params.getUserAlias(), params
                        .getGroupChatUserType());
            }
        });

        dispatcher.subscribe(CREATE_PAIR_CHAT, new Action<PairChatUser>() {
            public void execute(final PairChatUser pairChat) {
                extChatDialog.createPairChat(pairChat);
            }
        });

        dispatcher.subscribe(ACTIVATE_CHAT, new Action<String>() {
            public void execute(final String chatId) {
                extChatDialog.activateChat(chatId);
            }
        });

        dispatcher.subscribe(MESSAGE_RECEIVED, new Action<ChatInputMessageParam>() {
            public void execute(final ChatInputMessageParam param) {
                extChatDialog.messageReceived(param.getChatId(), param.getFromUser(), param.getMessage());
            }
        });

        dispatcher.subscribe(SET_GROUPCHAT_SUBJECT, new Action<GroupChatSubjectParam>() {
            public void execute(final GroupChatSubjectParam param) {
                extChatDialog.groupChatSubjectChanged(param.getChatId(), param.getSubject());
            }
        });

        dispatcher.subscribe(ADD_USER_TO_GROUP_CHAT, new Action<GroupChatUserAddActionParam>() {
            public void execute(final GroupChatUserAddActionParam param) {
                extChatDialog.addUsetToGroupChat(param.getGroupChatId(), param.getGroupChatUser());
            }
        });
    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
