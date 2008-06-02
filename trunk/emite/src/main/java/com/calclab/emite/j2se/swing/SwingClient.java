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
package com.calclab.emite.j2se.swing;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.xep.muc.MUCModule;
import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xep.muc.RoomInvitation;
import com.calclab.emite.client.xep.muc.RoomListener;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.j2se.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.j2se.swing.LoginPanel.LoginPanelListener;
import com.calclab.emite.j2se.swing.RoomPanel.RoomPanelListener;
import com.calclab.emite.j2se.swing.RosterPanel.RosterPanelListener;
import com.calclab.modular.client.signal.Listener;

public class SwingClient {

    private final ConversationsPanel conversationsPanel;
    private final JFrame frame;

    private final LoginPanel loginPanel;

    private final RoomsPanel roomsPanel;
    private final JPanel root;
    private final RosterPanel rosterPanel;
    private final JLabel status;
    private final JTabbedPane tabs;
    private final Xmpp xmpp;

    public SwingClient(final Xmpp xmpp) {
        this.xmpp = xmpp;
        this.frame = new JFrame("emite swing client");
        root = new JPanel(new BorderLayout());
        addXmppListeners();

        loginPanel = new LoginPanel(new LoginPanelListener() {
            public void onLogin(final String httpBase, final String domain, final String userName, final String password) {
                final String resource = "emite-swing";
                xmpp.setHttpBase(httpBase);
                xmpp.login(new XmppURI(userName, domain, resource), password, Presence.Show.dnd, "do not disturb at: "
                        + new Date().toString());
            }

            public void onLogout() {
                xmpp.logout();
            }

        });

        loginPanel.addConfiguration(new ConnectionConfiguration("empty", "", "", "", ""));
        loginPanel.addConfiguration(new ConnectionConfiguration("admin @ local openfire",
                "http://localhost:8383/http-bind/", "localhost", "admin", "easyeasy"));
        loginPanel.addConfiguration(new ConnectionConfiguration("dani @ local ejabberd",
                "http://localhost:5280/http-bind/", "mandarine", "dani", "dani"));
        loginPanel.addConfiguration(new ConnectionConfiguration("dani @ emite demo",
                "http://emite.ourproject.org/proxy", "emitedemo.ourproject.org", "dani", "dani"));
        loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty proxy",
                "http://localhost:4444/http-bind", "localhost", "test1", "test1"));
        loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty bosh servlet",
                "http://emite.ourproject.org/proxy", "localhost", "test1", "test1"));

        rosterPanel = new RosterPanel(frame, new RosterPanelListener() {
            public void onAddRosterItem(final String uri, final String name) {
                xmpp.getRosterManager().requestAddItem(uri(uri), name, null);
            }

            public void onRemoveItem(final RosterItem item) {
                xmpp.getRosterManager().requestRemoveItem(item.getJID());
            }

            public void onStartChat(final RosterItem item) {
                xmpp.getChatManager().openChat(item.getJID(), null, null);
            }
        });

        roomsPanel = new RoomsPanel(new RoomsPanelListener() {
            public void onRoomEnterd(final String roomName) {
                final RoomManager roomManager = MUCModule.getRoomManager(xmpp);
                roomManager.openChat(uri(roomName), null, null);
            }
        });

        conversationsPanel = new ConversationsPanel();
        status = new JLabel("emite test client");

        root.add(loginPanel, BorderLayout.NORTH);
        root.add(conversationsPanel, BorderLayout.CENTER);
        root.add(status, BorderLayout.SOUTH);

        tabs = new JTabbedPane();
        tabs.add("chats", rosterPanel);
        tabs.add("rooms", roomsPanel);

        root.add(tabs, BorderLayout.EAST);

    }

    public void start() {
        frame.setContentPane(root);
        frame.setSize(900, 400);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (xmpp != null) {
                    xmpp.stop();
                }
                System.exit(0);
            }
        });
    }

    private void addXmppListeners() {
        xmpp.getSession().onStateChanged(new Listener<State>() {
            public void onEvent(final State current) {
                print("STATE: " + current);
                final boolean isConnected = current == State.ready;
                loginPanel.showState("state: " + current.toString(), isConnected);
                tabs.setEnabled(isConnected);
                if (current == State.disconnected) {
                    rosterPanel.clear();
                } else if (current == State.notAuthorized) {
                    JOptionPane.showMessageDialog(frame, "lo siento, tienes mal la contraseña -o el usuario ;)-");
                }
            }
        });

        xmpp.getChatManager().onChatCreated(new Listener<Chat>() {
            public void onEvent(final Chat chat) {
                final ChatPanel chatPanel = conversationsPanel.createChat(chat.getOtherURI().toString(), chat.getID(),
                        new ChatPanelListener() {
                            public void onClose(final ChatPanel source) {
                                xmpp.getChatManager().close(chat);

                            }

                            public void onSend(final ChatPanel source, final String text) {
                                chat.send(new Message(text));
                                source.clearMessage();
                            }

                        });
                chat.addListener(new ChatListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        chatPanel.showIcomingMessage(message.getFrom(), message.getBody());
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        chatPanel.showOutMessage(message.getBody());
                    }
                });
                chatPanel.clearMessage();
            }
        });

        xmpp.getChatManager().onChatClosed(new Listener<Chat>() {
            public void onEvent(final Chat chat) {
                conversationsPanel.close(chat.getID());
            }
        });

        final RoomManager roomManager = MUCModule.getRoomManager(xmpp);

        roomManager.onChatCreated(new Listener<Chat>() {
            public void onEvent(final Chat room) {
                final RoomPanel roomPanel = conversationsPanel.createRoom(room.getOtherURI(), room.getID(),
                        new RoomPanelListener() {
                            public void onClose(final ChatPanel source) {
                                MUCModule.getRoomManager(xmpp).close(room);
                            }

                            public void onInviteUser(final String userJid, final String reasonText) {
                                ((Room) room).sendInvitationTo(userJid, reasonText);
                            }

                            public void onModifySubject(final String newSubject) {
                                ((Room) room).setSubject(newSubject);
                            }

                            public void onSend(final ChatPanel source, final String text) {
                                room.send(new Message(text));
                                source.clearMessage();
                            }
                        });
                room.addListener(new RoomListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        roomPanel.showIcomingMessage(message.getFrom(), message.getBody());
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        roomPanel.showOutMessage(message.getBody());
                    }

                    public void onOccupantModified(final Occupant occupant) {
                    }

                    public void onOccupantsChanged(final Collection<Occupant> users) {
                        roomPanel.setUsers(users);
                    }

                    public void onSubjectChanged(final String nick, final String newSubject) {
                        roomPanel.showIcomingMessage(nick, "New subject: " + newSubject);
                    }
                });
            }
        });

        roomManager.onChatClosed(new Listener<Chat>() {
            public void onEvent(final Chat chat) {
                conversationsPanel.close(chat.getID());
            }
        });

        roomManager.onInvitationReceived(new Listener<RoomInvitation>() {
            public void onEvent(final RoomInvitation invitation) {
                roomManager.openChat(invitation.getRoomURI(), null, null);
            }
        });

        Roster roster = xmpp.getRoster();
        roster.onItemChanged(new Listener<RosterItem>() {
            public void onEvent(final RosterItem item) {
                print("ROSTER ITEM PRESENCE CHANGED");
                rosterPanel.refresh();
            }
        });
        roster.onRosterChanged(new Listener<Collection<RosterItem>>() {

            public void onEvent(final Collection<RosterItem> items) {
                print("ROSTER INITIALIZED");
                rosterPanel.clear();
                for (final RosterItem item : items) {
                    rosterPanel.add(item.getName(), item);
                }
            }
        });

        xmpp.getRosterManager().onSubscriptionRequested(new Listener<Presence>() {
            public void onEvent(final Presence presence) {
                final Object message = presence.getFrom() + " want to add you to his/her roster. Accept?";
                final int result = JOptionPane.showConfirmDialog(frame, message);
                if (result == JOptionPane.OK_OPTION) {
                    xmpp.getRosterManager().acceptSubscription(presence);
                }
                print("SUBSCRIPTION: " + presence);
            }
        });

        xmpp.getPresenceManager().addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                print("PRESENCE!!: " + presence);
            }

        });
    }

    private void print(final String message) {
        Log.info(message);
        if (status != null) {
            status.setText(message);
        }
    }
}
