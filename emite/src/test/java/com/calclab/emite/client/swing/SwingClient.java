package com.calclab.emite.client.swing;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.TestHelper;
import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.swing.ChatPanel.ChatPanelListener;
import com.calclab.emite.client.swing.LoginPanel.LoginPanelListener;
import com.calclab.emite.client.swing.RosterPanel.RosterPanelListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;

public class SwingClient {

    public static void main(final String args[]) {
        new SwingClient(new JFrame("emite swing client")).start();
    }
    private final ConversationsPanel conversationsPanel;
    private final LoginPanel loginPanel;

    private final JPanel root;

    private final RosterPanel rosterPanel;
    private AbstractXmpp xmpp;
    private final JFrame frame;

    public SwingClient(final JFrame frame) {
        this.frame = frame;
        root = new JPanel(new BorderLayout());

        loginPanel = new LoginPanel(new LoginPanelListener() {
            public void onLogin(final String httpBase, final String domain, final String userName, final String password) {
                xmpp.login(userName, password);
            }

            public void onLogout() {
                xmpp.logout();
            }

        });
        rosterPanel = new RosterPanel(frame, new RosterPanelListener() {
            public void onAddRosterItem(final String uri, final String name) {
                xmpp.getRoster().requestAddItem(uri, name, null);
            }

            public void onStartChat(final RosterItem item) {
                xmpp.getChat().newChat(item.getXmppURI());
            }
        });
        conversationsPanel = new ConversationsPanel();
        root.add(loginPanel, BorderLayout.NORTH);
        root.add(rosterPanel, BorderLayout.EAST);
        root.add(conversationsPanel, BorderLayout.CENTER);

        initXMPP();
    }

    private void initXMPP() {
        this.xmpp = TestHelper.createXMPP(new HttpConnectorListener() {
            public void onError(final String id, final String cause) {
                print((id + "-ERROR: " + cause));
            }

            public void onFinish(final String id, final long duration) {
                print((id + "-FINISH " + duration + "ms"));
            }

            public void onResponse(final String id, final String response) {
                print((id + "-RESPONSE: " + response));
            }

            public void onSend(final String id, final String xml) {
                print((id + "-SENDING: " + xml));
            }

            public void onStart(final String id) {
                print((id + "-STARTED: " + id));
            }

        });
        xmpp.getSession().addListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                print("STATE: " + current);
                loginPanel.showState("state: " + current.toString(), current == State.connected);
            }
        });
        xmpp.getChat().addListener(new ChatManagerListener() {
            public void onChatCreated(final Chat chat) {
                final ChatPanel chatPanel = conversationsPanel.createChat(chat.getID(), new ChatPanelListener() {
                    public void onSend(final ChatPanel source, final String text) {
                        chat.send(text);
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
        xmpp.getRoster().addListener(new RosterListener() {
            public void onRosterInitialized(final List<RosterItem> items) {
                print("ROSTER INITIALIZED");
                for (final RosterItem item : items) {
                    rosterPanel.add(item.getName(), item);
                }
            }
        });
        xmpp.getPresenceManager().addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                print("PRESENCE!!: " + presence);
            }

            public void onUnsubscriptionReceived(final Presence presence) {
                print("UNSUBSCRIPTION!!: " + presence);
            }

            public void onSubscriptionRequest(final Presence presence) {
                final Object message = presence.getFrom() + " solicita añadirse a tu roster. ¿quires?";
                final int result = JOptionPane.showConfirmDialog(frame, message);
                if (result == JOptionPane.OK_OPTION) {
                    xmpp.getPresenceManager().acceptSubscription(presence);
                }
                print("SUBSCRIPTION: " + presence);
            }
        });
    }

    private void print(final String message) {
        Log.info(message);
    }

    private void start() {
        frame.setContentPane(root);
        frame.setSize(600, 400);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                xmpp.logout();
                System.exit(0);
            }
        });
    }
}
