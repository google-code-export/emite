package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPanel;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

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
        getDispatcher().subscribe(OPEN_CHAT_DIALOG, new Action<AbstractChatUser>() {
            public void execute(final AbstractChatUser user) {
                if (extChatDialog == null) {
                    createChatDialog(user);
                }
                extChatDialog.show();
            }

            private void createChatDialog(final AbstractChatUser user) {
                extChatDialog = new MultiChatPresenter(user, new MultiChatListener() {

                    public void onStatusSelected(final int status) {
                        getDispatcher().fire(ChatDialogPlugin.ON_STATUS_SELECTED, new Integer(status));
                    }

                    public void onSendMessage(final AbstractChat chat, final String message) {
                        getDispatcher().fire(ChatDialogPlugin.ON_MESSAGE_SENDED,
                                new AbstractChatOutputMessage(chat, message));
                    }

                    public void onClosePairChat(final PairChatPresenter pairChat) {
                        getDispatcher().fire(ChatDialogPlugin.ON_PAIR_CHAT_CLOSED, pairChat);
                    }

                    public void onCloseGroupChat(final GroupChat groupChat) {
                        getDispatcher().fire(ChatDialogPlugin.ON_GROUP_CHAT_CLOSED, groupChat);
                    }

                    public void setGroupChatSubject(final GroupChat groupChat, final String subject) {
                        getDispatcher().fire(ChatDialogPlugin.ON_GROUP_CHAT_SUBJECT_CHANGED,
                                new GroupChatSubject(groupChat.getChatTitle(), subject));
                    }

                    public void onUserColorChanged(final String color) {
                        getDispatcher().fire(ChatDialogPlugin.ON_USER_COLOR_SELECTED, color);
                    }
                });
                MultiChatPanel multiChatPanel = new MultiChatPanel(new I18nTranslationServiceMocked(), extChatDialog);
                extChatDialog.init(multiChatPanel);
            }
        });

        getDispatcher().subscribe(CLOSE_CHAT_DIALOG, new Action<Object>() {
            public void execute(final Object param) {
                extChatDialog.closeAllChats(true);
            }
        });

        getDispatcher().subscribe(SET_STATUS, new Action<Integer>() {
            public void execute(final Integer status) {
                if (extChatDialog != null) {
                    extChatDialog.setStatus(status);
                }
            }
        });

        getDispatcher().subscribe(CREATE_GROUP_CHAT, new Action<CreateGroupChatActionParams>() {
            public void execute(final CreateGroupChatActionParams params) {
                extChatDialog.createGroupChat(params.getGroupChatName(), params.getUserAlias(), params
                        .getGroupChatUserType());
            }
        });

        getDispatcher().subscribe(CREATE_PAIR_CHAT, new Action<PairChatUser>() {
            public void execute(final PairChatUser pairChat) {
                extChatDialog.createPairChat(pairChat);
            }
        });

        getDispatcher().subscribe(ACTIVATE_CHAT, new Action<AbstractChat>() {
            public void execute(final AbstractChat chat) {
                extChatDialog.activateChat(chat);
            }
        });

        getDispatcher().subscribe(MESSAGE_RECEIVED, new Action<AbstractChatInputMessage>() {
            public void execute(final AbstractChatInputMessage param) {
                extChatDialog.messageReceived(param.getChatId(), param.getFromUser(), param.getMessage());
            }
        });

        getDispatcher().subscribe(SET_GROUPCHAT_SUBJECT, new Action<GroupChatSubject>() {
            public void execute(final GroupChatSubject param) {
                extChatDialog.groupChatSubjectChanged(param.getChatId(), param.getSubject());
            }
        });

    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
