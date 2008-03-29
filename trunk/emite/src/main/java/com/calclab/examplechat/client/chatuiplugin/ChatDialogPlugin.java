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

    // Output events
    public static final String STATUS_SELECTED = "chatplugin.statusselected";
    public static final String MESSAGE_SENDED = "chatplugin.messagesended";
    public static final String PAIR_CHAT_CLOSED = "chatplugin.pairchatclosed";
    public static final String GROUP_CHAT_CLOSED = "chatplugin.groupchatclosed";
    public static final String GROUP_CHAT_SUBJECT_CHANGED = "chatplugin.groupchatsubjectchanged";
    public static final String USER_COLOR_SELECTED = "chatplugin.usercolorselected";

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
                        getDispatcher().fire(ChatDialogPlugin.STATUS_SELECTED, new Integer(status));
                    }

                    public void onSendMessage(final AbstractChat chat, final String message) {
                        getDispatcher().fire(ChatDialogPlugin.MESSAGE_SENDED, new AbstractChatMessage(chat, message));
                    }

                    public void onClosePairChat(final PairChatPresenter pairChat) {
                        getDispatcher().fire(ChatDialogPlugin.PAIR_CHAT_CLOSED, pairChat);
                    }

                    public void onCloseGroupChat(final GroupChat groupChat) {
                        getDispatcher().fire(ChatDialogPlugin.GROUP_CHAT_CLOSED, groupChat);
                    }

                    public void setGroupChatSubject(final GroupChat groupChat, final String subject) {
                        getDispatcher().fire(ChatDialogPlugin.GROUP_CHAT_SUBJECT_CHANGED,
                                new GroupChatSubject(groupChat, subject));
                    }

                    public void onUserColorChanged(final String color) {
                        getDispatcher().fire(ChatDialogPlugin.USER_COLOR_SELECTED, color);
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

    }

    @Override
    protected void stop() {
        extChatDialog.destroy();
    }

}
