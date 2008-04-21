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
package com.calclab.emiteuiplugin.client;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.dialog.MultiChat;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Image;

public class EmiteUIPlugin extends Plugin {

    // Input events
    public static final String CREATE_CHAT_DIALOG = "emiteuiplugin.openchatdialog";
    public static final String SHOW_CHAT_DIALOG = "emiteuiplugin.showchatdialog";
    public static final String HIDE_CHAT_DIALOG = "emiteuiplugin.hidechatdialog";
    public static final String SET_OWN_PRESENCE = "emiteuiplugin.setownpresence";
    public static final String CLOSE_ALLCHATS = "emiteuiplugin.closeallchats";
    public static final String CHATOPEN = "emiteuiplugin.chatopen";
    public static final String ROOMOPEN = "emiteuiplugin.roomopen";
    public static final String REFLESH_USER_OPTIONS = "emiteuiplugin.refreshuseroptions";

    // Output events
    public static final String ON_USER_COLOR_SELECTED = "emiteuiplugin.usercoloselected";
    public static final String ON_STATE_CONNECTED = "emiteuiplugin.onstateconnected";
    public static final String ON_STATE_DISCONNECTED = "emiteuiplugin.onstatedisconnected";
    public static final String ON_USER_SUBSCRIPTION_CHANGED = "emiteuiplugin.usersubschanged";
    public static final String ON_ROSTER_CHANGED = "emiteuiplugin.onrosterchanged";

    // Internal events
    public static final String ON_CANCEL_SUBSCRITOR = "emiteuiplugin.oncancelsubscriptor";
    public static final String ON_REQUEST_REMOVE_ROSTERITEM = "emiteuiplugin.onrequestremoveitem";
    public static final String ON_REQUEST_SUBSCRIBE = "emiteuiplugin.onrequestsubscribeitem";
    public static final String NO_ACTION = "emiteuiplugin.noaction";

    private MultiChat multiChatDialog;

    public EmiteUIPlugin() {
        super("emiteuiplugin");
    }

    @Override
    protected void start() {

        final Dispatcher dispatcher = getDispatcher();
        dispatcher.subscribe(CREATE_CHAT_DIALOG, new Action<MultiChatCreationParam>() {
            public void execute(final MultiChatCreationParam param) {
                if (multiChatDialog == null) {
                    createChatDialog(param);
                    preFetchImages();
                }
            }

            private void createChatDialog(final MultiChatCreationParam param) {
                final Xmpp xmpp = Xmpp.create(param.getBoshOptions());

                multiChatDialog = ChatDialogFactoryImpl.App.getInstance().createMultiChat(xmpp, param,
                        new MultiChatListener() {

                            public void attachToExtPoint(final UIExtensionElement extensionElement) {
                                dispatcher.fire(PlatformEvents.ATTACH_TO_EXT_POINT, extensionElement);
                            }

                            public void doAction(final String eventId, final Object param) {
                                dispatcher.fire(eventId, param);
                            }

                            public void onUserColorChanged(final String color) {
                                dispatcher.fire(EmiteUIPlugin.ON_USER_COLOR_SELECTED, color);
                            }

                            public void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
                                dispatcher.fire(EmiteUIPlugin.ON_USER_SUBSCRIPTION_CHANGED, subscriptionMode);
                            }

                        });

                dispatcher.subscribe(EmiteUIPlugin.CHATOPEN, new Action<XmppURI>() {
                    public void execute(final XmppURI param) {
                        xmpp.getChatManager().openChat(param);
                    }
                });

                dispatcher.subscribe(EmiteUIPlugin.ROOMOPEN, new Action<XmppURI>() {
                    public void execute(final XmppURI param) {
                        xmpp.getRoomManager().openChat(param);
                    }
                });

                dispatcher.subscribe(CLOSE_ALLCHATS, new Action<Boolean>() {
                    public void execute(final Boolean param) {
                        multiChatDialog.closeAllChats(param);
                    }
                });

                dispatcher.subscribe(REFLESH_USER_OPTIONS, new Action<UserChatOptions>() {
                    public void execute(final UserChatOptions param) {
                        multiChatDialog.setUserChatOptions(param);
                    }
                });

                dispatcher.subscribe(SHOW_CHAT_DIALOG, new Action<Object>() {
                    public void execute(final Object param) {
                        multiChatDialog.show();
                    }
                });

                dispatcher.subscribe(HIDE_CHAT_DIALOG, new Action<Object>() {
                    public void execute(final Object param) {
                        multiChatDialog.hide();
                    }
                });

                dispatcher.subscribe(SET_OWN_PRESENCE, new Action<OwnPresence>() {
                    public void execute(final OwnPresence ownPresence) {
                        multiChatDialog.setOwnPresence(ownPresence);
                    }
                });

            }

        });
    }

    @Override
    protected void stop() {
        multiChatDialog.destroy();
    }

    private void preFetchImages() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                String[] imgs = { "ext-load.gif", "group_add.gif", "group-chat.gif", "moderatoruser.gif",
                        "normaluser.gif", "person-def.gif", "smile.gif", "user_add.gif" };
                String[] cssImgs = { "add.gif", "cancel.gif", "chat.gif", "colors.gif ", "del.gif", "exit.gif",
                        "extload.gif", "forbidden.gif", "group-chat.gif", "group.gif", "new-chat.gif",
                        "new-message.gif", "useradd.gif", "userf.gif", "user.gif" };
                doTheJob(imgs, "images");
                doTheJob(cssImgs, "css/img");
            }

            private void doTheJob(final String[] imgs, final String prefix) {
                for (int i = 0; i < imgs.length; i++) {
                    String img = imgs[i];
                    Image.prefetch(prefix + "/" + img);
                }
            }
        });
    }
}
