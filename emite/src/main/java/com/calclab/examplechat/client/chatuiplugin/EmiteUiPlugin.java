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

import java.util.List;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;

public class EmiteUiPlugin extends Plugin {

    // Input events
    public static final String OPEN_MULTI_CHAT_DIALOG = "emiteuiplugin.openchatdialog";
    public static final String SET_STATUS = "emiteuiplugin.setstatus";
    public static final String ACTIVATE_CHAT = "emiteuiplugin.activatechat";
    public static final String CLOSE_CHAT_DIALOG = "emiteuiplugin.closechatdialog";

    // Output messages
    public static final String ON_USER_COLOR_SELECTED = "emiteuiplugin.usercoloselected";
    public static final String ON_PAIR_CHAT_START = "emiteuiplugin.onpairchatstart";
    public static final String ON_STATE_CONNECTED = "emiteuiplugin.onstateconnected";
    public static final String ON_STATE_DISCONNECTED = "emiteuiplugin.onstatedisconnected";

    private MultiChat multiChatDialog;

    public EmiteUiPlugin() {
        super("emiteuiplugin");
    }

    @Override
    protected void start() {
        final Dispatcher dispatcher = getDispatcher();
        dispatcher.subscribe(OPEN_MULTI_CHAT_DIALOG, new Action<MultiChatCreationParam>() {
            public void execute(final MultiChatCreationParam param) {
                if (multiChatDialog == null) {
                    createChatDialog(param);
                }
                multiChatDialog.show();
            }

            private void createChatDialog(final MultiChatCreationParam param) {
                final PairChatUser sessionUser = param.getSessionUser();
                final Xmpp xmpp = param.getXmpp();
                multiChatDialog = ChatDialogFactoryImpl.App.getInstance().createMultiChat(sessionUser,
                        new I18nTranslationServiceMocked(), new MultiChatListener() {

                            public void attachToExtPoint(final UIExtensionElement extensionElement) {
                                dispatcher.fire(PlatformEvents.ATTACH_TO_EXT_POINT, extensionElement);
                            }

                            public void doAction(final String eventId, final Object param) {
                                dispatcher.fire(eventId, param);
                            }

                            public void onCloseGroupChat(final GroupChat groupChat) {
                            }

                            public void onClosePairChat(final PairChatPresenter pairChat) {
                            }

                            public void onStatusSelected(final int status) {
                                switch (status) {
                                case MultiChatView.STATUS_ONLINE:
                                    xmpp.login(sessionUser.getUri().toString(), param.getUserPassword());
                                    break;
                                case MultiChatView.STATUS_OFFLINE:
                                    xmpp.logout();
                                    break;
                                case MultiChatView.STATUS_BUSY:
                                    break;
                                case MultiChatView.STATUS_INVISIBLE:
                                    break;
                                case MultiChatView.STATUS_XA:
                                    break;
                                case MultiChatView.STATUS_AWAY:
                                    break;
                                default:
                                    throw new IndexOutOfBoundsException("Xmpp status unknown");
                                }
                            }

                            public void onUserColorChanged(final String color) {
                                dispatcher.fire(EmiteUiPlugin.ON_USER_COLOR_SELECTED, color);
                            }

                            public void setGroupChatSubject(final Chat groupChat, final String subject) {
                                Log.info("Group '" + groupChat + "' changed subject to '" + subject
                                        + "' (not implemented yet emite connection");
                            }

                            public void onPresenceAccepted(final Presence presence) {
                                Log.info("Presence accepted in ui");
                                xmpp.getPresenceManager().acceptSubscription(presence);
                            }

                            public void onPresenceNotAccepted(final Presence presence) {
                                Log.info("Presence not accepted in ui");
                            }
                        });

                dispatcher.subscribe(EmiteUiPlugin.ON_PAIR_CHAT_START, new Action<XmppURI>() {
                    public void execute(final XmppURI param) {
                        xmpp.getChat().newChat(param);
                    }
                });

                dispatcher.subscribe(CLOSE_CHAT_DIALOG, new Action<Object>() {
                    public void execute(final Object param) {
                        multiChatDialog.closeAllChats(true);
                    }
                });

                dispatcher.subscribe(SET_STATUS, new Action<Integer>() {
                    public void execute(final Integer status) {
                        multiChatDialog.setStatus(status);
                    }
                });

                dispatcher.subscribe(ACTIVATE_CHAT, new Action<Chat>() {
                    public void execute(final Chat chat) {
                        multiChatDialog.activateChat(chat);
                    }
                });

                createXmppListeners(dispatcher, xmpp);

            }
        });
    }

    @Override
    protected void stop() {
        multiChatDialog.destroy();
    }

    private void createXmppListeners(final Dispatcher dispatcher, final Xmpp xmpp) {
        xmpp.getSession().addListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                Log.info("STATE CHANGED: " + current + " - old: " + old);
                switch (current) {
                case connected:
                    multiChatDialog.setStatus(MultiChatView.STATUS_ONLINE);
                    dispatcher.fire(ON_STATE_CONNECTED, null);
                    break;
                case connecting:
                    break;
                case disconnected:
                    multiChatDialog.setStatus(MultiChatView.STATUS_OFFLINE);
                    dispatcher.fire(ON_STATE_DISCONNECTED, null);
                    break;
                }
            }
        });

        xmpp.getRoster().addListener(new RosterListener() {
            public void onRosterInitialized(final List<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    Log.info("Rooster, adding: " + item.getXmppURI() + " name: " + item.getName() + " subsc: "
                            + item.getSubscription());
                    multiChatDialog.addRosterItem(new PairChatUser("images/person-def.gif", item.getXmppURI(), item
                            .getXmppURI().toString(), "maroon", createPresenceForTest()));
                }
            }
        });

        xmpp.getChat().addListener(new ChatManagerListener() {
            public void onChatCreated(final Chat chat) {
                multiChatDialog.createPairChat(chat);
                chat.addListener(new ChatListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        multiChatDialog.messageReceived(chat, message);
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        multiChatDialog.messageReceived(chat, message);
                    }
                });
            }
        });

        xmpp.getPresenceManager().addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                Log.debug("PRESENCE: " + presence.getFromURI());
            }

            public void onSubscriptionRequest(final Presence presence) {
                Log.debug("SUBSCRIPTION REQUEST: " + presence);
                multiChatDialog.onSubscriptionRequest(presence);
            }
        });
    }

    // TODO
    // xmpp.getRoster().requestAddItem(uri, name, null);

    private Presence createPresenceForTest() {
        Presence presence = new Presence();
        presence.setShow(Presence.Show.available);
        presence.setType(Presence.Type.available.toString());
        presence.setStatus("I\'m out for dinner");
        return presence;
    }

}
